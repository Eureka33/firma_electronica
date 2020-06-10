package com.eureka.firma.digital.ws.bean;

public class RespuestaFirma {

	private Integer codigo;
	private String mensaje;
	
	private Firma firma;
	
	
	public RespuestaFirma() {
		super();
	}


	public Integer getCodigo() {
		return codigo;
	}
	
    public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}
	
    public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Firma getFirma() {
		return firma;
	}
	
    public void setFirma( Firma firma) {
		this.firma = firma;
	}
	
}