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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import br.com.system.schedule.model.Usuario;
import br.com.system.schedule.service.UsuarioService;

@Model
public class UsuarioController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private UsuarioService usuarioService;
    
    private Usuario usuario;
    
    private String novaSenha;
    
    private String repitaNovaSenha;
    
    private Usuario usuarioLogado;

    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{6,10})";
    
    @Produces
    @Named
    public Usuario getUsuario() {
        return usuario;
    }
    
    @PostConstruct
    public void initUsuario() {
    	novaSenha=null;
		repitaNovaSenha=null;
    	usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    	usuario = new Usuario();
    }

    
    public void alterarSenha() throws Exception {
    	try {
    		if(novaSenha == null || "".equals(novaSenha)){
    			novaSenha=null;
    			repitaNovaSenha=null;
    			throw new Exception("Senha é obrigatória");
    		}
    		
    		if(repitaNovaSenha == null || "".equals(repitaNovaSenha)){
    			novaSenha=null;
    			repitaNovaSenha=null;
    			throw new Exception("Senha é obrigatória");
    		}
    		
    		if(!repitaNovaSenha.equals(novaSenha)){
    			novaSenha=null;
    			repitaNovaSenha=null;
    			throw new Exception("Senhas não conferem");
    		}
    		
    		Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
    		Matcher matcher = pattern.matcher(novaSenha);
  		  	
    		if(!matcher.matches()){
    			throw new Exception("Senha não respeita as diretrizes");
    		}
    		
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Senha alterada com sucesso", "Sucesso"));
            usuarioLogado.setSenha(novaSenha);
            usuarioService.alterarSenha(usuarioLogado);
    	} catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }
    
    public String login() throws Exception {
        try {
            Usuario usuarioLogado = usuarioService.login(usuario.getEmail(), usuario.getSenha());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogado", usuarioLogado);
            return "/protected/agenda.jsf?faces-redirect=true";
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
            return "index.jsf";
        }
    }


    public String logout() throws Exception {
        try {
        	HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);  
            sessao.invalidate();  
            
            return "index";
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
            return "index.jsf?faces-redirect=true";
        }
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

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getRepitaNovaSenha() {
		return repitaNovaSenha;
	}

	public void setRepitaNovaSenha(String repitaNovaSenha) {
		this.repitaNovaSenha = repitaNovaSenha;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}
}
