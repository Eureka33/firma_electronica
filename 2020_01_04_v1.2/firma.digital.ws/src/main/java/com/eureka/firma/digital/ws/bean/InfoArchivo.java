package com.eureka.firma.digital.ws.bean;

import java.io.Serializable;

import javax.activation.DataHandler;

/**	
 * 	Almacena la información de un archivo que esta siendo transferido por el 
 * servicio web.
 */
public class InfoArchivo implements Serializable {
	
	private static final long serialVersionUID = -5982418692346802353L;

	private String nombre;				// nombre del archivo
	private String extension;			// extensión
	
	private DataHandler handler;		// handler de archivo
	
	
	public InfoArchivo() {
		super();
	}
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getExtension() {
		return extension;
	}
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
	public DataHandler getHandler() {
		return handler;
	}
	public void setHandler(DataHandler handler) {
		this.handler = handler;
	}
	
}