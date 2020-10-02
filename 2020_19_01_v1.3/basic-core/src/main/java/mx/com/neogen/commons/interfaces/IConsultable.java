package mx.com.neogen.commons.interfaces;

import java.util.List;
import java.util.Map;

import com.eurk.core.beans.consulta.Consulta;

/**
 * 	<p>Interface con los métodos que deben ser implementados por un DAO para la consulta de una entidad</p>
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 *
 * @param <T>	Tipo de item devuelto por la consulta
 * 
 */
public interface IConsultable<T, ID> {
	
	/**
	 *	<p>Busca items utilizando la información de una {@link Consulta}.</p>
	 *  <p>Si se solicita la primer pagina, el bean de consulta debe ser actualizado con el número total de items que
	 *  cumplen los criterios de consulta.</p>
	 * 
	 * 	@param claveOrganizacion	<p>Identificador de la orgnanización a la que pertenece el invocador</p>
	 * 	@param invocador			<p>Usuario que solicita la operación</p>
	 * 	@param consulta 			<p>Criterios de búsqueda y ordenación, tipo de consulta e información de paginación</p>
	 *  
	 * 	@return	<p>Arreglo de resultados encontrados, puede tener cero o más elementos.</p>
	 */
	List<T> consultar( String claveOrganizacion, Invoker invocador, Consulta consulta); 
	
	/**
	 *	<p>Obtiene un registro de la base de datos o genera y devuelve un nuevo item.</p>
	 *
	 *  @param claveOrganizacion <p>Identificador de la orgnanización a la que pertenece el invocador</p>
	 * 	@param invocador		<p>Usuario que solicita la operación</p>
	 * 	@param idItem			<p>Identificador del item </p>
	 *
	 * @return					<p>El usuario encontrado, o bien, un nuevo registro de usuario.</p>
	 */
	T obtenerItem( String claveOrganizacion, Invoker invocador, ID idItem, Map<String, String> params);
	
}