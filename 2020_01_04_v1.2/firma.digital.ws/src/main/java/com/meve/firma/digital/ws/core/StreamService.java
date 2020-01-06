package com.meve.firma.digital.ws.core;

import com.meve.firma.digital.ws.bean.Firma;
import com.meve.firma.digital.ws.bean.InfoArchivo;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import javax.activation.DataHandler;

import org.springframework.stereotype.Service;

import com.meve.firma.digital.ws.bean.Resultado;
import com.meve.firma.digital.ws.bean.SessionFirma;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.UUID;
import mx.neogen.log.Log;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

@Service( "streamService")
public class StreamService implements IStreamService {

	@Autowired private PDFSignAppender appender;
    
    @Override
	public Resultado<Void> guardarArchivoDatos(File archivo, DataHandler handler) {

		InputStream inputStream = null;
		
		try {
			inputStream = handler.getInputStream();
			guardarStream(inputStream, archivo);
			
			return ResultadoEnum.OK.getResultado("");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_SAVING_FILE.getResultado( ex.getMessage());
							
		} finally {
			try { inputStream.close(); } catch( Exception ex) { ex.printStackTrace(); }
		}
	
	}

    @Override
    public String obtenerFolioArchivo() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
    @Override
    public String obtenerPathDeposito( String pathRepositorio, String folio, String nombre) {
        return pathRepositorio + File.separator + folio + File.separator + nombre;
    }
    
    public String generarURLDescarga( String serverName, String webAppContext, String folio, String nombre) {
		return "{server}{rutaBase}/validacionDocumento?folio={folio}&nombre={nombre}"
			.replace(   "{server}", serverName)
			.replace( "{rutaBase}", webAppContext)
			.replace(    "{folio}", folio)
			.replace(   "{nombre}", nombre);
	}
    
    @Override
    public void firmarDocumento( String pathDeposito, String urlDescarga, SessionFirma sf, Firma firma) {
                
        final String source = sf.archivo.getAbsolutePath();
        final File pathDirDeposito = new File( pathDeposito).getParentFile();
        
        firma.setUrlDescarga( urlDescarga);
        
        pathDirDeposito.mkdirs(); // crea directorio deposito
               
        appender.firmarPDF( source, pathDeposito, urlDescarga, firma);
    }
    
    @Override
	public void eliminarDirectorio(File directorio) {
		if (directorio == null) {
			return;
		}
		
		try {
			for ( File archivo : directorio.listFiles()) {
				archivo.delete();
			}
			directorio.delete();
		
		} catch( Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
    @Override
	public String streamToBase64(InfoArchivo infoArchivo) {
		InputStream inputStream = null;
		ByteArrayOutputStream out = null;
		
		try {
			inputStream =  infoArchivo.getHandler().getInputStream();
			byte[] buffer = new byte[1024];
			out = new ByteArrayOutputStream( inputStream.available());
			
			int length = 0;
			
			while( (length = inputStream.read( buffer)) != -1) {
				out.write( buffer, 0, length);
			}
			
			out.flush();
			
			return Base64.encodeBase64String( out.toByteArray());
		} catch( Exception ex) {
			ex.printStackTrace();
			
			return null;
			
		} finally {
			try { inputStream.close(); 	} catch( Exception ex) { ex.printStackTrace(); }
			try { out.close(); 			} catch( Exception ex) { ex.printStackTrace(); }
		}
	}
	
	
	private void guardarStream( InputStream inputStream, File target) throws Exception {
		
		Log.info( "Saving archivo: " + target);
		
		OutputStream out = null;
		byte[] buffer = new byte[4*1024];
		
		try {
			out = new BufferedOutputStream( new FileOutputStream( target));
			int length = 0;
			
			while( (length = inputStream.read( buffer)) != -1) {
				out.write( buffer, 0, length);
			}
			
			out.flush();
			
		} finally {
			out.close();
			buffer = null;
		}
	}
    
}
