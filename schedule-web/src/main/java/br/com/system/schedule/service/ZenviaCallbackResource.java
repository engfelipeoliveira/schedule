package br.com.system.schedule.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.POST;
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
	
	@POST
	@Produces({"application/json"})
	public void getZenviaCallback(@QueryParam("idMT") String idMT, 
								  @QueryParam("msg") String msg) throws Exception{
		
		this.logger.log(Level.INFO, "Recebendo retorno SMS getZenviaCallback()");
		this.logger.log(Level.INFO, "args - idMT " + idMT);
		this.logger.log(Level.INFO, "args - msg " + msg);
		
		if(idMT != null && !"".equals(idMT)){
			agendaService.atualizarRetornoSms(idMT, msg);
		}
	}
}
