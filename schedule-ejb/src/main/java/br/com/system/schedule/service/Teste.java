package br.com.system.schedule.service;

import sun.misc.BASE64Encoder;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class Teste {

  public static void main(String[] args) {

	try {
		String userZenvia = "pegoliveira.api";
		String passZenvia = "OVS5WOK94h";
		String ddi = "55";
		String celular = "24993223538";
		String mensagem = "ola testando";
		String id = "_+115";
		String userPassword = userZenvia + ":" + passZenvia;
		String encoding = new BASE64Encoder().encode(userPassword.getBytes());

		Client client = Client.create();

		WebResource webResource = client
		   .resource("https://api-rest.zenvia360.com.br/services/send-sms");

		String input = "{ \"sendSmsRequest\": {\"to\": \""+ddi+celular+"\", \"msg\": \""+mensagem+"\", \"callbackOption\": \"ALL\", \"id\": \""+id+"\" }}";

		ClientResponse response = webResource.type("application/json")
			.header("Authorization", "Basic " + encoding)
		   .post(ClientResponse.class, input);

		/*if (response.getStatus() != 200) {
			throw new RuntimeException("Failed : HTTP error code : "
			     + response.getStatus());
		}*/

		System.out.println("Output from Server .... \n");
		String output = response.getEntity(String.class);
		System.out.println(output);

	  } catch (Exception e) {

		e.printStackTrace();

	  }

	}
}