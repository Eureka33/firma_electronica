package mx.com.neogen.commons.interfaces;

public interface IArbolConsultable<T, ID>{
	
	/**
	 * Obtiene el arbol al que pertenece el nodo con el identificador
	 */
	Object obtenerArbol( final String idItem, final Invoker invocador);

	/**
	 * 	Obtiene toda una rama a partir de un nodo
	 */
	<N extends INodo> N[] obtenerRama( final String idItem, final Invoker invocador);
	
}
