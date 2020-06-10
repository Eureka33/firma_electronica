package mx.eureka.firma.digital.bean;

import java.io.File;

public class ArchivoDepositado {

    private File pathDeposito;
    private String urlDescarga;
    
    
    public ArchivoDepositado() {
        this( null, null);
    }
    
    public ArchivoDepositado( String pathDeposito, String urlDescarga) {
        super();
        
        this.pathDeposito = new File( pathDeposito);
        this.urlDescarga  = urlDescarga;
    }

    
    public File getPathDeposito() {
        return pathDeposito;
    }

    public void setPathDeposito( String pathDeposito) {
        this.pathDeposito = new File( pathDeposito);
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }
    
}
