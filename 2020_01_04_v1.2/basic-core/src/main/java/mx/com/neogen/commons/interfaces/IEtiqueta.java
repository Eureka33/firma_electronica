package mx.com.neogen.commons.interfaces;

/**
 *	Esta interface esta pensada para utilizarse en el despliegue de opciones o 
 * listados en paginas web
 * 	La propiedad 'valor' representa el valor obtenido al seleccionar una opcion
 * o un elemento de un listado.
 *  La propiedad 'keyDescripcion' es una llave de mensaje tomada de un archivo
 *  de mensajes localizado.
 *  
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public interface IEtiqueta {

	/**
	 *	Valor que será regresado al seleccionar la entidad
	 */
	String getValor();
	
	/**
	 *	Llave del mensaje localizado que será presentado al usuario.
	 */
	String getKeyDescripcion();
	
}
