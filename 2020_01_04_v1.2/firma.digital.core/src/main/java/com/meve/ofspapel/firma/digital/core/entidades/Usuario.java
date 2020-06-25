package com.meve.ofspapel.firma.digital.core.entidades;

import mx.com.neogen.commons.interfaces.Invoker;

public class Usuario implements Invoker {
    
    private Integer id;
    private String clave;
    private String nombre;
    
    
    public Usuario( Integer id) {
        super();    
        this.id = id;
    }
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
      
}
