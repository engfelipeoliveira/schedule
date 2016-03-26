package br.com.system.schedule.service;

public class SMSJsonResponse {
	
	private SendSmsResponse sendSmsResponse;

	public SendSmsResponse getSendSmsResponse() {
		return sendSmsResponse;
	}

	public void setSendSmsResponse(SendSmsResponse sendSmsResponse) {
		this.sendSmsResponse = sendSmsResponse;
	}

	public SMSJsonResponse(SendSmsResponse sendSmsResponse) {
		super();
		this.sendSmsResponse = sendSmsResponse;
	}
	
	public SMSJsonResponse() {
	}

}
