package com.meve.ofspapel.firma.digital.core.entidades;

import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import java.util.Date;

public class RegistroSolicitud extends ArchivoDepositado {
    
    private String emailDestinatario;
    private EnumEstatusSolicitud estatus;
    private Date ultimaActualizacion;
    
    
    public RegistroSolicitud() {
        super();
    }

    public RegistroSolicitud( Integer id) {
        super( id);
    }


    public String getEmailDestinatario() {
        return emailDestinatario;
    }

    public void setEmailDestinatario( String emailDestinatario) {
        this.emailDestinatario = emailDestinatario;
    }

    public EnumEstatusSolicitud getEstatus() {
        return estatus;
    }

    public void setEstatus( EnumEstatusSolicitud estatus) {
        this.estatus = estatus;
    }

    public Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion( Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
    
}
