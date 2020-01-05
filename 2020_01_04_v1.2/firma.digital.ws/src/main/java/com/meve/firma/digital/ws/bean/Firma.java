package com.meve.firma.digital.ws.bean;

import java.io.Serializable;


public class Firma implements Serializable {

	private static final long serialVersionUID = -1950695546057002662L;

	private String titular;
	private String rfc;
	private String curp;
	private String firmaElectronica;
	private String fecha;
	
	
	public Firma() {
		super();
	}


	
	public String getTitular() {
		return titular;
	}
	public void setTitular(String titular) {
		this.titular = titular;
	}

	public String getRfc() {
		return rfc;
	}
	public void setRfc(String rfc) {
		this.rfc = rfc;
	}
	
	public String getCurp() {
		return curp;
	}
	public void setCurp( String curp) {
		this.curp = curp;
	}

	public String getFirmaElectronica() {
		return firmaElectronica;
	}
	public void setFirmaElectronica(String firmaElectronica) {
		this.firmaElectronica = firmaElectronica;
	}

	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

}
