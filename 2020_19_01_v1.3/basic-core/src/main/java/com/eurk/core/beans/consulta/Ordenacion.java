package com.eurk.core.beans.consulta;

import java.io.Serializable;
import com.eurk.core.beans.consulta.enums.EnumOrder;

public class Ordenacion implements Serializable {

	private static final long serialVersionUID = -8788346856184151614L;

	private String campo;
	private String direccion;
	
	
	public Ordenacion() {
		super();
	}

	public String getCampo() {
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public String getDireccion() {
		return direccion;
	}
    
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

    public String generarClausula( String campo) {
        return campo + " " + EnumOrder.valueOf( direccion.toUpperCase());
    }
}
