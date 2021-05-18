package com.meve.ofspapel.firma.digital.beans;

import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;


public class SolicitudFirma {

    private Integer id;
    private String fechaHora;
    private String folio;
    private String solicitante;
    private String emailSolicitante;
    private String estatus;
               
    
    public SolicitudFirma() {
        super();
    }
        
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(String fechaHora) {
        this.fechaHora = fechaHora;
    }
    
    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }
    
    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }
    
    public String getEmailSolicitante() {
        return emailSolicitante;
    }

    public void setEmailSolicitante(String emailSolicitante) {
        this.emailSolicitante = emailSolicitante;
    }
    
    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public void setEstatus(EnumEstatusSolicitud estatus) {
        this.estatus = estatus.toString();
    }
    
}
