package com.meve.ofspapel.firma.digital.core.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import mx.com.neogen.commons.bean.Resultado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meve.ofspapel.firma.digital.core.entidades.VigenciaCertificado;
import com.meve.ofspapel.firma.digital.core.mappers.VigenciaCertificadoMapper;

@Service( "vigenciaCertificadoService")
public class VigenciaCertificadoService implements IVigenciaCertificadoService {

	@Autowired
	private VigenciaCertificadoMapper vigenciaDAO;
		
	@Transactional( readOnly=true)
	public Resultado<VigenciaCertificado> encuentraCertificado(
														final String serial, 
														final String rfc) {
		final Resultado<VigenciaCertificado> resultado = new Resultado<VigenciaCertificado>();
	    resultado.setResultado( vigenciaDAO.findVigenciaCert( serial, rfc));
	    
	    return resultado;
	}
	

	@Transactional( readOnly=true)
	public Resultado<VigenciaCertificado> encuentraCertificado(
														final String serial) {
		final Resultado<VigenciaCertificado> resultado = new Resultado<VigenciaCertificado>();
	    resultado.setResultado( vigenciaDAO.findVigenciaCertBySerial( serial));
	    
	    return resultado;
	}
	
	public Resultado<Boolean> updateFromFile( 	final String filePath) {
		
		BufferedReader lector = null;
		final DateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
		
		try {
			lector = new BufferedReader( new FileReader( filePath));
			
			String linea = lector.readLine();   // first line of headers 
			
			while( (linea = lector.readLine()) != null) {
				
				VigenciaCertificado vigenciaArchivo = getBean( linea, formatter);
				
				VigenciaCertificado vigenciaDB = 
				vigenciaDAO.findVigenciaCert( 
					vigenciaArchivo.getSerial(), vigenciaArchivo.getRfc()
				);
				
				if ( vigenciaDB != null){
					vigenciaDAO.actualizaVigenciaCertificado( vigenciaArchivo);
				} else {
					vigenciaDAO.insertaVigenciaCertificado( vigenciaArchivo);
				}
				
			}
		
			return Resultado.crearInstancia( true);
			
		} catch ( Exception ex) {
			Resultado<Boolean> resultado = new Resultado<Boolean>();
			resultado.setError( "Error al sincronizar archivo de vigencia de certificados:" + ex.getMessage());
			
			return resultado;

		} finally {
			try { lector.close(); } catch( Exception ex) { ex.printStackTrace(); }
		}
	}
	
	private VigenciaCertificado getBean( String lineaCSD, DateFormat formatter) throws Exception {
		final VigenciaCertificado bean = new VigenciaCertificado();
		final String[] registro = lineaCSD.split( "[|]");
		
		bean.setSerial( 	registro[0]);
		
		bean.setFechaIni( 	formatter.parse( registro[1]));
		bean.setFechaFin(   formatter.parse( registro[2]));
		
		bean.setRfc(    registro[3]);
		bean.setEstatus(  registro[4].charAt(0));
		
		return bean;
		
	}
	
}
