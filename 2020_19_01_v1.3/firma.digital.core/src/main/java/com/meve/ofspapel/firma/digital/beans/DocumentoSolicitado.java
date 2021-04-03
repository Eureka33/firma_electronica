package com.meve.ofspapel.firma.digital.beans;

import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;


public class DocumentoSolicitado extends DocumentoFirmado {

    private String estatus;
    private DocumentoFirmado atencion;
 
    
    public DocumentoSolicitado() {
        super();
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

    public DocumentoFirmado getAtencion() {
        return atencion;
    }

    public void setAtencion(DocumentoFirmado atencion) {
        this.atencion = atencion;
    }

}
