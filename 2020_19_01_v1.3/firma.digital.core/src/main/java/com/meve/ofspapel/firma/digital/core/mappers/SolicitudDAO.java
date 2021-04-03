package com.meve.ofspapel.firma.digital.core.mappers;

import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import java.util.Date;
import org.apache.ibatis.annotations.Param;

public interface SolicitudDAO {

    Integer obtenerIdSolicitud( @Param( "folio") String folio, @Param( "nombre") String nombre);
     
    int updateSolicitud(
        @Param( "id") Integer id, @Param( "estatus") EnumEstatusSolicitud estatus, @Param( "fechaHora") Date fechaHora
    );
    
    Integer obtenerVisitas( @Param( "idItem") Integer idItem);
    
    int insertVisitas( @Param( "idItem") Integer idItem, @Param( "fechaHora") Date fechaHora, @Param( "visitas") Integer visitas);
    
    int updateVisitas( @Param( "idItem") Integer idItem, @Param( "fechaHora") Date fechaHora, @Param( "visitas") Integer visitas);
    
    int insertAtencion( @Param( "idDocumento") Integer idDocumento, @Param( "idSolicitud") Integer idSolicitud);
}
