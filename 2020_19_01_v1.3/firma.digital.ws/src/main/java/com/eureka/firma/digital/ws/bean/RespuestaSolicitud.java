package com.eureka.firma.digital.ws.bean;

public class RespuestaSolicitud {

	private Integer codigo;
	private String mensaje;
	
	private Firma firma;
	
	
	public RespuestaSolicitud() {
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