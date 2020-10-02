package com.meve.ofsp.certificado;

import mx.com.neogen.commons.util.UtilReflection;

public class SujetoX509 {

	private String CN;
	private String serialNumber;
	private String uniqueIdentifier;
	
	
	public SujetoX509() {
		super();
	}
	
	
	public String getCN() {
		return CN;
	}
	public void setCN(String cN) {
		CN = cN;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getUniqueIdentifier() {
		return uniqueIdentifier;
	}
	public void setUniqueIdentifier(String uniqueIdentifier) {
		this.uniqueIdentifier = uniqueIdentifier;
	}

	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
}