package mx.eureka.firma.digital.bean;

import java.io.Serializable;

public class BeanInfoDocumento implements Serializable {
    
    private String folio;
    private String nombre;

    
    public BeanInfoDocumento() {
        super();
    }

    
    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }    
    
}
