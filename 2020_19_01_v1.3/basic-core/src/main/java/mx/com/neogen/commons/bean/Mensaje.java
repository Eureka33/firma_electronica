package mx.com.neogen.commons.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Un mensaje se compone de una llave de mensaje y uno o más parametros.
 * 
 * 	La llave de mensaje identifica a un mensaje parametrizable de la forma:
 *  	mensajeKey = Mensaje {0} parametrizable {1} ...
 *  
 *  Al mostrar el mensaje al usuario se tomara el mensaje de un archivo de 
 * propiedades y los parametros serán reemplazados.
 *  
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public class Mensaje implements Serializable {
	
	private static final long serialVersionUID = -1093794644960305395L;
	
	private String llaveMensaje;
	private Object[] parametros;
	
	
	public Mensaje( String llaveMensaje, Object... parametros) {
		super();
		this.llaveMensaje = llaveMensaje;
		this.parametros   = parametros;
	}
	
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
	public String getLlaveMensaje() {
		return llaveMensaje;
	}
	
	public Object[] getParametros() {
		return parametros;
	}
	
}