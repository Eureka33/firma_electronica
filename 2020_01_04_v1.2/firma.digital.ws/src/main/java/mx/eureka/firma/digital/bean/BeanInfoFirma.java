package mx.eureka.firma.digital.bean;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class BeanInfoFirma {
    
    private List<InfoArchivo> archivos;
    private InputStream certificado;
    private InputStream llavePrivada;
    
    private String password;
    private String correo;

    
    public BeanInfoFirma() {
        super();
    }

    
    public List<InfoArchivo> getArchivos() {
        return archivos;
    }
    
    public void addArchivo( InfoArchivo archivo) {
        if( archivos == null) {
            archivos = new ArrayList<>();
        }
        
        archivos.add( archivo);
    }
    
    public InputStream getCertificado() {
        return certificado;
    }

    public void setCertificado(InputStream certificado) {
        this.certificado = certificado;
    }

    public InputStream getLlavePrivada() {
        return llavePrivada;
    }

    public void setLlavePrivada(InputStream llavePrivada) {
        this.llavePrivada = llavePrivada;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
   
        
}
