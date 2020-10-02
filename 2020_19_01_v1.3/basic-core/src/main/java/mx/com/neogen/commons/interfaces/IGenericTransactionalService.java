package mx.com.neogen.commons.interfaces;

import java.io.InputStream;


public interface IGenericTransactionalService<T, ID> extends IConsultable<T, ID> {

	/**
	 * 	Devuelve una entidad a partir de la información contenida en un InputStream que contiene información en formato
	 *  JSON
	 */
	T obtenerItem( InputStream jsonStream);
	
	/**
	 *	<p>Persiste un item en un origen de datos, devuelve el item actualizado</p>
	 *
	 */
	T guardar( T item, final Invoker invocador);
	
}