/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.system.schedule.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;

import br.com.system.schedule.model.Agenda;
import br.com.system.schedule.model.Usuario;
import br.com.system.schedule.service.AgendaService;

@Model
public class AgendaController {

	@Inject
    private FacesContext facesContext;

    @Inject
    private AgendaService agendaService;
    
    private Agenda agenda;
    
    private Usuario usuarioLogado;

    private Locale localeCalendar = new Locale("pt", "BR");
    
    @Produces
    @Named
    public Agenda getAgenda() {
        return agenda;
    }
    
    @PostConstruct
    public void initAgenda() {
    	agenda = new Agenda();
    	usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    	agenda.setUsuario(usuarioLogado);
    	agenda.setDataInclusao(new Date());
    	agenda.setTipoCadastro("M");
    	agenda.setSituacao("A");
    }

    public List<String> autoCompleteNome(String nome) throws Exception {
        List<Agenda> listaAgenda = agendaService.consultarRemetente(nome, usuarioLogado);
        Set<String> results = new HashSet<String>();
        List<String> retorno = new ArrayList<String>();
        for(Agenda agenda : listaAgenda){
        	String telefone = agenda.getCelular().toString();
        	results.add(agenda.getNome() +" - "+ "(" + telefone.substring(0, 2) + ")" + telefone.substring(2, 3) + "." + telefone.substring(3, 7) + "-"  + telefone.substring(7, 11));
        }
        retorno.addAll(results);
        return retorno;
    }
    
    public void onItemSelect(SelectEvent event) {
    	String itemSelecionado = event.getObject().toString();
    	String itens[] = itemSelecionado.split(" - ");
    	String nome = itens[0];
    	String celular = itens[1];
    	celular = celular.replace("(", "");
    	celular = celular.replace(")", "");
    	celular = celular.replace("-", "");
    	celular = celular.replace(".", "");
    	Long celularLong = Long.parseLong(celular);
    	
    	agenda.setNome(nome);
    	agenda.setCelular(celularLong);
    }

    public String formataData(Date data, String formato){
    	SimpleDateFormat sdf = new SimpleDateFormat(formato);
    	String dataFormat = sdf.format(data);
    	return dataFormat;
    }
    
    public void inserirAgenda() throws Exception {
        try {
        	Date dataAtual = new Date();
        	dataAtual.setHours(dataAtual.getHours()+2);
        	
        	if(agenda.getDataEvento().compareTo(dataAtual) < 0 ){
        		throw new Exception("A data/hora do evento deve ser 2 horas superior a data/hora atual");
        	}
            agendaService.inserirAgenda(agenda);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agenda salva com sucesso", "Sucesso"));
            initAgenda();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }

    public void excluirAgenda(Agenda agenda) throws Exception {
        try {
            agendaService.excluirAgenda(agenda);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agenda excluida com sucesso", "Sucesso"));
            initAgenda();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }
    

    public Date getDataAtual(){
    	return new Date();
    }
    
    public void selecionarAgenda(Agenda agenda) throws Exception {
    	this.agenda = agenda;
    }

    
    public List<Agenda> listarAgenda() throws Exception{
    	List<Agenda> listaAgenda = null;
    	try {
    		listaAgenda = agendaService.listarAgenda(usuarioLogado);	
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
		}
    	
    	return listaAgenda;
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

	public Locale getLocaleCalendar() {
		return localeCalendar;
	}

	public void setLocaleCalendar(Locale localeCalendar) {
		this.localeCalendar = localeCalendar;
	}

}
