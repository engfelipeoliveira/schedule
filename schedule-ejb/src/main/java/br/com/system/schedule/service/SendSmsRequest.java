package br.com.system.schedule.service;

public class SendSmsRequest {

	/**
	 * {
    "sendSmsRequest": {
        "from": "Remetente",
        "to": "555199999999",
        "schedule": "2014-08-22T14:55:00",
        "msg": "Mensagem de teste",
        "callbackOption": "NONE",
        "id": "002",
        "aggregateId": "1111"
    }
}
	 */
	
	private String to;
	private String msg;
	private String callbackOption;
	private String id;
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getCallbackOption() {
		return callbackOption;
	}
	public void setCallbackOption(String callbackOption) {
		this.callbackOption = callbackOption;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public SendSmsRequest(){
	}
	
	public SendSmsRequest(String to, String msg, String callbackOption,
			String id) {
		super();
		this.to = to;
		this.msg = msg;
		this.callbackOption = callbackOption;
		this.id = id;
	}
	
	
	
}
