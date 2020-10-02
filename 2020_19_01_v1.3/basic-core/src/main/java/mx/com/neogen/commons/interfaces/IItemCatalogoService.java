package mx.com.neogen.commons.interfaces;

import java.util.List;

import com.eurk.core.beans.consulta.Consulta;
import mx.com.neogen.commons.bean.ItemCatalogo;


public interface IItemCatalogoService {

	/**
	 * 	Consulta un catalogo en base a una o más propiedades
	 *  	
	 */
	List<ItemCatalogo> consultar(String claveOrganizacion, Invoker invocador, String catalogo, Consulta consulta);
	
	/**
	 * 	Consulta un item específico de un catalogo
	 */
	ItemCatalogo obtener( String claveOrganizacion, Invoker invocador, String catalogo, String idItem);
	
}