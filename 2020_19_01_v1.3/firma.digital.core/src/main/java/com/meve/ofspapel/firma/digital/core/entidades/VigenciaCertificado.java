package com.meve.ofspapel.firma.digital.core.entidades;

import java.io.Serializable;
import java.util.Date;

import mx.com.neogen.commons.util.UtilReflection;

public class VigenciaCertificado implements Serializable {

	private static final long serialVersionUID = -2236095074794783197L;

	private String serial;
	private Date fechaIni;
	private Date fechaFin;
	private String rfc;
	private char estatus;
	
	
	public VigenciaCertificado() {
		super();
	}


	public String getSerial() {
		return serial;
	}
	public void setSerial(		final String serial) {
		this.serial = serial;
	}

	public Date getFechaIni() {
		return fechaIni;
	}
	public void setFechaIni(	final Date fechaIni) {
		this.fechaIni = fechaIni;
	}

	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(	final Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public String getRfc() {
		return rfc;
	}
	public void setRfc(			final String rfc) {
		this.rfc = rfc;
	}

	public char getEstatus() {
		return estatus;
	}
	public void setEstatus(		final char estatus) {
		this.estatus = estatus;
	}

	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}
