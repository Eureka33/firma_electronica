package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.UUID;
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
			resultado = validarInfo( solicitud);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
			resultado = obtenerPathDescarga();
			if ( resultado.isError()) { return getRespuesta( resultado); }
			String pathDescarga = (String) resultado.getResultado();
					
			resultado = descargarInfo( sf, pathDescarga);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
			resultado = validarCertificado( sf);
			if ( resultado.isError()) { return getRespuesta( resultado); }
        
            resultado = generaFirma( sf);
            
            actualizarArchivo( sf,  (Resultado<Firma>) resultado);
                        
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
	
	
	Resultado<?> validarCertificado( SessionFirma sf) {
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
	
	Resultado<Firma> generaFirma( SessionFirma sf) {
		Resultado<Firma> resultado = firmaService.generaFirma( sf.privateKey, sf.firma, sf.archivo, sf.solicitud.getCadena());
		
		if ( resultado.isError()) {
			return resultado;	
		}
		
		return resultado;
	}


	Resultado<Void> validarInfo( SolicitudFirma solicitud) {
		
		Resultado<Void> resultado = validarInfoConfidencial( solicitud.getInfoConfidencial());
		if ( resultado.isError()) {
			return resultado;	
		}
	
		resultado = validarArchivoDatos( solicitud.getArchivoDatos());
		
        if ( resultado.isError()) {
			// checa si existe la entrada de cadena de texto
			final String cadena = solicitud.getCadena();
			if( cadena != null && !(cadena.trim().length() == 0)) {
				resultado = ResultadoEnum.OK.getResultado( "cadena validada con éxito");
			}
			
		}
			
		// otras validaciones
		return resultado;
	}

	Resultado<Void> validarInfoConfidencial( InfoConfidencial info) {
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
	
	Resultado<Void> validarArchivoDatos( InfoArchivo archivoDatos) {
		return validarInfoArchivo( archivoDatos, "Archivo a firmar");
	}
	
	Resultado<Void> validarInfoArchivo(	final InfoArchivo infoArchivo, String entidad) {
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
	
	/**
	 * 	Crea directorio temporal y descarga la información de archivos
	 */
	Resultado<Void> descargarInfo( final SessionFirma sf, final String directoriBasePath) {

		InfoArchivo archivo 	= sf.solicitud.getArchivoDatos();
		InfoArchivo certificado = sf.solicitud.getInfoConfidencial().getCertificado();
		InfoArchivo archivoLlave= sf.solicitud.getInfoConfidencial().getArchivoLlave();
		
		try {
			sf.certificadoBase64  	= streamService.streamToBase64( certificado);
			sf.archivoLlaveBase64	= streamService.streamToBase64( archivoLlave);
			
			if ( archivo != null) {
				File directorio = new File( directoriBasePath + File.separator + UUID.randomUUID().toString() );
				directorio.mkdir();
				
				sf.archivo    = new File( directorio, archivo.getNombre() + "." + archivo.getExtension());
				return streamService.guardarArchivoDatos( sf.archivo, archivo.getHandler());
			}
			
			return ResultadoEnum.OK.getResultado( "");
			
		} catch (Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_SAVING_FILE.getResultado( ex.getMessage()); 
		
		}		
	}
	
	
	void eliminarInfo( final SessionFirma sf) {
		if ( sf.archivo != null) {
			streamService.eliminarDirectorio( sf.archivo.getParentFile());
		}
	}
	
	
	RespuestaFirma getRespuesta( Resultado<?> resultado) {
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
	
	Resultado<String> obtenerPathDescarga() {
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
    
    void actualizarArchivo( SessionFirma sf, Resultado<Firma> resultado) throws UnsupportedEncodingException {
        if( sf.archivo == null ) {
            return;
        }
        
        final String extension = sf.solicitud.getArchivoDatos().getExtension();
        if( !"pdf".equalsIgnoreCase( extension)) {
            return;
        }
        
        String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        
        String serverName    = configService.getPropiedad( "url.server.name");
        String webAppContext = configService.getPropiedad( "path.app.context");
        String nombreArchivo = sf.archivo.getName();
        
        final String folioArchivo = streamService.obtenerFolioArchivo(); 
        final String pathDeposito = streamService.obtenerPathDeposito( pathRepositorio, folioArchivo, nombreArchivo);
        
        final String downloadURL  = streamService.generarURLDescarga( serverName, webAppContext, folioArchivo, nombreArchivo);
      
        streamService.firmarDocumento( pathDeposito, downloadURL, sf, resultado.getResultado());
    }

}
