package com.eureka.firma.digital.web;

import com.eureka.firma.digital.web.beans.RequestItem;
import com.eurk.core.beans.respuesta.ListaMensajes;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.exception.PeticionIncorrectaException;
import mx.com.neogen.commons.interfaces.Invoker;

public final class UtilWeb {
	
	public static String getHostRemoto( final HttpServletRequest request) {
		final String clientHost = request.getHeader("X-Forwarded-For");
		return (clientHost == null)? request.getRemoteAddr() : clientHost;
	}
	
	public static String getParametroRequest( HttpServletRequest request, String parametro) {
		return request.getParameter( parametro);
	}
	
	@SuppressWarnings("rawtypes")
	public static Propiedades getParametros( HttpServletRequest request) {
		final Propiedades parametros = new Propiedades();
		
		final Enumeration items = request.getParameterNames();
		String nombre = null;
		
		while ( items.hasMoreElements()) {
			nombre = (String) items.nextElement();
			parametros.put( nombre, request.getParameter( nombre));
		}
		
		return parametros;
		
	}
	
	public static Usuario getUsuarioPuesto( HttpServletRequest request) {
		final HttpSession sesion = getSession(request);
		return (Usuario) sesion.getAttribute( "usuario");
	}
	
	public static HttpSession getSession(final HttpServletRequest request){
		final HttpSession sesion = request.getSession( false);
		return sesion == null ? request.getSession(true) : sesion;
	}
	
	@SuppressWarnings("unchecked")
	public static String requestToString(	HttpServletRequest request) {
		
		final StringBuilder strb = new StringBuilder();
		
		strb.append( "{\nparametros: [");
				
		final Enumeration<String>  nombres = request.getParameterNames();
		while (nombres.hasMoreElements()) {
			String nombre = nombres.nextElement();
			strb.append( "\n\t{ nombre: ").append( nombre).append( ", valor: ");
			strb.append( Arrays.toString(request.getParameterValues( nombre))).append( "}");
		}
		
		strb.append("\n],\ninput_stream: {");
		
		try {
			final BufferedReader reader = new BufferedReader( new InputStreamReader(request.getInputStream()));
				
			String linea = null;
		
			while ( ( linea = reader.readLine()) != null) { 
				strb.append("\n\t").append( linea);
			}
			
		} catch( Exception ex) {
			ex.printStackTrace();
		}
				
		strb.append( "\n}");
		
		return strb.toString();
					
	}

	public static void validateRequest( Invoker invocador, RequestItem item) {
	
		ListaMensajes mensajes = null;
		
		if(invocador == null) {
			mensajes = new ListaMensajes();
			mensajes.addLlaveMensaje( "error.sesion.invalida");
			throw new PeticionIncorrectaException(mensajes.getListado());
		}
		
		final Usuario usuario = (Usuario) invocador;
		
        /*
		if ( !usuario.getClaveOrganizacion().equals( item.getOrganizacion())) {
			mensajes = new ListaMensajes();
			mensajes.addLlaveMensaje( "error.organizacion.incorrecta");
			throw new PeticionIncorrectaException( mensajes.getListado());
		}
        */
	}
}
