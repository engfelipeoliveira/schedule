package br.com.system.schedule.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import sun.misc.BASE64Encoder;
import br.com.system.schedule.model.Agenda;

@Stateless
public class SMSService {
	
	@Inject
    private Logger logger;
	
	@Inject
    private EntityManager entityManager;

    private final String NOME_CLASS = "SMSService";
	
	private final static String USER_ZENVIA = "pegoliveira.api";
	
	private final static String PASS_ZENVIA = "SX7qUGU32R";
	
	private final static String URL_ZENVIA = "https://api-rest.zenvia360.com.br/services/send-sms";
	
	private final static String DDI = "55";
	
	private final static String PREFIXO = "SMS_";
	
	//@Schedule(hour = "*", minute = "*/1")
	public void jobEnvioSMS() throws Exception{
		logger.log(Level.INFO, "JOB Envia SMS");
		List<Agenda> listaAgenda = listarAgenda();
		
		for(Agenda agenda : listarAgenda()){
			enviarSMS(agenda);
		}
		
	}
	
	public void enviarSMS(Agenda agenda) throws Exception{
		logger.log(Level.INFO, "Enviando SMS");
		logger.log(Level.INFO, "Celular " + agenda.getCelular());
		
		try {
			String celular = agenda.getCelular().toString();
			String id = agenda.getId().toString();
			String mensagem = "mensagem de teste";
			
			String userPassword = USER_ZENVIA + ":" + PASS_ZENVIA;
			String encoding = new BASE64Encoder().encode(userPassword.getBytes());
			
			Client client = ClientBuilder.newClient();
			Entity<String> payload = Entity.json("{ \"sendSmsRequest\": {\"to\": \""+DDI+celular+"\", \"msg\": \""+mensagem+"\", \"callbackOption\": \"ALL\", \"id\": \""+PREFIXO+id+"\" }}");
			Response response = client.target(URL_ZENVIA)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .header("Authorization", "Basic " + encoding)
			  .header("Accept", "application/json")
			  .post(payload);
			
			logger.log(Level.INFO, "status: " + response.getStatus());
			if(response.getStatus() != 200){
				throw new Exception("Erro ao enviar SMS");
			}
			
			agenda.setSituacao("E");
			entityManager.merge(agenda);
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, NOME_CLASS +".enviarSMS() - Erro ao enviar SMS");
			agenda.setSituacao("F");
			entityManager.merge(agenda);
			throw new Exception("Erro ao enviar SMS");
		}
	}
	
	
    @SuppressWarnings("deprecation")
	public List<Agenda> listarAgenda() throws Exception {
    	logger.info("Listando agenda");
        
    	String situacao = "A";
    	Date dataAtual = new Date();
    	dataAtual.setMonth(dataAtual.getMonth()-1);
    	
        List<Agenda> listaAgenda = new ArrayList<Agenda>();
        StringBuilder sql = new StringBuilder();
        sql.append("  from Usuario u ");
        sql.append("  inner join u.agenda a ");
        sql.append(" where a.situacao = :situacao ");
                
    	try {
            listaAgenda = (List<Agenda>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("situacao", situacao)
        			.getResultList();
			
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarAgenda() - Nenhuma agenda cadastrada");
			throw new Exception("Nenhuma agenda cadastrada");
		}
            	
    	return listaAgenda;
    }

}
