package mx.com.neogen.commons.bean;

import java.io.Serializable;

public class Ordenacion implements Serializable {

	private static final long serialVersionUID = -8788346856184151614L;

	private String columna;
	
	
	public Ordenacion() {
		super();
	}


	public String getColumna() {
		return columna;
	}


	public void setColumna(String columna) {
		this.columna = columna;
	}
	
	
}
