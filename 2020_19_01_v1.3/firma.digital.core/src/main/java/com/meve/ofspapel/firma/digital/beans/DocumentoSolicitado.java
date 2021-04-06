package com.meve.ofspapel.firma.digital.beans;

import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;


public class DocumentoSolicitado extends DocumentoFirmado {

    private String estatus;
    private String destinatario;
    private String fechaHoraFirma;
    private DocumentoFirmado documentoFirmado;
    
    
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

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getFechaHoraFirma() {
        return fechaHoraFirma;
    }

    public void setFechaHoraFirma(String fechaHoraFirma) {
        this.fechaHoraFirma = fechaHoraFirma;
    }

    public DocumentoFirmado getDocumentoFirmado() {
        return documentoFirmado;
    }

    public void setDocumentoFirmado(DocumentoFirmado documentoFirmado) {
        this.documentoFirmado = documentoFirmado;
    }
    
}
