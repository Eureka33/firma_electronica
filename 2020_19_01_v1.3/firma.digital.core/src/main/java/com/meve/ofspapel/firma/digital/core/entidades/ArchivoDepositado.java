package com.meve.ofspapel.firma.digital.core.entidades;

import java.util.Date;

public class ArchivoDepositado {
    
    private Integer id;
    
    private Integer idUsuario;
    private Date fechaHora;
    private String folio;
    private String nombre;
    
    
    public ArchivoDepositado() {
        super();
    }

    public ArchivoDepositado( Integer id) {
        super();
        this.id = id;
    }

    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Date fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
        
}
