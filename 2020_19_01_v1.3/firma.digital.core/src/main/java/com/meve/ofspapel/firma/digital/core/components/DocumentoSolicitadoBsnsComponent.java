package com.meve.ofspapel.firma.digital.core.components;

import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.consulta.Ordenacion;
import com.meve.ofspapel.firma.digital.beans.ConsultaBase;
import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import com.meve.ofspapel.firma.digital.beans.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.mappers.DocumentoSolicitadoDAO;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.interfaces.Invoker;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentoSolicitadoBsnsComponent extends ConsultaBase<RegistroSolicitud, Integer> {
    
    @Autowired private DocumentoFirmadoBsnsComponent docBsnsComponent;
    @Autowired private DocumentoSolicitadoDAO data;
    
     
    public DocumentoFirmado obtenerItem( String claveOrganizacion, Invoker invocador, String idItemStr, Propiedades propiedades) {
        DocumentoFirmado item;
        
        if ( idItemStr != null) {
            item = entidadToItem( obtenerItem(claveOrganizacion, invocador, Integer.valueOf( idItemStr)), claveOrganizacion, invocador);
        
        } else {
            // obtiene solicitud relacionada con documento firmado (folio, nombre)
            item = docBsnsComponent.obtenerItem( claveOrganizacion, invocador, null, propiedades);
            RegistroSolicitud solicitud;
            if( item == null) {
                // no hay documento firmado con el folio
                solicitud = obtenerItemByFolio( propiedades);
                item = entidadToItem( solicitud, claveOrganizacion, invocador);
                
            } else {
                // si hay documento firmado
                solicitud =  obtenerItemByIdDocumento( item.getId());
            }
            
            if ( solicitud != null) {
                item.setSolicitud( entidadToItem( solicitud));
            }
        }
        
        return item; 
    }
    
    public List<DocumentoFirmado> listarItems( String claveOrganizacion, Invoker invocador, Consulta consulta) {
        final List<RegistroSolicitud> entidades = consultar( claveOrganizacion, invocador, consulta);
        final List<DocumentoFirmado> items = new ArrayList<>();
        
        for( RegistroSolicitud entidad : entidades) {
            items.add( entidadToItem( entidad, claveOrganizacion, invocador));
        }
        
        return items;
    }
    
    @Override
    protected int contarItems(Consulta consulta, Invoker invocador) {
       	return data.contarItems( consulta, invocador);
    }

    @Override
    protected List<RegistroSolicitud> listarItems(Consulta consulta, Invoker invocador, List<String> ordenacion, RowBounds rowBounds) {
        return data.listarItems( consulta, invocador, ordenacion, rowBounds);
    }

    @Override
    protected RegistroSolicitud obtenerItem(String claveOrganizacion, Invoker invocador, Integer idItem) {
        return data.obtenerItem( claveOrganizacion, invocador, idItem);
    }
    
    protected RegistroSolicitud obtenerItemByIdDocumento( Integer idDocumento) {
        return data.obtenerItemByIdDocumento( idDocumento);
    }
    
    protected RegistroSolicitud obtenerItemByFolio( Propiedades propiedades) {
        return data.obtenerItemByFolio( propiedades.getPropiedad( "folio"), propiedades.getPropiedad( "nombre"));
    }
    
    @Override
	protected List<String> createOrdenacion(Consulta consulta) {
		final List<String> clausulas = new ArrayList<>();
	
		for ( Ordenacion item : consulta.getOrdenacion()) {
            switch( item.getCampo()) {
                case "nombre": 
                    clausulas.add(  item.generarClausula( "item.nombre"));
                    break;
                case "fechaHora":
                    clausulas.add( item.generarClausula( "item.fecha_hora"));
                    break;
			}
		}
        
        if( clausulas.isEmpty()) {
            clausulas.add( "item.fecha_hora DESC");
        }
		
		return clausulas;
	}
    
    private DocumentoFirmado entidadToItem( RegistroSolicitud entidad, String claveOrganizacion, Invoker invocador) {
        final DocumentoFirmado item;
        
        if (entidad.getIdDocumentoFirmado() != null) {
            item = docBsnsComponent.obtenerItem( claveOrganizacion, invocador, entidad.getIdDocumentoFirmado().toString(), null);
            
        } else {
            item = new DocumentoFirmado();
            item.setNombre(  entidad.getNombre());
        }
        
        item.setSolicitud( entidadToItem( entidad));
                
        return item;
    }
    
    private SolicitudFirma entidadToItem( RegistroSolicitud entidad) {
        final DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
                       
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        solicitud.setId(                entidad.getId());
        solicitud.setFechaHora(         formatter.format( entidad.getFechaHora()));
        solicitud.setFolio(             entidad.getFolio());
        solicitud.setSolicitante(       entidad.getEmailDestinatario());
        solicitud.setEmailSolicitante(  entidad.getEmailSolicitante());
        solicitud.setEstatus(           entidad.getEstatus());
        
        return solicitud;
    }
    
    
}
