package com.eureka.firma.digital.ws.bean;

import java.util.List;

public class RespuestaFirmaMasiva extends RespuestaFirma {

    private List<String> paths;
	
	
	public RespuestaFirmaMasiva() {
		super();
	}
    
    
    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }  
	
}