package mx.eureka.firma.digital.bean;

public class BeanInfoDocumento {
    
    private String folio;
    private String nombre;

    
    public BeanInfoDocumento() {
        super();
    }

    
    public String getFolio() {
        return folio;
    }

    void setFolio(String folio) {
        this.folio = folio;
    }

    public String getNombre() {
        return nombre;
    }

    void setNombre(String nombre) {
        this.nombre = nombre;
    }    
    
}
