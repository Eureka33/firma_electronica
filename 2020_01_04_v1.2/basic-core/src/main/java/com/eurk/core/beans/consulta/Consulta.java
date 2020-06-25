package com.eurk.core.beans.consulta;

import com.eurk.core.beans.consulta.enums.EnumTipoConsulta;
import java.util.Map;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Bean con información para realizar una consulta en algún origen de datos.
 *	
 * 	@author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public class Consulta {

	private String[] 	     tokens;				// palabras de búsqueda general
	private Propiedades propiedades;				// propiedades específicas de consulta
	private EnumTipoConsulta   tipo;				// tipo de consulta 
	private Ordenacion[] ordenacion;				// información de ordenación
	private Paginacion	 paginacion;				// información de paginación
	
		
	public Consulta() {
		this( EnumTipoConsulta.NOPAGINADA, null);
	}	
	
	public Consulta( EnumTipoConsulta tipo) {
		this( tipo, null);
	}
	
	public Consulta( EnumTipoConsulta tipo, Map<String, Object> propiedades) {
		super();
		this.paginacion  = new Paginacion();
		this.ordenacion  = new Ordenacion[] {};
		
		this.propiedades = (propiedades == null) ? new Propiedades() : new Propiedades( propiedades);
		
		this.tipo        = tipo;
	}
	
	
	public String[] getTokens() {
		return tokens;
	}
	
	public void setTokens( final String[] tokens) {
		this.tokens = tokens;
	}
	
	public Propiedades getPropiedades() {
		return propiedades;
	}
	
	public void setPropiedades( final Propiedades propiedades) {
		this.propiedades = propiedades;
	}
	
	public void agregarPropiedad( final String llave, Object propiedad) {
		propiedades.put( llave, propiedad);
	}
	
	public EnumTipoConsulta getTipo() {
		return tipo;
	}
	
	public void setTipo( final EnumTipoConsulta tipo) {
		this.tipo = tipo;
	}

	public Ordenacion[] getOrdenacion() {
		return ordenacion;
	}
	
	public void setOrdenacion( final Ordenacion[] ordenacion) {
		this.ordenacion = ordenacion;
	}

	public Paginacion getPaginacion() {
		return paginacion;
	}
	
	public void setPaginacion( final Paginacion paginacion) {
		this.paginacion = paginacion;
	}
	
	
	public String toString() {
		return UtilReflection.toString( this);
	}

}