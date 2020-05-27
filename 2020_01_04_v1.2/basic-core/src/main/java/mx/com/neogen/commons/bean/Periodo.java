package mx.com.neogen.commons.bean;

import java.io.Serializable;
import java.util.Date;

import mx.com.neogen.commons.util.UtilReflection;

/**
 *	Representa un periodo o lapso de tiempo comprendido entre una fecha/hora
 * inicial y una fecha/hora final 
 *
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public class Periodo implements Serializable {
	
	private static final long serialVersionUID = -5036289312909424945L;

	private Date fechaIni;
	private Date fechaFin;

	
	public Periodo() {
		super();	
	}

	public Periodo( Date fechaIni, Date fechaFin) {
		this();
		this.fechaIni = fechaIni;
		this.fechaFin = fechaFin;
	}
	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
		
	/**
	 * 	Determina si el periodo o intervalo de tiempo almacenado en este bean 
	 * es valido. En un periodo valido, la fecha inicial debe ser anterior 
	 * a la fecha final. 
	 *
	 * @return true, si se trata de un periodo valido.
	 */
	public boolean isValido() {
		if ( fechaIni == null || fechaFin == null) {
			return false;
		}
		
		return fechaFin.after( fechaIni); 
	}

	
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(Date fechaIni) {
		this.fechaIni = fechaIni;
	}
		
}
