package mx.com.neogen.commons.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Bean con información para realizar una consulta en algún origen de datos.
 *	
 * 	@author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public class Consulta implements Serializable {

	private static final long serialVersionUID = 3967204147039612770L;
	
	private String[] 			tokens;
	private Map<String, Object> propiedades; 
	private String 				tipoConsulta;
	private Ordenacion 			ordenacion;
	private Paginacion			paginacion;
	
	
	
	public Consulta() {
		super();
		
		this.paginacion = new Paginacion();
		this.ordenacion = new Ordenacion();
		this.propiedades = new HashMap<String, Object>();
	}


	
	public String[] getTokens() {								return tokens;					}
	public void setTokens(		final String[] tokens) {		this.tokens = tokens;			}
	
	public Map<String, Object> getPropiedades() { 							return propiedades;				}
	public void setPropiedades( final Map<String, Object> propiedades) { 	this.propiedades = propiedades; }
	
	public String getTipoConsulta() {							return tipoConsulta;				}
	public void setTipoConsulta(final String tipoConsulta) {	this.tipoConsulta = tipoConsulta;	}

	public Ordenacion getOrdenacion() {							return ordenacion;				}
	public void setOrdenacion(	final Ordenacion ordenacion) {	this.ordenacion = ordenacion;	}

	public Paginacion getPaginacion() {							return paginacion;				}
	public void setPaginacion(	final Paginacion paginacion) {	this.paginacion = paginacion;	}
		
	
	public String toString() {
		return UtilReflection.toString( this);
	}

}