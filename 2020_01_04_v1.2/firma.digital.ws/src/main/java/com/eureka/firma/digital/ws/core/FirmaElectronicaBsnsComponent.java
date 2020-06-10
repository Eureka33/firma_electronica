package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.RespuestaFirmaMasiva;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import mx.eureka.firma.digital.bean.ArchivoDepositado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirmaElectronicaBsnsComponent {

	@Autowired private IConfiguracionService configService;
	@Autowired private IStreamService        streamService;
	@Autowired private IFirmaDigitalService   firmaService;
	
	/**
	 * 	Firma el archivo recibido como argumento y devuelve el archivo firmado
	 */
	public RespuestaFirma firmarArchivo(	final SolicitudFirma solicitud) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
            resultado = validarArchivoDatos( solicitud);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
			resultado = descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return getRespuesta( resultado); }
            
   			resultado = obtenerPathDescarga();
			if ( resultado.isError()) { return getRespuesta( resultado); }
			String pathDescarga = (String) resultado.getResultado();

            resultado = descargarArchivoDatos( sf, pathDescarga);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
			resultado = validarCertificado( sf);
			if ( resultado.isError()) { return getRespuesta( resultado); }
        
            resultado = generaFirma( sf);
            
            sf.folio = streamService.obtenerFolioArchivo(); 
            
            actualizarArchivo( sf, (Resultado<Firma>) resultado);
            
			return getRespuesta( resultado);
				
		} catch ( Exception ex) {
			ex.printStackTrace();
			
            return getRespuesta( 
				ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage())
			);
			
		} finally {
			eliminarInfo( sf);
		}		
	}
    
    public RespuestaFirmaMasiva firmarArchivos( final SolicitudFirma solicitud, List<InfoArchivo> archivos) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return getRespuestaMasiva( resultado, null); }
			
            resultado = descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return getRespuestaMasiva( resultado, null); }
        
            resultado = validarCertificado( sf);
			if ( resultado.isError()) { return getRespuestaMasiva( resultado, null); }
        
            resultado = obtenerPathDescarga();
			if ( resultado.isError()) { return getRespuestaMasiva( resultado, null); }
			String pathDescarga = (String) resultado.getResultado();

            List<String> paths = new ArrayList<>();
            sf.folio = streamService.obtenerFolioArchivo(); 
            
            for ( InfoArchivo archivo : archivos) {
                solicitud.setArchivoDatos(  archivo);
                
                resultado = validarArchivoDatos( solicitud);
                if ( resultado.isError()) { break; }
		
                resultado = descargarArchivoDatos( sf, pathDescarga);
                if ( resultado.isError()) { break; }
                
                resultado = generaFirma( sf);
                if ( resultado.isError()) { break; } 
                
                paths.add( actualizarArchivo( sf, (Resultado<Firma>) resultado));
            }
            
            return getRespuestaMasiva( resultado, paths);
        
        } catch ( Exception ex) {
			ex.printStackTrace();
			
            return getRespuestaMasiva( 
				ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage()), null
			);
			
        } finally {
			eliminarInfo( sf);
            
		}
	}
	
	private Resultado<?> validarCertificado( SessionFirma sf) {
		Resultado<PublicKey> rPubKey = firmaService.validaCertificado( sf.certificadoBase64, sf.firma);
		if ( rPubKey.isError()) {
			return rPubKey;	
		}
		sf.publicKey = rPubKey.getResultado();
		
		Resultado<PrivateKey>  rPrvKey = firmaService.validaLlavePrivada( 
			sf.archivoLlaveBase64, sf.solicitud.getInfoConfidencial().getPasswordLlave()
		);
		
		if ( rPrvKey.isError()) {
			return rPrvKey;
		}
		sf.privateKey = rPrvKey.getResultado();
		
		Resultado<Void> resultado = firmaService.validaPareja( sf.privateKey, sf.publicKey);		
		if ( resultado.isError()) {
			return resultado;
		}
		
		return resultado;

	}
	
	private Resultado<Firma> generaFirma( SessionFirma sf) {
		Resultado<Firma> resultado = firmaService.generaFirma( sf.privateKey, sf.firma, sf.archivo, sf.solicitud.getCadena());
		
		if ( resultado.isError()) {
			return resultado;	
		}
		
		return resultado;
	}


	private Resultado<Void> validarArchivoDatos( SolicitudFirma solicitud) {
        Resultado<Void> resultado = validarArchivoDatos( solicitud.getArchivoDatos());
		
        if ( resultado.isError()) {
			// checa si existe la entrada de cadena de texto
			final String cadena = solicitud.getCadena();
			if( cadena != null && !(cadena.trim().length() == 0)) {
				resultado = ResultadoEnum.OK.getResultado( "cadena validada con éxito");
			}
		}
		
		return resultado;
	}

	private Resultado<Void> validarInfoConfidencial( SolicitudFirma solicitud) {
		
        final InfoConfidencial info = solicitud.getInfoConfidencial();
        
        if ( info == null) {
			return ResultadoEnum.INVOCATION_ERROR.getResultado(
				"Falta elemento de información confidencial"
			);
		}
		
		String campo = info.getPasswordLlave();
		if ( campo == null || campo.trim().length() == 0) { 
			return ResultadoEnum.INVOCATION_ERROR.getResultado(
				"Falta valor de contraseña de llave privada"
			);
		} 
		Resultado<Void> resultado = validarInfoArchivo( info.getArchivoLlave(), "Llave privada");
		if ( resultado.isError()) {
			return resultado;
		}
		
		resultado = validarInfoArchivo( info.getCertificado(), "Certificado público");
		if( resultado.isError()) {
			return resultado;
		}
		
		return resultado;
		
	}
	
	private Resultado<Void> validarArchivoDatos( InfoArchivo archivoDatos) {
		return validarInfoArchivo( archivoDatos, "Archivo a firmar");
	}
	
	private Resultado<Void> validarInfoArchivo(	final InfoArchivo infoArchivo, String entidad) {
		if ( infoArchivo == null || infoArchivo.getHandler() == null) {
			return ResultadoEnum.INVOCATION_ERROR.getResultado(
				"Elemento: " + entidad + " : Falta el contenido del archivo"
			);
		}
		
		String campo = infoArchivo.getNombre();
		if ( campo == null || campo.trim().length() == 0) {
			return ResultadoEnum.INVOCATION_ERROR.getResultado(
				"Elemento: " + entidad + " : Falta el nombre del archivo"
			);
		}
		
		campo = infoArchivo.getExtension();
		if ( campo == null || campo.trim().length() == 0 ) {
			return  ResultadoEnum.INVOCATION_ERROR.getResultado(
				"Elemento: " + entidad + " : Falta la extensión del archivo"
			);
		}	
		
		return ResultadoEnum.OK.getResultado( entidad + " validada con éxito");
	}
	
    
	private Resultado<Void> descargarArchivoDatos( final SessionFirma sf, final String directoriBasePath) {
        
		final InfoArchivo archivo = sf.solicitud.getArchivoDatos();
		
		try {
			
			if ( archivo != null) {
				File directorio = new File( directoriBasePath + File.separator + UUID.randomUUID().toString() );
				directorio.mkdir();
				
				sf.archivo = new File( directorio, archivo.getNombre() + "." + archivo.getExtension());
				return streamService.guardarArchivoDatos( sf.archivo, archivo.getHandler());
			}
			
			return ResultadoEnum.OK.getResultado( "");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_SAVING_FILE.getResultado( ex.getMessage()); 
		}
	}
    
    private Resultado<Void> descargarInfoConfidencial( final SessionFirma sf) {

		InfoArchivo certificado = sf.solicitud.getInfoConfidencial().getCertificado();
		InfoArchivo archivoLlave= sf.solicitud.getInfoConfidencial().getArchivoLlave();
		
		try {
			sf.certificadoBase64  = streamService.streamToBase64( certificado);
			sf.archivoLlaveBase64 = streamService.streamToBase64( archivoLlave);
				
			return ResultadoEnum.OK.getResultado( "");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_SAVING_FILE.getResultado( ex.getMessage()); 
		}
	}		
	
	private void eliminarInfo( final SessionFirma sf) {
		if ( sf.archivo != null) {
			streamService.eliminarDirectorio( sf.archivo.getParentFile());
		}
	}
	
	
	private RespuestaFirma getRespuesta( Resultado<?> resultado) {
		final RespuestaFirma rf = new RespuestaFirma();
		
		rf.setCodigo( resultado.getCodigo());
		rf.setMensaje( resultado.getMensaje());
		
		if ( resultado.getResultado() != null) {
			rf.setFirma( (Firma) resultado.getResultado());
		} else {
			rf.setFirma( new Firma());
		}
	
		return rf;
	}
    
    private RespuestaFirmaMasiva getRespuestaMasiva( Resultado<?> resultado, List<String> paths) {
		final RespuestaFirmaMasiva rf = new RespuestaFirmaMasiva();
		
		rf.setCodigo( resultado.getCodigo());
		rf.setMensaje( resultado.getMensaje());
		
		if ( resultado.getResultado() != null) {
			rf.setFirma( (Firma) resultado.getResultado());
		} else {
			rf.setFirma( new Firma());
		}
        
        rf.setPaths( paths);
        
		return rf;
	}
	
	private Resultado<String> obtenerPathDescarga() {
		final String pathDescarga = configService.getPropiedad( "path.directorio.descarga");
		
		if ( pathDescarga == null) {
			return ResultadoEnum.ERROR_INTERNAL_CONFIG.getResultado(  
				"Trayecto de descarga no configurado: 'path.directorio.descarga'"
			);
		}
		Resultado<String> resultado = ResultadoEnum.OK.getResultado( "");
		resultado.setResultado( pathDescarga);
		
		return resultado;
	}
    
    private String actualizarArchivo( SessionFirma sf, Resultado<Firma> resultado) throws UnsupportedEncodingException, FileNotFoundException {
        if( sf.archivo == null ) {
            return null;
        }
        
        final String extension = sf.solicitud.getArchivoDatos().getExtension();
        if( !"pdf".equalsIgnoreCase( extension)) {
            return null;
        }
        
        String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        
        String serverName    = configService.getPropiedad( "url.server.name");
        String webAppContext = configService.getPropiedad( "path.app.context");
        String nombreArchivo = sf.archivo.getName();
        
        final String pathDeposito = streamService.obtenerPathDeposito( pathRepositorio, sf.folio, nombreArchivo);
        
        final String downloadURL  = streamService.generarURLDescarga( serverName, webAppContext, sf.folio, nombreArchivo);
      
        streamService.firmarDocumento( pathDeposito, downloadURL, sf, resultado.getResultado(), sf.solicitud.getOrganizacion());
        
        return pathDeposito;
    }
    
    
    public ArchivoDepositado generarDestinoZip() {
        
        final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        
        final String serverName    = configService.getPropiedad( "url.server.name");
        final String webAppContext = configService.getPropiedad( "path.app.context");
        final String nombreArchivo = "firma_masiva.zip";
        
        final String folioArchivo = streamService.obtenerFolioArchivo(); 
        final String pathDeposito = streamService.obtenerPathDeposito( pathRepositorio, folioArchivo, nombreArchivo);
        
        try {
            final String downloadURL  = streamService.generarURLDescarga( serverName, webAppContext, folioArchivo, nombreArchivo);
            return new ArchivoDepositado( pathDeposito, downloadURL);
        
        } catch( Exception ex) {
            throw new RuntimeException( ex); 
        }
    }

    public String obtenerRealPath( String urlDescarga) {
        final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        return streamService.obtenerPathDeposito( pathRepositorio, urlDescarga);
    }
    
}
