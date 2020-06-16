package com.meve.ofspapel.firma.digital.core.mappers;

import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface RegistroDAO {

    int insertDocumento( @Param( "item") ArchivoDepositado item);
    
    int insertDocumentoBatch( @Param( "item") ArchivoDepositado item);
       
    int asignaDocumento( @Param( "idDocumento") Integer idDocumento, @Param( "idRegistroBatch") Integer idBatch);
    
    List<ArchivoDepositado> listItems( @Param( "usuario") Usuario usuario);
    
}
