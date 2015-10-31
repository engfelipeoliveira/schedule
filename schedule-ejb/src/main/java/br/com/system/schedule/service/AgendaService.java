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

@Stateless
public class AgendaService {

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
                entityManager.merge(agenda);
                entityManager.flush();
    			
    		} catch (NoResultException e) {
    			logger.log(Level.INFO, NOME_CLASS +".atualizarRetornoSms() - Nenhuma agenda cadastrada com o id informado");
    			throw new Exception("Nenhuma agenda cadastrada com o id informado");
    		}
        }
    }
    
    
    public void inserirAgenda(Agenda agenda) throws Exception {
    	logger.info("Inserindo Agenda");
        
        if(agenda == null){
        	logger.log(Level.INFO, NOME_CLASS +".inserirAgenda() - Agenda e obrigatoria");
        	throw new Exception("Agenda e obrigatoria");
        }else{
        	try {
        		entityManager.merge(agenda);
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
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".excluirAgenda() - Erro ao excluir agenda");
				throw new Exception("Erro ao excluir agenda");
			}
        }
    }
    
    @SuppressWarnings("deprecation")
	public List<Agenda> listarAgenda() throws Exception {
    	logger.info("Listando agenda");
        
    	Date dataAtual = new Date();
    	dataAtual.setMonth(dataAtual.getMonth()-1);
    	
        List<Agenda> listaAgenda = new ArrayList<Agenda>();
        String sql = " from Agenda a where a.dataEvento >= :dataAtual order by a.dataEvento desc";
                
    	try {
            listaAgenda = (List<Agenda>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("dataAtual", dataAtual)
        			.getResultList();
			
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarAgenda() - Nenhuma agenda cadastrada");
			throw new Exception("Nenhuma agenda cadastrada");
		}
            	
    	return listaAgenda;
    }

    
    
}
