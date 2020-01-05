package mx.com.neogen.commons.bean;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Clase de conveniencia para almancenar uno o más parametros ( parejas del
 * tipo 'nombreParametro' = 'valorParametro').
 * 
 *  'nombreParametro' es de tipo String
 *  'valorParametro' puede ser cualquier tipo de valor
 * 
 *  La ventaja de esta clase sobre un Map es únicamente sintáctica. Enviar 
 * un bean Parametros en lugar de un Map<String, ...> hará más claro el
 * proposito del objeto.
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public class Propiedades implements Serializable {
	
	private static final long serialVersionUID = 1378091056812550697L;

	private Map<String, Object> parametros;

	
	public Propiedades() {
		super();
		parametros = new HashMap<String, Object>();
	}
	
	
	/**
	 * 	Agrega o actualiza el valor de un parámetro
	 * 
	 * @param paramName		- Nombre del parámetro
	 * @param valor			- Valor del parámetro
	 */
	public void setParametro( String paramName, Object valor) {
		if ( paramName == null || paramName.trim().isEmpty()) {
			throw new IllegalArgumentException(
				"Parameter name can not be empty"
			);
		}
		parametros.put( paramName, valor);
	}

	/**
	 * 	Recupera el valor de un parámetro
	 * 
	 * @param <T>		- tipo de dato del valor del parámetro
	 * @param paramName	
	 * @return
	 */
	@SuppressWarnings( "unchecked")
	public <T> T getParametro( String paramName) {
		return (T) parametros.get( paramName);
	}
	
	public boolean containsParam(final String name){
		return parametros.containsKey(name);
	}
	
	/** 
	 *  Devuelve un conjunto con los nombre de los parámetros contenidos en 
	 * este mapa.
	 */
	public Set<String> getNombresParametros() {
		return parametros.keySet(); 
	}
	
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
}
