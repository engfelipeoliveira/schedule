package br.com.system.schedule.controller;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import jxl.DateCell;
import jxl.Sheet;
import jxl.Workbook;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.com.system.schedule.model.Agenda;
import br.com.system.schedule.model.Destinatario;
import br.com.system.schedule.model.ImportaAgenda;
import br.com.system.schedule.service.ImportaAgendaService;
 
@Model
public class ImportaAgendaController {
     
    @Inject
    private FacesContext facesContext;
    
    @Inject
    private ImportaAgendaService importaAgendaService;
    
    private ImportaAgenda importaAgenda;
	
    @Produces
    @Named
    public ImportaAgenda getImportaAgenda() {
        return importaAgenda;
    }
    
    @PostConstruct
    public void initImportaAgenda() {
    	importaAgenda = new ImportaAgenda();
    }
    
    private void validarData(String dataString, String formato) throws Exception{
    	SimpleDateFormat sdf = new SimpleDateFormat(formato);
    	sdf.setLenient(false);
		Date data = sdf.parse(dataString);
    }
    
    public void inserirImportaAgenda(FileUploadEvent fileUploadEvent) throws Exception {
        try {
        	UploadedFile uploadedFile = fileUploadEvent.getFile();
        	String nomeArquivo = uploadedFile.getFileName();
        	Workbook workbook = Workbook.getWorkbook(uploadedFile.getInputstream());
        	Sheet sheet = workbook.getSheet(0);
        	int linhas = sheet.getRows();
        	int colunas = sheet.getColumns();
        	// Arquivo Excel: Nome, Data(28/11/1981), Hora(23:15), Celular(24993223538)

        	if(colunas < 4){
        		throw new Exception("O arquivo precisa de quatro colunas. Nome, Data, Hora e Celular.");
        	}
        	
        	if(linhas < 2 || linhas > 1000){
        		throw new Exception("O arquivo precisa de 1 a 1000 registros de agenda.");
        	}
        	
        	List<Agenda> listaAgenda = new ArrayList<Agenda>();
        	
        	for(int i = 1; i < linhas; i++){
        		String nome = sheet.getCell(0, i).getContents();
        		DateCell data = null;
        		DateCell hora = null;
        		String celular = sheet.getCell(3, i).getContents();
        		int lin = i + 1;
        				
        		if(nome.length() < 1 || nome.length() > 100){
        			throw new Exception("Nome nulo ou com mais de 100 caracteres na linha "+lin+".");
        		}
        		
        		try {
        			data = (DateCell)sheet.getCell(1, i);
        		} catch (Exception e) {
        			throw new Exception("Data inválida na linha "+lin+".");
        		}
        		
        		try {
        			hora = (DateCell)sheet.getCell(2, i);
        		} catch (Exception e) {
        			throw new Exception("Hora inválida na linha "+lin+".");
        		}
        		
        		if(celular.length() < 11){
        			throw new Exception("Celular com menos de 11 dígitos na linha "+lin+".");
        		}
        		
        		if(celular.length() < 11){
        			throw new Exception("Celular com menos de 11 dígitos na linha "+lin+".");
        		}
        		
        		try {
        			Long.parseLong(celular);	
				} catch (Exception e) {
					throw new Exception("Celular deve conter somente dígitos na linha "+lin+".");
				}
        		
        		SimpleDateFormat dataSdf = new SimpleDateFormat("dd/MMM/yyyy");
        		String dataStr = dataSdf.format(data.getDate());
        		
        		SimpleDateFormat horaSdf = new SimpleDateFormat("HH:mm");
        		String horaStr = horaSdf.format(hora.getDate());
        		
        		String dataEventoStr = dataStr +" "+ horaStr;
        		SimpleDateFormat frm = new SimpleDateFormat("dd/MMM/yyyy HH:mm");
        		Date dataEvento = frm.parse(dataEventoStr);
        		
        		Agenda agenda = new Agenda();
        		agenda.setTipoCadastro("A");
        		agenda.setDataEvento(dataEvento);
        		agenda.setDataInclusao(new Date());
        		Destinatario destinatario = new Destinatario();
        		destinatario.setCelular(Long.parseLong(celular));
        		destinatario.setNome(nome);
        		agenda.setDestinatario(destinatario);
        		listaAgenda.add(agenda);
        	}

        	importaAgenda.setAgenda(listaAgenda);
        	importaAgenda.setNomeArquivo(nomeArquivo);
        	importaAgenda.setQuantidade(linhas);
            importaAgendaService.importarAgenda(importaAgenda);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Agenda importada com sucesso", "Sucesso"));
            initImportaAgenda();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }
    
    public void excluirImportaAgenda(ImportaAgenda importaAgenda) throws Exception {
        try {
        	importaAgendaService.excluirImportaAgenda(importaAgenda);
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Importação excluída com sucesso", "Sucesso"));
            initImportaAgenda();
        } catch (Exception e) {
            String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
        }
    }
    
    private StreamedContent file;
    
    public void download() {        
    	//InputStream stream = getClass().getResourceAsStream("/exemplo.xls");
    	InputStream stream = ImportaAgendaController.class.getClassLoader().getResourceAsStream("exemplo.xls");
    	//InputStream stream = getClass().getClassLoader().getResourceAsStream("/exemplo.xls");
        file = new DefaultStreamedContent(stream, "application/xls", "exemplo.xls");
    }
 
    public StreamedContent getFile() {
        return file;
    }

    
    public String formataData(Date data, String formato){
    	SimpleDateFormat sdf = new SimpleDateFormat(formato);
    	String dataFormat = sdf.format(data);
    	return dataFormat;
    }

    public List<ImportaAgenda> listarImportaAgenda() throws Exception{
    	List<ImportaAgenda> listaImportaAgenda = null;
    	try {
    		listaImportaAgenda = importaAgendaService.listarImportaAgenda();	
		} catch (Exception e) {
			String errorMessage = getRootErrorMessage(e);
            FacesMessage m = new FacesMessage(FacesMessage.SEVERITY_ERROR, errorMessage, "Erro");
            facesContext.addMessage(null, m);
		}
    	
    	return listaImportaAgenda;
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