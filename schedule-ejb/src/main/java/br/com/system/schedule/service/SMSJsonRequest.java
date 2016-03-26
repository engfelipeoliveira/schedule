package br.com.system.schedule.service;

public class SMSJsonRequest {
	
	private SendSmsRequest sendSmsRequest;

	public SendSmsRequest getSendSmsRequest() {
		return sendSmsRequest;
	}

	public void setSendSmsRequest(SendSmsRequest sendSmsRequest) {
		this.sendSmsRequest = sendSmsRequest;
	}

	public SMSJsonRequest(SendSmsRequest sendSmsRequest) {
		super();
		this.sendSmsRequest = sendSmsRequest;
	}
	
	public SMSJsonRequest() {
	}

}
