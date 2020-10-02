package mx.com.neogen.commons.interfaces;

import java.util.Map;

import mx.com.neogen.commons.bean.Propiedades;

public interface ICRUDService {
	
	/**
	 * 	Define el tipo de dato del item enviado al método save()
	 * 
	 * @return	La clase del item al que será casteado el bean recibido en el endpoint de servicio
	 */
	@SuppressWarnings("rawtypes")
	Class<?> getItemClass();
	
	/**
	 * 	Borra una o más entidades en base a sus identificadores
	 * 
	 * @param claveOrganizacion		clave de la organizacion en el endpoint de servicio
	 * @param invocador				entidad que invoca el servicio, normalmente un usuario final
	 * @param idItems				arreglo de uno o más identificadores
	 * 
	 * @return	El numero de registros eliminados
	 */
	Map<String, Object> delete(String claveOrganizacion, Invoker invocador, Propiedades propiedades);
	
	/**
	 * 	Guarda la información de una entidad (inserta o actualiza), dependiendo de si la entidad existe.
	 * 
	 * @param claveOrganizacion		clave de la organización en el endpoint de servicio
	 * @param invocador				entidad que invoca el servicio, normalmente un usuario final
	 * @param item					entidad de datos que cuya información debe ser persistida
	 * 
	 * @return	La misma entidad de datos recibida y actualizada
	 */
	Object create( String claveOrganizacion, Invoker invocador, Object item);
	
	Object update( String claveOrganizacion, Invoker invocador, Object item);
	
}