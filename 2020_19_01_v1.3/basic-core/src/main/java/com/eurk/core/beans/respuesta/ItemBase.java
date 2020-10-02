package com.eurk.core.beans.respuesta;

import java.io.Serializable;
import mx.com.neogen.commons.util.UtilReflection;


public class ItemBase implements Serializable {

	private static final long serialVersionUID = 1858975324932101837L;
	
	private String id;
	
	private String nombre;
	private String descripcion;
	private boolean activo;
	
	
	public ItemBase() {
		super();
	}

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}