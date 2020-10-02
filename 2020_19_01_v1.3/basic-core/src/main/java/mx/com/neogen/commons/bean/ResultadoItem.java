package mx.com.neogen.commons.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

public class ResultadoItem implements Serializable {

	private static final long serialVersionUID = -1733573291616344674L;
	
	private Object 		 item;
	private boolean 	error;
	private Mensaje[] mensajes;
	
	
	
	public ResultadoItem() {
		super();
	}
		
	
	
	public Object getItem() {								return item;			}
	public void setItem(		final Object value) {		this.item = value;		}

	public boolean isError() {								return error;			}
	public void setError(		final boolean value) {		this.error = value;		}

	public Mensaje[] getMensajes() {						return mensajes;		}
	public void setMensajes(	final Mensaje[] value) {	this.mensajes = value;	}
	
	
	public String toString() {	return UtilReflection.toString( this);	}
	
}