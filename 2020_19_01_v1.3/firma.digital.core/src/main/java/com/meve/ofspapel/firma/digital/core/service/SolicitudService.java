package com.meve.ofspapel.firma.digital.core.service;

import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.enums.EnumAccionSolicitud;
import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import com.meve.ofspapel.firma.digital.core.mappers.SolicitudDAO;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolicitudService {

    @Autowired private SolicitudDAO dao;
    @Autowired private IConfiguracionService config;

    
    public Integer obtenerIdSolicitud( String folio, String nombre) {
        return dao.obtenerIdSolicitud( folio, nombre);
    }
    
    public EnumEstatusSolicitud actualizaSolicitud( Integer idItem, EnumAccionSolicitud accion) {
        RegistroSolicitud entidad = dao.obtenerItem( idItem);
        EnumEstatusSolicitud estatus = entidad.getEstatus();
        
        switch ( accion) {
            case CORREO_ENVIADO:
                estatus = EnumEstatusSolicitud.NOTIFICADA;
                break;
                
            case LINK_VISITADO:
                Integer maximo  = Integer.valueOf( config.getPropiedad( "link.maximo.visitas"));
                Integer visitas = registrarVisita( idItem);
                estatus = (visitas <= maximo)? EnumEstatusSolicitud.VISITADA : EnumEstatusSolicitud.CANCELADA;
                break;
                
            case DOCUMENTO_FIRMADO: 
                estatus = EnumEstatusSolicitud.ATENDIDA;
                break;
                
            default:
                throw new IllegalArgumentException( "Indicador de estatus no conocido: " + accion);
        }
    
        dao.updateSolicitud( idItem, estatus, new Date());
        
        return estatus;
    }
    
    public void registraAtencionSolicitud( Integer idDocumentoFirmado, Integer idSolicitud) {
        dao.insertAtencion( idDocumentoFirmado, idSolicitud);
    }    
    
    private int registrarVisita( Integer idItem) {
        Integer value = dao.obtenerVisitas( idItem);
        Date fechaHora = new Date();
        
        if ( value == null) {
            value = 1; 
            dao.insertVisitas( idItem, fechaHora, value);
        } else {
            value = value + 1;
            dao.updateVisitas( idItem, fechaHora, value);
        }
      
        return value;      
    }
}
