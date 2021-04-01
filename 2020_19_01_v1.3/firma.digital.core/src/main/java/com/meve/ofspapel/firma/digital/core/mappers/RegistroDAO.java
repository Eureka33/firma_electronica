package com.meve.ofspapel.firma.digital.core.mappers;

import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import java.util.Date;
import org.apache.ibatis.annotations.Param;

public interface RegistroDAO {

    int insertDocumento( @Param( "item") ArchivoDepositado item);
    
    int insertDocumentoBatch( @Param( "item") ArchivoDepositado item);
       
    int asignaDocumento( @Param( "idDocumento") Integer idDocumento, @Param( "idRegistroBatch") Integer idBatch);

    int insertSolicitud( @Param( "item") RegistroSolicitud item);
    
}
