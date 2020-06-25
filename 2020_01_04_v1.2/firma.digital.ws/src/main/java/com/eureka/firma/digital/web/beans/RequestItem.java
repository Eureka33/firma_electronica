package com.eureka.firma.digital.web.beans;

import java.io.Serializable;
import mx.com.neogen.commons.util.UtilText;


public class RequestItem implements Serializable {

	private static final long serialVersionUID = 7409952667633237159L;

	private String organizacion;
	private String modulo;
	private String entidad;
	private String id;
	
	public RequestItem( String organizacion, String modulo, String entidad, String id) {
		super();
		
		this.organizacion = organizacion;
		this.modulo = modulo;
		this.entidad = entidad;
		this.id = (id == null)? null : UtilText.unescapeURLString( id);
	}
	
	public RequestItem( String organizacion, String modulo, String entidad) {
		this(organizacion, modulo, entidad, null);
	}

	
	public String getOrganizacion() {
		return organizacion;
	}

	public String getModulo() {
		return modulo;
	}

	public String getEntidad() {
		return entidad;
	}

	public String getId() {
		return id;
	}
	
}