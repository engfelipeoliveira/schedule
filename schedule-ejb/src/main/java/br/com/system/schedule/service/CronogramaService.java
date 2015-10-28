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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import br.com.system.schedule.model.Cronograma;

@Stateless
public class CronogramaService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    private final String NOME_CLASS = "CronogramaService";
    
    public void inserirCronograma(Cronograma cronograma) throws Exception {
    	logger.info("Inserindo Cronograma");
        
        if(cronograma == null){
        	logger.log(Level.INFO, NOME_CLASS +".inserirCronograma() - Cronograma e obrigatoria");
        	throw new Exception("Cronograma e obrigatoria");
        }else{
        	try {
        		entityManager.merge(cronograma);
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".inserirCronograma() - Erro ao inserir cronograma");
				throw new Exception("Erro ao inserir cronograma");
			}
        }
    }
    
    public void excluirCronograma(Cronograma cronograma) throws Exception {
    	logger.info("Excluindo Cronograma");
        
        if(cronograma == null){
        	logger.log(Level.INFO, NOME_CLASS +".excluirCronograma() - Cronograma e obrigatoria");
        	throw new Exception("Cronograma e obrigatorio");
        }else{
        	try {
                entityManager.remove(entityManager.getReference(Cronograma.class, cronograma.getId()));
			} catch (PersistenceException e) {
				logger.log(Level.INFO, NOME_CLASS +".excluirCronograma() - Erro ao excluir cronograma");
				throw new Exception("Erro ao excluir cronograma");
			}
        }
    }
    
	public List<Cronograma> listarCronograma() throws Exception {
    	logger.info("Listando cronograma");
        
        List<Cronograma> listaCronograma = new ArrayList<Cronograma>();
        String sql = " from Cronograma c order by c.id desc";
                
    	try {
            listaCronograma = (List<Cronograma>)entityManager
        			.createQuery(sql.toString())
        			.getResultList();
			
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarCronograma() - Nenhum cronograma cadastrado");
			throw new Exception("Nenhum cronograma cadastrado");
		}
            	
    	return listaCronograma;
    }

    
    
}
