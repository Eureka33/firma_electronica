package com.eureka.firma.digital.ws.bean;

import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import java.util.List;

public class RespuestaFirmaMasiva extends RespuestaFirma {

    private List<String> paths;
    private List<ArchivoDepositado> documentos;
	
    
	public RespuestaFirmaMasiva() {
		super();
	}
    
    
    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }  

    public List<ArchivoDepositado> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<ArchivoDepositado> documentos) {
        this.documentos = documentos;
    }
    
}