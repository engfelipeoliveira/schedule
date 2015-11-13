package br.com.system.schedule.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("zenviaCallbackResource")
public class ZenviaCallbackResource {
	
	@Inject
    private Logger logger;
	
	@Inject
    private AgendaService agendaService;
	
	/*
	{
  "callbackMoRequest": {
    "id": "20690090",
    "mobile": "555191951711",
    "shortCode": "40001",
    "account": "zenvia.envio ",
    "body": "Conteudo do SMS respondido",
    "received": "2014-08-26T12:27:08.488-03:00",
    "correlatedMessageSmsId": "hs765939061"}}
	*/
	
	@GET
	@Produces({"application/json"})
	public void getZenviaCallback(@QueryParam("id") String id, 
								  @QueryParam("mobile") String mobile, 
								  @QueryParam("shortCode") String shortCode, 
								  @QueryParam("account") String account, 
								  @QueryParam("body") String body, 
								  @QueryParam("received") String received, 
								  @QueryParam("correlatedMessageSmsId") String correlatedMessageSmsId) throws Exception{
		
		this.logger.log(Level.INFO, "Recebendo retorno SMS getZenviaCallback()");
		this.logger.log(Level.INFO, "args - id " + id);
		this.logger.log(Level.INFO, "args - mobile " + mobile);
		this.logger.log(Level.INFO, "args - shortCode " + shortCode);
		this.logger.log(Level.INFO, "args - account " + account);
		this.logger.log(Level.INFO, "args - body " + body);
		this.logger.log(Level.INFO, "args - received " + received);
		this.logger.log(Level.INFO, "args - correlatedMessageSmsId " + correlatedMessageSmsId);
		
		if(id != null && !"".equals(id)){
			Long idAgenda = Long.parseLong(id);
			agendaService.atualizarRetornoSms(idAgenda, body);
		}
	}
}
