package br.com.system.schedule.service;

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

import sun.misc.BASE64Encoder;
import br.com.system.schedule.model.Agenda;
import br.com.system.schedule.model.Cronograma;
import br.com.system.schedule.model.Usuario;

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
	
	public void enviarSMS(Agenda agenda, Cronograma cronograma) throws Exception{
		logger.log(Level.INFO, "Enviando SMS");
		logger.log(Level.INFO, "Celular " + agenda.getCelular());
		
		try {
			String celular = agenda.getCelular().toString();
			String id = agenda.getId().toString();
			String mensagem = cronograma.getTexto();
			
			String destinatario = agenda.getNome();
			
			SimpleDateFormat fmtHora = new SimpleDateFormat("HH:mm");
			String hora = fmtHora.format(agenda.getDataEvento());
			
			SimpleDateFormat fmtData = new SimpleDateFormat("dd/MM/yyyy");
			String data = fmtData.format(agenda.getDataEvento());
			
			String remetente = agenda.getUsuario().getNome();
			
			mensagem = mensagem.replaceAll("#REMETENTE", remetente);
			mensagem = mensagem.replaceAll("#DESTINATARIO", destinatario);
			mensagem = mensagem.replaceAll("#DATA", data);
			mensagem = mensagem.replaceAll("#HORA", hora);
			
			System.out.println(mensagem);
			
			
			
			String userPassword = USER_ZENVIA + ":" + PASS_ZENVIA;
			String encoding = new BASE64Encoder().encode(userPassword.getBytes());
			
			Client client = ClientBuilder.newClient();
			Entity<String> payload = Entity.json("{ \"sendSmsRequest\": {\"to\": \""+DDI+celular+"\", \"msg\": \""+mensagem+"\", \"callbackOption\": \"ALL\", \"id\": \""+id+"\" }}");
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
	
	
	public List<Agenda> listarAgenda(Date dataEvento, Usuario usuario) throws Exception {
    	logger.info("Listando agenda");
        
        List<Agenda> listaAgenda = new ArrayList<Agenda>();
        StringBuilder sql = new StringBuilder();
        sql.append("  from Agenda a ");
        sql.append(" where a.usuario = :usuario ");
        sql.append("   and a.dataEvento = :dataEvento ");
        sql.append("   and (a.situacao = 'A' ");
        sql.append("    or (a.situacao = 'E' ");
        sql.append("   and a.retornoSms is null)) ");
                
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
