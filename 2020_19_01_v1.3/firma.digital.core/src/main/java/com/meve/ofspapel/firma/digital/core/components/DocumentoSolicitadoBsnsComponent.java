package com.meve.ofspapel.firma.digital.core.components;

import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.consulta.Ordenacion;
import com.meve.ofspapel.firma.digital.beans.ConsultaBase;
import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import com.meve.ofspapel.firma.digital.beans.DocumentoSolicitado;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.mappers.DocumentoSolicitadoDAO;
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
    
    @Autowired private DocumentoSolicitadoDAO data;
    
        
    public DocumentoFirmado obtenerItem( String claveOrganizacion, Invoker invocador, String idItemStr, Propiedades propiedades) {
        return entidadToItem( obtenerItem(claveOrganizacion, invocador, Integer.valueOf( idItemStr)));
    }
    
    public List<DocumentoSolicitado> listarItems( String claveOrganizacion, Invoker invocador, Consulta consulta) {
        final List<RegistroSolicitud> entidades = consultar( claveOrganizacion, invocador, consulta);
        final List<DocumentoSolicitado> items = new ArrayList<>();
        
        for( RegistroSolicitud entidad : entidades) {
            items.add( entidadToItem( entidad));
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
        return data.obtenerItem(claveOrganizacion, invocador, idItem);
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
    
    private DocumentoSolicitado entidadToItem( RegistroSolicitud entidad) {
        final DocumentoSolicitado item = new DocumentoSolicitado();
        
        item.setId( entidad.getId());
        item.setFechaHora( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss").format( entidad.getFechaHora()));
        item.setFolio( entidad.getFolio());
        item.setNombre( entidad.getNombre());
        item.setEstatus( entidad.getEstatus());
        
        return item;
    }
}
