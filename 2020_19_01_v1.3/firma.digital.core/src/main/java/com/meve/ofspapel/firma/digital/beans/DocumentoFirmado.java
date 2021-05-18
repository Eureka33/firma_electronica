package com.meve.ofspapel.firma.digital.beans;


public class DocumentoFirmado {

    private Integer id;
    private String fechaHora;
    private String folio;
    private String nombre;
    private String firmante;
    
    private SolicitudFirma solicitud;

    
    public DocumentoFirmado() {
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getFirmante() {
        return firmante;
    }
    
    public void setFirmante( String firmante) {
        this.firmante = firmante;
    }

    public SolicitudFirma getSolicitud() {
        return solicitud;
    }

    public void setSolicitud(SolicitudFirma solicitud) {
        this.solicitud = solicitud;
    }
    
    
}
