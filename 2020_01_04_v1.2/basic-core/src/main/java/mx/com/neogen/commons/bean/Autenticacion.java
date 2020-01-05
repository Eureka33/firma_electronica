package mx.com.neogen.commons.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	Información provista por el usuario al ingresar a un sitio
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public class Autenticacion implements Serializable {

	private static final long serialVersionUID = 6849197290403495860L;

	private String username;			// identificador de usuario
	private String password;			// clave o password
	private String llave;				// llave única de session
	private String organizacion;		// clave de organizacion
	
	
	public Autenticacion() {
		super();	
	}

	
	public String getUsername() {		return username;	}
	public void setUsername(		final String username	) {
		this.username = username;
	}
	
	public String getPassword() {		return password;	}
	public void setPassword(		final String password	) {
		this.password = password;
	}

	public String getLlave() {			return llave;		}
	public void setLlave(			final String llave		) {
		this.llave = llave;
	}
	
	public String getOrganizacion() {	return organizacion; }
	public void setOrganizacion(	final String organizacion	) {	
		this.organizacion = organizacion;
	}
	
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}