package mx.eureka.firma.digital.bean;

import java.io.InputStream;

public class InfoArchivo {
    
    private String nombre;
    private String path;
    private long longitud;
    private InputStream contenido;

    
    public InfoArchivo() {
        super();
    }

    
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getLongitud() {
        return longitud;
    }

    public void setLongitud( long longitud) {
        this.longitud = longitud;
    }

    public InputStream getContenido() {
        return contenido;
    }

    public void setContenido(InputStream contenido) {
        this.contenido = contenido;
    }  
    
}
