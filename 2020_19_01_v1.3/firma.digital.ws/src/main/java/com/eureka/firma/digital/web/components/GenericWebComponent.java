package com.eureka.firma.digital.web.components;

import com.eureka.firma.digital.web.ComponentLocator;
import com.eureka.firma.digital.web.UtilWeb;
import com.eureka.firma.digital.web.beans.RequestItem;
import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.consulta.enums.EnumTipoConsulta;
import com.eurk.core.beans.respuesta.RespuestaItemWS;
import com.eurk.core.beans.respuesta.RespuestaItemsWS;
import com.eurk.core.beans.respuesta.RespuestaWS;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.interfaces.ICRUDService;
import mx.com.neogen.commons.interfaces.IConsultableService;
import mx.com.neogen.commons.interfaces.IManageService;
import mx.com.neogen.commons.util.UtilBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class GenericWebComponent {
	
	@Autowired private ComponentLocator serviceLocator;
		

	public RespuestaWS listar( 		final HttpServletRequest   request,
									final HttpServletResponse response,
									final RequestItem	  	      item,
									final Consulta            consulta) {
        
		final Usuario invocador = UtilWeb.getUsuarioPuesto( request);
		
		try {
		    UtilWeb.validateRequest( invocador, item);
            	
        	final IConsultableService service = serviceLocator.getConsultableService( item);
			
        	final RespuestaItemsWS respuesta = RespuestaItemsWS.crear(
				service.getItems( item.getOrganizacion(), invocador, consulta)
			);
			
        	if( !EnumTipoConsulta.NOPAGINADA.equals( consulta.getTipo())) {
				respuesta.setConsulta( consulta);
			}

        	return respuesta;
			
		} catch( Exception ex) {
			return RespuestaWS.crear( ex);

		}
	}
	

	public RespuestaWS crear(		final HttpServletRequest   request,
									final HttpServletResponse response,
									final RequestItem	  	      item) {

		final Usuario invocador = UtilWeb.getUsuarioPuesto( request);
		
		try {
			UtilWeb.validateRequest( invocador, item);
			
			final ICRUDService service = serviceLocator.getCRUDService( item);
			
			final InputStream itemStream = request.getInputStream();
			final Object entidad = UtilBean.parseBean(itemStream, service.getItemClass());
			
			return RespuestaItemWS.crear( service.create( item.getOrganizacion(), invocador, entidad));
			
		} catch( Exception ex) {
			return RespuestaWS.crear( ex);
		}
	}
	
	public RespuestaWS actualizar(	final HttpServletRequest   request,
									final HttpServletResponse response,
									final RequestItem	  	      item) {

		final Usuario invocador = UtilWeb.getUsuarioPuesto( request);

		try {
			UtilWeb.validateRequest( invocador, item);

			final ICRUDService service = serviceLocator.getCRUDService( item);

			final InputStream itemStream = request.getInputStream();
			final Object entidad = UtilBean.parseBean(itemStream, service.getItemClass());

			return RespuestaItemWS.crear( service.update( item.getOrganizacion(), invocador, entidad));

		} catch( Exception ex) {
			return RespuestaWS.crear( ex);
		}
	}

	public RespuestaWS getItem(		final HttpServletRequest   request,
									final HttpServletResponse response,
									final RequestItem	  	      item,
									final Propiedades		propiedades) {
			
		final Usuario invocador = UtilWeb.getUsuarioPuesto( request);

		try {
			UtilWeb.validateRequest( invocador, item);
			
			final IConsultableService service = serviceLocator.getConsultableService( item);
			return RespuestaItemWS.crear( service.getItem( item.getOrganizacion(), invocador, item.getId(), propiedades));	

		} catch(Exception e) {
			return RespuestaWS.crear(e);
			
		}
	}
	
	public RespuestaWS borrar(		final HttpServletRequest request, 
			                        final HttpServletResponse response,
			                        final RequestItem item, final Propiedades propiedades) {

		final Usuario invocador = UtilWeb.getUsuarioPuesto( request);
		
		try {
			UtilWeb.validateRequest( invocador, item);
			
			final ICRUDService service = serviceLocator.getCRUDService(item);

			final List<String> identificadores = new ArrayList<>();
			identificadores.add( item.getId());
			
			propiedades.put( "idsItems", identificadores);
			
			return RespuestaItemWS.crear( service.delete( item.getOrganizacion(), invocador, propiedades));

		} catch (Exception ex) {
			return RespuestaWS.crear(ex);
		
		}
     }
	
	
	
	public RespuestaWS manage(       final HttpServletRequest request, 
			                         final HttpServletResponse response,
			                         final RequestItem item) {

		final Usuario invocador = UtilWeb.getUsuarioPuesto( request);

		try {
			UtilWeb.validateRequest( invocador, item);
			
			final InputStream itemStream = request.getInputStream();
			final Map<String,Object> entidad = UtilBean.parseBean( itemStream, Map.class);
			
			final Propiedades parametros = UtilWeb.getParametros( request);
			
			final IManageService service = serviceLocator.getManageService(item);

			return RespuestaItemWS.crear(service.manage(item.getOrganizacion(), invocador, parametros, entidad));

		} catch (Exception ex) {
			return RespuestaWS.crear(ex);

		}
	}

}