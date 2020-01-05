package mx.com.neogen.commons.bean;

import java.io.Serializable;

import mx.com.neogen.commons.util.UtilReflection;

public class ResultadoConsulta implements Serializable {

	private static final long serialVersionUID = -1733573291616344674L;
	
	private Consulta consulta;
	private Object[] 	items;
	private boolean 	error;
	private Mensaje[] mensajes;
	
	
	
	public ResultadoConsulta() {
		super();
	}
	
	public ResultadoConsulta( 	final Consulta consulta) {
		super();	
		this.consulta = consulta;
	}

	
	
	public Consulta getConsulta() {							return consulta;		}
	public void setConsulta(	final Consulta value) {		this.consulta = value;	}

	public Object[] getItems() {							return items;			}
	public void setItems(		final Object[] value) {		this.items = value;		}

	public boolean isError() {								return error;			}
	public void setError(		final boolean value) {		this.error = value;		}

	public Mensaje[] getMensajes() {						return mensajes;		}
	public void setMensajes(	final Mensaje[] value) {	this.mensajes = value;	}
	
		
	public String toString() {	return UtilReflection.toString( this);	}
	
}