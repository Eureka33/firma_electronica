package com.eurk.core.beans.consulta;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Esta clase reune la información de paginación necesaria para realizar 
 * una consulta
 *
 */
public class Paginacion implements Serializable {

	private static final long serialVersionUID = 4364031132722375191L;

	private int		 pagina;	// numero de pagina actual (inicia en 1)
	private int itemsPagina;   	// elementos máximos por página 
	private int  totalItems;
	
	
	public Paginacion() {
		super();
		
		this.pagina 	 = 1;
		this.itemsPagina = 10;
	}

	
	public int getPagina() {								return pagina;					}
	public void setPagina(		final int pagina) {			this.pagina = pagina;			}

	public int getItemsPagina() {							return itemsPagina;				}
	public void setItemsPagina(	final int itemsPagina) {	this.itemsPagina = itemsPagina;	}
	
	public int getTotalItems() {							return totalItems;				}
	public void setTotalItems(	final int totalItems) {		this.totalItems = totalItems;	}

	
	public String toString() {
		return UtilReflection.toString( this);
	}

}
