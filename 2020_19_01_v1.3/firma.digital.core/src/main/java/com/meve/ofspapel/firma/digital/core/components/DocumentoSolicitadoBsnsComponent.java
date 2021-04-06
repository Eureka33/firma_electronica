package com.meve.ofspapel.firma.digital.core.components;

import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.consulta.Ordenacion;
import com.meve.ofspapel.firma.digital.beans.ConsultaBase;
import com.meve.ofspapel.firma.digital.beans.DocumentoSolicitado;
import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.mappers.DocumentoSolicitadoDAO;
import com.meve.ofspapel.firma.digital.core.mappers.UsuarioDAO;
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
    @Autowired private UsuarioDAO usuarioDAO;
    
        
    public DocumentoSolicitado obtenerItem( String claveOrganizacion, Invoker invocador, String idItemStr, Propiedades propiedades) {
        return entidadToItem( obtenerItem(claveOrganizacion, invocador, Integer.valueOf( idItemStr)), claveOrganizacion, invocador);
    }
    
    public List<DocumentoSolicitado> listarItems( String claveOrganizacion, Invoker invocador, Consulta consulta) {
        final List<RegistroSolicitud> entidades = consultar( claveOrganizacion, invocador, consulta);
        final List<DocumentoSolicitado> items = new ArrayList<>();
        
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
    
    private DocumentoSolicitado entidadToItem( RegistroSolicitud entidad, String claveOrganizacion, Invoker invocador) {
        final DocumentoSolicitado item = new DocumentoSolicitado();
        final DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
        
        item.setId( entidad.getId());
        item.setFechaHora( formatter.format( entidad.getFechaHora()));
        item.setFolio( entidad.getFolio());
        item.setNombre( entidad.getNombre());
        item.setEstatus( entidad.getEstatus());
        
        if (entidad.getIdDocumentoFirmado() != null) {
            final ArchivoDepositado documento = docBsnsComponent.obtenerItem( claveOrganizacion, invocador, entidad.getIdDocumentoFirmado());
            final Usuario usuario = usuarioDAO.obtenerItem( documento.getIdUsuario());
            
            item.setDestinatario( usuario.getNombre() + " (" + usuario.getClave() + ")");
            item.setFechaHoraFirma( formatter.format( documento.getFechaHora()));
            
            item.setDocumentoFirmado( docBsnsComponent.entidadToItem( documento));
            
        } else {
            item.setDestinatario( entidad.getEmailDestinatario());
            item.setFechaHoraFirma( "");
        }
        
        return item;
    }
}
