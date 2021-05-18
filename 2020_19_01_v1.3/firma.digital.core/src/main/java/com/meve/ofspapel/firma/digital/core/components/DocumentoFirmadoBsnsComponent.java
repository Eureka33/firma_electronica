package com.meve.ofspapel.firma.digital.core.components;

import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.consulta.Ordenacion;
import com.meve.ofspapel.firma.digital.beans.ConsultaBase;
import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.mappers.DocumentoFirmadoDAO;
import com.meve.ofspapel.firma.digital.core.mappers.UsuarioDAO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.interfaces.Invoker;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DocumentoFirmadoBsnsComponent extends ConsultaBase<ArchivoDepositado, Integer> {
    
    @Autowired private DocumentoFirmadoDAO data;
    @Autowired private UsuarioDAO usuarioDAO;

    
    public DocumentoFirmado obtenerItem( String claveOrganizacion, Invoker invocador, String idItemStr, Propiedades propiedades) {
        ArchivoDepositado entidad;
        
        if( idItemStr == null) {
            entidad = obtenerItemByFolio( claveOrganizacion, invocador, propiedades.getPropiedad( "folio"), propiedades.getPropiedad( "nombre"));
        } else {
            entidad = obtenerItem(claveOrganizacion, invocador, Integer.valueOf( idItemStr));
        }
        
        if ( entidad == null) {
            return null;
        } else {
            return entidadToItem( entidad);
        }
    }
    
    public List<DocumentoFirmado> listarItems( String claveOrganizacion, Invoker invocador, Consulta consulta) {
        final List<ArchivoDepositado> entidades = consultar( claveOrganizacion, invocador, consulta);
        final List<DocumentoFirmado> items = new ArrayList<>();
        
        for( ArchivoDepositado entidad : entidades) {
            items.add( entidadToItem( entidad));
        }
        
        return items;
    }
    
    
    @Override
    protected int contarItems(Consulta consulta, Invoker invocador) {
       	return data.contarItems( consulta, invocador);
    }

    @Override
    protected List<ArchivoDepositado> listarItems(Consulta consulta, Invoker invocador, List<String> ordenacion, RowBounds rowBounds) {
        return data.listarItems( consulta, invocador, ordenacion, rowBounds);
    }

    @Override
    protected ArchivoDepositado obtenerItem(String claveOrganizacion, Invoker invocador, Integer idItem) {
        return data.obtenerItem(claveOrganizacion, invocador, idItem);
    }
    
    protected ArchivoDepositado obtenerItemByFolio( String claveOrganizacion, Invoker invocador, String folio, String nombre) {
        return data.obtenerItemByFolio( claveOrganizacion, invocador, folio, nombre);
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
    
    public DocumentoFirmado entidadToItem( ArchivoDepositado entidad) {
        final DocumentoFirmado item = new DocumentoFirmado();
        
        item.setId( entidad.getId());
        item.setFechaHora( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss").format( entidad.getFechaHora()));
        item.setFolio( entidad.getFolio());
        item.setNombre( entidad.getNombre());
        
        if( entidad.getIdUsuario() != null) {
            final Usuario usuario = usuarioDAO.obtenerItem( entidad.getIdUsuario());
            item.setFirmante( usuario.getNombre() + " (" + usuario.getClave() + ")");
        } else {
            item.setFirmante( "");
        }
        
        return item;
    }
}
