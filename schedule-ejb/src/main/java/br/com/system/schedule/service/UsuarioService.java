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

import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import br.com.system.schedule.model.Usuario;
import br.com.system.schedule.util.Encriptor;

@Stateless
public class UsuarioService {

    @Inject
    private Logger logger;

    @Inject
    private EntityManager entityManager;

    private final String NOME_CLASS = "UsuarioService";
    
    private static final Locale LOCALE_PTBR = new Locale("pt", "BR");
    
    public Usuario login(String email, String senha) throws Exception {
    	logger.info("Consultando usuario por email e senha");
        logger.info("Email: " + email);
        
        Usuario usuario = null;
        
        	if(email == null || "".equals(email) || senha == null || "".equals(senha)){
        		logger.log(Level.INFO, NOME_CLASS +".login() - Email e senha sao obrigatorios");
        		throw new Exception("Email e Senha sao obrigatorios");
        	}else{
                StringBuilder sql = new StringBuilder();
                sql.append("  from Usuario u ");
                sql.append(" where u.email = :email ");
                
                
            	try {
                    usuario = (Usuario)entityManager
                			.createQuery(sql.toString())
                			.setParameter("email", email)
                			.getSingleResult();
					
				} catch (NoResultException e) {
					logger.log(Level.INFO, NOME_CLASS +".login() - Usuario nao encontrado");
					throw new Exception("Usuario nao encontrado");
				}
            	
            	Encriptor encriptor = new Encriptor();
                String senhaCript = encriptor.criptografar(senha);
            	
            	if(!senhaCript.equals(usuario.getSenha().toUpperCase(LOCALE_PTBR))){
            		logger.log(Level.INFO, NOME_CLASS +".login() - Senha invalida");
            		throw new Exception("Senha invalida");
            	}
            	
            	if(usuario.getSituacao().equals("I")){
            		logger.log(Level.INFO, NOME_CLASS +".login() - Usuario inativo");
            		throw new Exception("Usuario inativo");
            	}
        	}
			
    	return usuario;
    }
}
