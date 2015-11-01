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

import br.com.system.schedule.model.ImportaAgenda;
import br.com.system.schedule.model.Usuario;

@Stateless
public class ImportaAgendaService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    private final String NOME_CLASS = "ImportaAgendaService";
    
    public void importarAgenda(ImportaAgenda importaAgenda) throws Exception {
    	logger.info("Importando Agenda");
        
        if(importaAgenda == null){
        	logger.log(Level.INFO, NOME_CLASS +".inserirAgenda() - ImportaAgenda e obrigatoria");
        	throw new Exception("ImportaAgenda e obrigatoria");
        }else{
        	try {
        		importaAgenda.setDataInclusao(new Date());
                entityManager.persist(importaAgenda);
                entityManager.flush();
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".importarAgenda() - Erro ao importar agenda");
				throw new Exception("Erro ao importar agenda");
			}
        }
    }
    
    
    public void excluirImportaAgenda(ImportaAgenda importaAgenda) throws Exception {
    	logger.info("Excluindo agendas importadas");
        
        if(importaAgenda == null){
        	logger.log(Level.INFO, NOME_CLASS +".excluirImportaAgenda() - Agenda e obrigatoria");
        	throw new Exception("Agenda e obrigatoria");
        }else{
        	try {
                entityManager.remove(entityManager.getReference(ImportaAgenda.class, importaAgenda.getId()));
                entityManager.flush();
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".excluirImportaAgenda() - Erro ao excluir agenda");
				throw new Exception("Erro ao excluir agenda");
			}
        }
    }

    
	public List<ImportaAgenda> listarImportaAgenda(Usuario usuario) throws Exception {
    	logger.info("Listando agenda importada");
        
        List<ImportaAgenda> listaImportaAgenda = new ArrayList<ImportaAgenda>();
        String sql = " from ImportaAgenda a where a.usuario = :usuario order by a.id desc";
                
    	try {
            listaImportaAgenda = (List<ImportaAgenda>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("usuario", usuario)
        			.getResultList();
            entityManager.flush();
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarImportaAgenda() - Nenhuma importação de agenda cadastrada");
			throw new Exception("Nenhuma importação de agenda cadastrada");
		}
            	
    	return listaImportaAgenda;
    }

    
    
}
