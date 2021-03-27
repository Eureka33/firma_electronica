package com.eureka.firma.digital.ws.bean;


public class Solicitud {
    
    private Integer id;
    private String pathArchivo;
    
    
    public Solicitud() {
        super();
    }
    
    
    public Integer getId() {
        return id;
    }
    
    public void setId( Integer value) {
        this.id = value;
    }
    
    public String getPathArchivo() {
        return pathArchivo;
    }
    
    public void setPathArchivo( String value) {
        this.pathArchivo = value;
    }
    
}
