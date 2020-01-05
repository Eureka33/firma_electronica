package mx.com.neogen.commons.logs;

import java.io.Serializable;
import java.util.Date;

import mx.com.neogen.commons.util.UtilReflection;
import mx.com.neogen.commons.util.UtilText;

/**
 * 	Este bean registra cualquier evento cuya duración y resultado sea de 
 * interes.
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */

public class RegistroEvento implements Serializable {

	private static final long serialVersionUID = -1246575113687503824L;

	private String descripcion;
	private Date	    inicio;
	private long	  duracion;
	
	private boolean    	 exito;
	private String     mensaje;
	
	
	
	public RegistroEvento() {
		super();
		this.inicio			= new Date();
	}
	


	public void terminarOK() {
		terminarOK( null);
	}
	
	public void terminarOK( 	final String mensaje) {
		terminar( true, mensaje);
	}
	
	public void terminarError() {
		terminarError( null);
	}
	
	public void terminarError( 	final String mensaje) {
		terminar( false, mensaje); 
	}

	public void terminarPorExcepcion( 	final Throwable tirable) {
		terminar( false, UtilText.stackTraceToString( tirable));
	}
	
	public void terminar(		final boolean exito)	{
		terminar( exito, null);
	}
	
	public void terminar( 		final boolean exito, String mensaje) {
		this.exito	 	= exito;
		this.mensaje 	= (mensaje == null)? "" : mensaje;
		
		this.duracion	= (new Date().getTime()) - inicio.getTime();
	}
	
	public String getDescripcion() {						return descripcion;				}
	public void setDescripcion(	final String descripcion) {	this.descripcion = descripcion;	}

	public Date getInicio() {								return inicio;					}
	public void setInicio(		final Date inicio) {		this.inicio = inicio;			}

	public long getDuracion() {								return duracion;				}
	public void setDuracion(	final long duracion) {		this.duracion = duracion;		}

	public boolean isExito() {								return exito;					}
	public void setExito(		final boolean exito) {		this.exito = exito;				}

	public String getMensaje() {							return mensaje;					}
	public void setMensaje(		final String mensaje) {		this.mensaje = mensaje;			}
	
	
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}