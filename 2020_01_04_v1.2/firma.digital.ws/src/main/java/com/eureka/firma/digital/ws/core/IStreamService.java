package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import java.io.File;

import javax.activation.DataHandler;

import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public interface IStreamService {

	Resultado<Void> guardarArchivoDatos( 	File archivo, DataHandler handler);
    
    String obtenerFolioArchivo();
    
    String obtenerPathDeposito( String pathRepositorio, String folio, String nombre);
    
    String generarURLDescarga( String serverName, String webAppContext, String folio, String nombre) throws UnsupportedEncodingException;
    
    void firmarDocumento( String pathDeposito, String baseDownloadURL, SessionFirma sf, Firma firma, String organizacion) throws FileNotFoundException;
	
	void eliminarDirectorio( 	File directorio);
	
	String streamToBase64( 		InfoArchivo infoArchivo);

}
