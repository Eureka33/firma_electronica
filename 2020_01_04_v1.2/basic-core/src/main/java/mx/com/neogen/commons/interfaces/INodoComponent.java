package mx.com.neogen.commons.interfaces;

import java.io.Serializable;
import java.util.List;

import mx.com.neogen.commons.interfaces.INodo;

public interface INodoComponent {

	/**
	 * 	Recrea una jerarquia completa a partir del listado de todos los nodos
	 */
	<ID extends Serializable, T extends INodo<ID, T>> T recrearArbol( 	final List<T> nodos);
	
}
