package br.com.system.schedule.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@FacesConverter(value="telefoneConverter")
public class TelefoneConverter implements Converter{
	
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		
		String telefoneFrmt = arg2;
		Long telefone = null;
		
		if(telefoneFrmt != null && !"".equals(telefoneFrmt)){
			telefoneFrmt = telefoneFrmt.replace("(", "");
			telefoneFrmt = telefoneFrmt.replace(")", "");
			telefoneFrmt = telefoneFrmt.replace("-", "");
			telefoneFrmt = telefoneFrmt.replace(".", "");
			
			telefone = Long.parseLong(telefoneFrmt);
		}
		
		
		return telefone;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		String telefone = arg2.toString();
		String telefoneFrm = null;
		
		if(telefone != null && !"".equals(telefone)){
			int lenTel = telefone.length();
			
			if(lenTel < 11){
				telefoneFrm = "(" + telefone.substring(0, 2) + ")" + telefone.substring(2, 6) + "-"  + telefone.substring(6, 10);				
			}else{
				telefoneFrm = "(" + telefone.substring(0, 2) + ")" + telefone.substring(2, 3) + "." + telefone.substring(3, 7) + "-"  + telefone.substring(7, 11);
			}
		}
		
		return telefoneFrm;
	}

}
