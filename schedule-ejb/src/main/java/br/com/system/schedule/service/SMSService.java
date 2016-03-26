package br.com.system.schedule.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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

import org.codehaus.jackson.map.ObjectMapper;
import org.omg.CORBA.Environment;

import com.google.gson.Gson;

import sun.misc.BASE64Encoder;
import br.com.system.schedule.model.Agenda;
import br.com.system.schedule.model.Cronograma;
import br.com.system.schedule.model.Usuario;
import br.com.system.schedule.util.Encriptor;

@Stateless
public class SMSService {
	
	@Inject
    private Logger logger;
	
	@Inject
    private EntityManager entityManager;

	@Inject
    private ParametroService parametroService;
	
    private final String NOME_CLASS = "SMSService";
	
	@Schedule(hour = "*", minute = "*/1")
	public void jobEnvioSMS() throws Exception{
		logger.log(Level.INFO, "JOB Envia SMS");
		
		Date dataAtual = new Date();

		List<Cronograma> listaCronograma = listarCronograma();
		for(Cronograma cronograma : listaCronograma){
			Long horaAntesEvento = cronograma.getHoraAntesEvento();
			
		    Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
		    dataDeHoje.setSeconds(0);
		    dataDeHoje.setNanos(0);
		    dataDeHoje.setHours(dataDeHoje.getHours() + Integer.parseInt(horaAntesEvento.toString()));
		    Date dataEvento = new Date(dataDeHoje.getTime());
			
			List<Agenda> listaAgenda = listarAgenda(dataEvento, cronograma.getUsuario());
			for(Agenda agenda : listaAgenda){
				enviarSMS(agenda, cronograma);
			}
		}
		
	}
	
	
	public static void main(String[] args) {
		String userZenvia = "pegoliveira.api";
		String passZenvia = "OVS5WOK94h";
		String urlZenvia = "https://api-rest.zenvia360.com.br/services/get-sms-status/F38C0E31686CED4EF0CBC3FE5672DF22";
		String userPassword = userZenvia + ":" + passZenvia;
		String encoding = new BASE64Encoder().encode(userPassword.getBytes());
		
		Client client = ClientBuilder.newClient();
		Response response = client.target(urlZenvia)
				.request(MediaType.APPLICATION_JSON_TYPE)
				  .header("Authorization", "Basic " + encoding)
				  .header("Accept", "application/json")
		  .get();
		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		System.out.println("body:" + response.readEntity(String.class));
		
		
	}		
	
	
	/*public static void main(String[] args) {
		String userZenvia = "pegoliveira.api";
		String passZenvia = "OVS5WOK94h";
		String urlZenvia = "https://api-rest.zenvia360.com.br/services/send-sms";
		String ddi = "55";
		String celular = "24993223538";
		String mensagem = "agendado 20:45";
		
		String id = "za11";
		String userPassword = userZenvia + ":" + passZenvia;
		String encoding = new BASE64Encoder().encode(userPassword.getBytes());
		System.out.println(encoding);
		
		SMSJsonRequest j = new SMSJsonRequest();
		SendSmsRequest s = new SendSmsRequest();
		s.setCallbackOption("ALL");
		s.setId("zaaa1");
		s.setMsg("Mensagem cel desligado");
		s.setTo("5524993223538");
		j.setSendSmsRequest(s);
		
		Gson gson = new Gson();
		String json = gson.toJson(j); 
		System.out.println(json);
		
		Client client = ClientBuilder.newClient();
		Entity<String> payload = Entity.json(json);
		
		Response response = client.target(urlZenvia)
		  .request(MediaType.APPLICATION_JSON_TYPE)
		  .header("Authorization", "Basic " + encoding)
		  .header("Accept", "application/json")
		  .post(payload);
		
		System.out.println("status: " + response.getStatus());
		System.out.println("headers: " + response.getHeaders());
		//System.out.println("body:" + response.readEntity(String.class));
		String retorno = response.readEntity(String.class);
		System.out.println(retorno);
		SMSJsonResponse resp = gson.fromJson(retorno, SMSJsonResponse.class);
		System.out.println("desc " + resp.getSendSmsResponse().getStatusDescription());
		
	}*/
	
	
	public void enviarSMS(Agenda agenda, Cronograma cronograma) throws Exception{
		logger.log(Level.INFO, "Enviando SMS");
		logger.log(Level.INFO, "Celular " + agenda.getCelular());
		
		String userZenvia = parametroService.getParametroByNome("userZenvia"); //pegoliveira.api";
		String passZenvia = parametroService.getParametroByNome("passZenvia"); //"OVS5WOK94h";
		String urlZenvia = parametroService.getParametroByNome("urlZenvia"); //"https://api-rest.zenvia360.com.br/services/send-sms";
		String ddi = parametroService.getParametroByNome("ddi"); //"55";
		
		String detalheSituacao = null;

		try {
			String celular = agenda.getCelular().toString();
			String mensagem = cronograma.getTexto();
			
			String destinatario = agenda.getNome();
			
			SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");
			String hora = fmtHora.format(agenda.getDataEvento());
			
			SimpleDateFormat fmtData = new SimpleDateFormat("dd/MM");
			String data = fmtData.format(agenda.getDataEvento());
			
			String remetente = agenda.getUsuario().getNome();
			
			mensagem = mensagem.replaceAll("#REMETENTE", remetente);
			mensagem = mensagem.replaceAll("#DESTINATARIO", destinatario);
			mensagem = mensagem.replaceAll("#DATA", data);
			mensagem = mensagem.replaceAll("#HORA", hora);
			
			logger.log(Level.INFO, mensagem);

			Encriptor encriptor = new Encriptor();
    		String idMsgZenvia = encriptor.criptografar(new Date().toString());
    		agenda.setIdZMsgZenvia(idMsgZenvia);
			agenda.setSituacao("E");
			agenda.setDetalheSituacao("Message Sent");
    		entityManager.merge(agenda);
			entityManager.flush();
			
			String id = agenda.getIdZMsgZenvia();
			String userPassword = userZenvia + ":" + passZenvia;
			String encoding = new BASE64Encoder().encode(userPassword.getBytes());
			
			SMSJsonRequest sMSJsonRequest = new SMSJsonRequest();
			SendSmsRequest sendSmsRequest = new SendSmsRequest();
			sendSmsRequest.setCallbackOption("ALL");
			sendSmsRequest.setId(id);
			sendSmsRequest.setMsg(mensagem);
			sendSmsRequest.setTo(ddi + celular);
			sMSJsonRequest.setSendSmsRequest(sendSmsRequest);
			
			Gson gson = new Gson();
			String jsonReq = gson.toJson(sMSJsonRequest); 
			
			Client client = ClientBuilder.newClient();
			Entity<String> payload = Entity.json(jsonReq);
			Response response = client.target(urlZenvia)
			  .request(MediaType.APPLICATION_JSON_TYPE)
			  .header("Authorization", "Basic " + encoding)
			  .header("Accept", "application/json")
			  .post(payload);
			
			String jsonRes = response.readEntity(String.class);
			SMSJsonResponse smsJsonResponse = gson.fromJson(jsonRes, SMSJsonResponse.class);
			String statusCode = smsJsonResponse.getSendSmsResponse().getStatusCode();
			detalheSituacao = smsJsonResponse.getSendSmsResponse().getDetailDescription();
			
			if(!"00".equalsIgnoreCase(statusCode)){
				throw new Exception("Erro ao enviar SMS");
			}
			
			if(response.getStatus() != 200){
				throw new Exception("Erro ao enviar SMS");
			}
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, NOME_CLASS +".enviarSMS() - Erro ao enviar SMS");
			agenda.setSituacao("F");
			agenda.setDetalheSituacao(detalheSituacao);
			entityManager.merge(agenda);
			entityManager.flush();
			throw new Exception("Erro ao enviar SMS");
		}
	}
	
	
	public List<Agenda> listarAgenda(Date dataEvento, Usuario usuario) throws Exception {
    	logger.info("Listando agenda");
        
        List<Agenda> listaAgenda = new ArrayList<Agenda>();
        StringBuilder sql = new StringBuilder();
        sql.append("  from Agenda a ");
        sql.append(" where a.usuario = :usuario ");
        sql.append("   and a.dataEvento = :dataEvento ");
        sql.append("   and a.situacao <> 'R' ");
                
    	try {
            listaAgenda = (List<Agenda>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("dataEvento", dataEvento)
        			.setParameter("usuario", usuario)
        			.getResultList();
			
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarAgenda() - Nenhuma agenda cadastrada");
			throw new Exception("Nenhuma agenda cadastrada");
		}
            	
    	return listaAgenda;
    }

    @SuppressWarnings("deprecation")
	public List<Cronograma> listarCronograma() throws Exception {
    	logger.info("Listando cronograma");
        
    	String situacao = "A";
    	
        List<Cronograma> listaCronograma = new ArrayList<Cronograma>();
        StringBuilder sql = new StringBuilder();
        sql.append("  from Cronograma c ");
        sql.append(" where c.usuario.situacao = :situacao ");
                
    	try {
            listaCronograma = (List<Cronograma>)entityManager
        			.createQuery(sql.toString())
        			.setParameter("situacao", situacao)
        			.getResultList();
			
		} catch (NoResultException e) {
			logger.log(Level.INFO, NOME_CLASS +".listarCronograma() - Nenhuma Cronograma cadastrada");
			throw new Exception("Nenhuma Cronograma cadastrada");
		}
            	
    	return listaCronograma;
    }

}
