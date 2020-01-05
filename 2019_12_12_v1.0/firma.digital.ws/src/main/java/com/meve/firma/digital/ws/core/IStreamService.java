package com.meve.firma.digital.ws.core;

import com.meve.firma.digital.ws.bean.Firma;
import java.io.File;

import javax.activation.DataHandler;

import com.meve.firma.digital.ws.bean.InfoArchivo;
import com.meve.firma.digital.ws.bean.Resultado;
import com.meve.firma.digital.ws.bean.SessionFirma;

public interface IStreamService {

	Resultado<Void> guardarArchivoDatos( 	File archivo, DataHandler handler);
    
    String obtenerPathDeposito( String pathRepositorio, String nombre, String firma);
    
    void firmarDocumento( String pathDeposito, String baseDownloadURL, SessionFirma sf, Firma firma);
	
	void eliminarDirectorio( 	File directorio);
	
	String streamToBase64( 		InfoArchivo infoArchivo);
}
