package mx.com.neogen.commons.bean;

import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;

import mx.com.neogen.commons.util.UtilReflection;


public class Item implements Serializable {
	
	private static final long serialVersionUID = -7042627382617388693L;
	
	private String			 idItem;
	private Map<String, Object> propiedades;
	private boolean			 activo;
	
	
	
	public Item( final String idItem) {
		super();
		this.idItem = idItem;
		this.propiedades = new TreeMap<String, Object>();
	}

	public Item() {
		this( null);
	}

	
	
	
	public String getIdItem() {								return idItem;				}
	public void setIdItem(		final String value) {		this.idItem = value;		}

	public Map<String, Object> getPropiedades() {			return propiedades;			}
	public void setPropiedades(	final Map<String, Object> value) {	this.propiedades = value;	}

	public boolean isActivo() {								return activo;				}
	public void setActivo(		final boolean value) {		this.activo = value;		}
	
	
	
	public String toString() {
		return UtilReflection.toString( this);
	}

}