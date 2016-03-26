package br.com.system.schedule.service;

public class SendSmsResponse {
	
/*
 * body:{
  "sendSmsResponse" : {
    "statusCode" : "00",
    "statusDescription" : "Ok",
    "detailCode" : "000",
    "detailDescription" : "Message Sent"
  }
}	

statusCode
00 - Ok
01 - Scheduled
02 - Sent
03 - Delivered
04 - Not Received
05 - Blocked - No Coverage
06 - Blocked - Black listed
07 - Blocked - Invalid Number
08 - Blocked - Content not allowed
08 - Blocked - Message Expired
09 - Blocked
10 - Error
 */
	
	private String statusCode;
	private String statusDescription;
	private String detailCode;
	private String detailDescription;
	
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getStatusDescription() {
		return statusDescription;
	}
	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}
	public String getDetailCode() {
		return detailCode;
	}
	public void setDetailCode(String detailCode) {
		this.detailCode = detailCode;
	}
	public String getDetailDescription() {
		return detailDescription;
	}
	public void setDetailDescription(String detailDescription) {
		this.detailDescription = detailDescription;
	}
	public SendSmsResponse(String statusCode, String statusDescription,
			String detailCode, String detailDescription) {
		super();
		this.statusCode = statusCode;
		this.statusDescription = statusDescription;
		this.detailCode = detailCode;
		this.detailDescription = detailDescription;
	}
	public SendSmsResponse(){
	}
	
	
}
