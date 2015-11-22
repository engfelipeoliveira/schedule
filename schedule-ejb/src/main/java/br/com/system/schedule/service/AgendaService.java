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
package br.com.system.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.system.schedule.model.Agenda;
import br.com.system.schedule.model.Usuario;

@Stateless
public class AgendaService {

	
	public static void main(String[] args) {
		Date a = new Date();
		System.out.println(a);
	}
	
    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    private final String NOME_CLASS = "AgendaService";
    
    public void atualizarRetornoSms(Long id, String retornoSms) throws Exception {
    	logger.info("Consultando Agenda por id:" + id);
        
        if(id == null){
        	logger.log(Level.INFO, NOME_CLASS +".atualizarRetornoSms() - Id Agenda e obrigatoria");
        	throw new Exception("Id Agenda e obrigatoria");
        }else{
        	String sql = " from Agenda a where a.id = :id";
            Agenda agenda = null;
        	try {
                agenda = (Agenda)entityManager
            			.createQuery(sql.toString())
            			.setParameter("id", id)
            			.getSingleResult();
                
                agenda.setRetornoSms(retornoSms);
                agenda.setSituacao("R");
                entityManager.merge(agenda);
                entityManager.flush();
    			
    		} catch (NoResultException e) {
    			logger.log(Level.INFO, NOME_CLASS +".atualizarRetornoSms() - Nenhuma agenda cadastrada com o id informado");
    			throw new Exception("Nenhuma agenda cadastrada com o id informado");
    		}
        }
    }
    
    public List<Agenda> consultarRemetente(String nome, Usuario usuario) throws Exception {
    	logger.info("Consultando Remetente por nome:" + nome);
    	List<Agenda> agenda = null;
    	
        if("".equals(nome) || nome == null){
        	logger.log(Level.INFO, NOME_CLASS +".consultarRemetente() - Nome e obrigatorio");
        	throw new Exception("Nome e obrigatorio");
        }else{
        	String sql = " from Agenda a where upper(a.nome) like upper(:nome) and a.usuario = :usuario order by a.nome";
        	try {
                agenda = (List<Agenda>)entityManager
            			.createQuery(sql.toString())
            			.setParameter("nome", "%"+nome+"%")
            			.setParameter("usuario", usuario)
            			.getResultList();
                
                entityManager.flush();
    			
    		} catch (NoResultException e) {
    			logger.log(Level.INFO, NOME_CLASS +".consultarRemetente() - Nenhum remetente cadastrado com o nome informado");
    			throw new Exception("Nenhuma remetente cadastrado com o nome informado");
    		}
        }
        return agenda;
    }

    
    public void inserirAgenda(Agenda agenda) throws Exception {
    	logger.info("Inserindo Agenda");
        
        if(agenda == null){
        	logger.log(Level.INFO, NOME_CLASS +".inserirAgenda() - Agenda e obrigatoria");
        	throw new Exception("Agenda e obrigatoria");
        }else{
        	try {
        		entityManager.merge(agenda);
        		entityManager.flush();
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".inserirAgenda() - Erro ao inserir agenda");
				throw new Exception("Erro ao inserir agenda");
			}

        }
    }
    
    public void excluirAgenda(Agenda agenda) throws Exception {
    	logger.info("Excluindo Agenda");
        
        if(agenda == null){
        	logger.log(Level.INFO, NOME_CLASS +".excluirAgenda() - Agenda e obrigatoria");
        	throw new Exception("Agenda e obrigatoria");
        }else{
        	try {
                entityManager.remove(entityManager.getReference(Agenda.class, agenda.getId()));
                entityManager.flush();
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".excluirAgenda() - Erro ao excluir agenda");
				throw new Exception("Erro ao excluir agenda");
			}
        }
    }
    
    @SuppressWarnings("deprecation")
	public List<Agenda> listarAgenda(Usuario usuario) throws Exception {
    	logger.info("Listando agenda");
        
    	Date dataAtual = new Date();
    	dataAtual.setMonth(dataAtual.getMonth()-1);
    	
        List<Agenda> listaAgenda = new ArrayList<Agenda>();
        String sql = " from Agenda a where a.dataEvento >= :dataAtual and a.usuario = :usuario order by a.dataEvento desc";
                
    	try {
            listaAgenda = (List<Agenda>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("dataAtual", dataAtual)
        			.setParameter("usuario", usuario)
        			.getResultList();
            entityManager.flush();
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarAgenda() - Nenhuma agenda cadastrada");
			throw new Exception("Nenhuma agenda cadastrada");
		}
            	
    	return listaAgenda;
    }


	public List<Agenda> relatorioAgenda(Usuario usuario, Date dataInicio, Date dataFim, String nome, Long celular, String tipo, String situacao) throws Exception {
    	logger.info("Relatorio de agendamento");
    	
        List<Agenda> listaAgenda = new ArrayList<Agenda>();
        StringBuilder sql = new StringBuilder(" from Agenda a ");
        						   sql.append(" where a.usuario = :usuario ");
        						   sql.append("   and a.dataEvento between :dataInicio and :dataFim ");
        						if(nome != null && !"".equals(nome)){
        						   nome = nome.toUpperCase();
        						   sql.append("   and upper(a.nome) like '%"+nome+"%' ");
        						}
        						if(celular != null){
        						   sql.append("   and a.celular = " +celular );        							
        						}
        						if(tipo != null && !"".equals(tipo)){
          						   sql.append("   and a.tipoCadastro = '"+tipo+"' ");
          						}
        						if(situacao != null && !"".equals(situacao)){
          						   sql.append("   and a.situacao = '"+situacao+"' ");
          						}
        						   sql.append(" order by a.dataEvento desc ");
    	try {
            listaAgenda = (List<Agenda>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("usuario", usuario)
        			.setParameter("dataInicio", dataInicio)
        			.setParameter("dataFim", dataFim)
        			.getResultList();
            entityManager.flush();
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarAgenda() - Nenhuma agenda cadastrada");
			throw new Exception("Nenhuma agenda cadastrada");
		}
            	
    	return listaAgenda;
    }
    
}
