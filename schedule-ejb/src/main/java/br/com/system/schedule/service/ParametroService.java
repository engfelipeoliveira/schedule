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

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.system.schedule.model.Parametro;

@Stateless
public class ParametroService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    private final String NOME_CLASS = "ParametroService";
    
    public String getParametroByNome(String nome) throws Exception {
    	logger.info("Consultando parametro por nome");
        logger.info("Nome: " + nome);
        
        Parametro parametro = null;
        String valor = null;
        
        	if(nome == null || "".equals(nome)){
        		logger.log(Level.INFO, NOME_CLASS +".getParametroByNome() - Nome e obrigatorio");
        		throw new Exception("Nome e obrigatorio");
        	}else{
                StringBuilder sql = new StringBuilder();
                sql.append("  from Parametro p ");
                sql.append(" where p.nome = :nome ");
                
            	try {
                    parametro = (Parametro)entityManager
                			.createQuery(sql.toString())
                			.setParameter("nome", nome)
                			.getSingleResult();
                    
                    valor = parametro.getValor();
                    logger.info("Valor: " + valor);
					
				} catch (NoResultException e) {
					logger.log(Level.INFO, NOME_CLASS +".getParametroByNome() - Parametro nao encontrado");
					throw new Exception("Parametro nao encontrado");
				}
            	
        	}
			
    	return valor;
    }
}
