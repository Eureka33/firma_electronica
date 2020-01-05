package mx.com.neogen.commons.interfaces;

import java.io.Serializable;
import java.util.List;


/**
 * 	Las entidades que pueden ser gestionadas por un sistema forman una 
 * estructura de tipo árbol.
 *  La atención a una nueva entidad puede dar pie a un arbol de entidades 
 * requerido dar una solución.
 *  
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public interface INodo<ID extends Serializable, T> {
	
	ID getIdNodo();
	
	ID getIdNodoPadre();
	
	List<T> getHijos();
		
	void setHijos( List<T> hijos);
	
	boolean esNodoRaiz();
		
}