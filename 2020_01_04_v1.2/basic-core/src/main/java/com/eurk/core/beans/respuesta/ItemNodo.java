package com.eurk.core.beans.respuesta;

import java.io.Serializable;
import mx.com.neogen.commons.util.UtilReflection;


public class ItemNodo implements Serializable, Comparable<ItemNodo> {

	private static final long serialVersionUID = -9216106363532315215L;

	private Integer id;
	private Integer parentId;
	
	private String      nombre;
	private String descripcion;
    private Integer   posicion;
	private boolean tieneHijos;
	private boolean     activo;
	

	
	public ItemNodo() {
		super();
        this.posicion = 0;
	}
	
	public ItemNodo( Integer id) {
		this();	
		this.id = id;
	}

	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Integer getParentId() {
		return parentId;
	}
	
	public void setParentId( Integer parentId) {
		this.parentId = parentId;
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
    
    public Integer getPosicion() {
		return posicion;
	}
	 
	public void setPosicion( Integer posicion) {
		this.posicion = posicion;
	}
    
	public boolean isTieneHijos() {
		return tieneHijos;
	}
	
	public void setTieneHijos( boolean tieneHijos) {
		this.tieneHijos = tieneHijos;
	}
	
	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	
	@Override
	public int compareTo(ItemNodo nodo) {
		return nombre.compareTo( nodo.nombre);
	}
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}