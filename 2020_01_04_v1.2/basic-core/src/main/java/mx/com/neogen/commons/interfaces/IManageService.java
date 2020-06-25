package mx.com.neogen.commons.interfaces;

import java.util.Map;

import mx.com.neogen.commons.bean.Propiedades;

public interface IManageService {

	Object  manage(String claveOrganizacion, Invoker invocador, Propiedades parametros, Map<String, Object> entidad);
	
}
