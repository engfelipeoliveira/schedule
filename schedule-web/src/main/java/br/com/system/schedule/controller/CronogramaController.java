package br.com.system.schedule.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.system.schedule.model.Cronograma;
import br.com.system.schedule.model.Usuario;
import br.com.system.schedule.service.CronogramaService;

@Model
public class CronogramaController {

    @Inject
    private FacesContext facesContext;

    @Inject
    private CronogramaService cronogramaService;
    
    private Cronograma cronograma;
    
    private Usuario usuarioLogado;

    @Produces
    @Named
    public Cronograma getCronograma() {
        return cronograma;
    }
    
    @PostConstruct
    public void initCronograma() {
    	cronograma = new Cronograma();
    	usuarioLogado = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuarioLogado");
    	cronograma.setUsuario(usuarioLogado);
    	cronograma.setDataInclusao(new Date());
    }

    public String formataData(Date data, String formato){
    	SimpleDateFormat sdf = new SimpleDateFormat(formato);
    	String dataFormat = sdf.format(data);
    	return dataFormat;
    }
    
    public void inserirCronograma() throws Exception {
        try {
        	cronogramaService.inserirCronograma(cronograma);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cronograma salvo com sucesso", "Sucesso"));
            initCronograma();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }

    public void excluirCronograma(Cronograma cronograma) throws Exception {
        try {
            cronogramaService.excluirCronograma(cronograma);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Cronograma excluido com sucesso", "Sucesso"));
            initCronograma();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }
    

    public void selecionarCronograma(Cronograma cronograma) throws Exception {
    	this.cronograma = cronograma;
    }

    
    public List<Cronograma> listarCronograma() throws Exception{
    	List<Cronograma> listaCronograma = null;
    	try {
    		listaCronograma = cronogramaService.listarCronograma(usuarioLogado);	
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
		}
    	
    	return listaCronograma;
    }

    private String getRootErrorMessage(Exception e) {
        // Default to general error message that registration failed.
        String errorMessage = "Registration failed. See server log for more information";
        if (e == null) {
            // This shouldn't happen, but return the default messages
            return errorMessage;
        }

        // Start with the exception and recurse to find the root cause
        Throwable t = e;
        while (t != null) {
            // Get the message from the Throwable class instance
            errorMessage = t.getLocalizedMessage();
            t = t.getCause();
        }
        // This is the root cause message
        return errorMessage;
    }

}
