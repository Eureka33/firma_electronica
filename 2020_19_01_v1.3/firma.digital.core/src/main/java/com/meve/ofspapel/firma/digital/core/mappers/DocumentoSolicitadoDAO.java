package com.meve.ofspapel.firma.digital.core.mappers;

import com.eurk.core.beans.consulta.Consulta;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import java.util.List;
import mx.com.neogen.commons.interfaces.Invoker;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface DocumentoSolicitadoDAO {

    List<RegistroSolicitud> listarItems(
                                @Param(   "consulta") Consulta       consulta, 
                                @Param(  "invocador") Invoker       invocador,
                                @Param( "ordenacion") List<String> ordenacion,
                                RowBounds rowBounds
                            );
    
    RegistroSolicitud obtenerItem(
                                @Param( "claveOrganizacion") String claveOrganizacion,
                                @Param(         "invocador") Invoker        invocador,
                                @Param(            "idItem") Integer           idItem
                            );
   
    RegistroSolicitud obtenerItemById(
                                @Param( "idItem") Integer idItem
                            );
    
    RegistroSolicitud obtenerItemByIdDocumento(
                                @Param( "idDocumento") Integer idDocumento
                            );
    
    RegistroSolicitud obtenerItemByFolio(
                                @Param(  "folio") String  folio,
                                @Param( "nombre") String nombre
                            );
    
	int contarItems( @Param( "consulta") Consulta consulta, @Param( "invocador") Invoker invocador);
    
}
