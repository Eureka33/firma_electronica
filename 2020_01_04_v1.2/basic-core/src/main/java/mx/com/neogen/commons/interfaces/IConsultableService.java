package mx.com.neogen.commons.interfaces;

import com.eurk.core.beans.consulta.Consulta;
import java.util.List;
import mx.com.neogen.commons.bean.Propiedades;

public interface IConsultableService {
	
	/**
	 * 	Obtiene un item de negocio  en base a un identificador y un conjunto opcional de propiedades
	 */
	Object getItem( String claveOrganizacion, Invoker invocador, String idItem, Propiedades propiedades);
	
	/**
	 * 	Obtiene un listado de items de negocio de acuerdo a la información de consulta
	 */
	List<?> getItems(String claveOrganizacion, Invoker invocador, Consulta consulta);
	
}
