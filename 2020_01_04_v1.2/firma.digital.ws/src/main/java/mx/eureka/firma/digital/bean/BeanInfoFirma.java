package mx.eureka.firma.digital.bean;

import java.io.InputStream;


public class BeanInfoFirma {
    
    private String nombreDocumento;
    
    private InputStream contenidoDocumento;
    private InputStream certificado;
    private InputStream llavePrivada;
    
    private String password;
    private String correo;

    
    public BeanInfoFirma() {
        super();
    }

    
    public String getNombreDocumento() {
        return nombreDocumento;
    }

    public void setNombreDocumento( String value) {
        this.nombreDocumento = value;
    }
    
    public InputStream getContenidoDocumento() {
        return contenidoDocumento;
    }

    public void setContenidoDocumento(InputStream value) {
        this.contenidoDocumento = value;
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
