package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import com.meve.ofspapel.firma.digital.core.service.RegistroService;
import com.meve.ofspapel.firma.digital.core.service.UsuarioService;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import mx.neogen.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolicitudFirmaBsnsComponent  {
    
    @Autowired private UsuarioService usuarioService;
    @Autowired private RegistroService registroService;
    
	@Autowired private IConfiguracionService configService;
	@Autowired private IStreamService        streamService;
	@Autowired private IFirmaDigitalService   firmaService;
	
    @Autowired private FirmaBaseBsnsComponent  service;
    
	/**
	 * 	Firma el archivo recibido como argumento y devuelve el archivo firmado
	 */
	public RespuestaFirma registrarArchivo( final SolicitudFirma solicitud) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = service.validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
			
            resultado = service.validarArchivoDatos( solicitud);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
			
			resultado = service.descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
            
   			resultado = service.obtenerPathDescarga();
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
			String pathDescarga = (String) resultado.getResultado();

            resultado = service.descargarArchivoDatos( sf, pathDescarga);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
			
			resultado = service.validarCertificado( sf);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
 
            Usuario usuario = usuarioService.obtenerUsuario( sf.firma.getRfc(), sf.firma.getTitular());
            
            //resultado = generaFirma( sf);
            
            sf.folio = streamService.obtenerFolioArchivo(); 
            
            //actualizarArchivo( sf, (Resultado<Firma>) resultado);
            
            registroService.registraDocumento( usuario, sf.firma.getFecha(), sf.folio, sf.archivo.getName());
            
			return service.getRespuesta( resultado);
				
		} catch ( Exception ex) {
			ex.printStackTrace();
			
            return service.getRespuesta( 
				ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage())
			);
			
		} finally {
			eliminarInfo( sf);
		}		
    }
    
    protected String actualizarArchivo( SessionFirma sf, Resultado<Firma> resultado) throws UnsupportedEncodingException, FileNotFoundException {
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
        String organizacion  = configService.getPropiedad( "string.organizacion.nombre");
        
        String nombreArchivo = sf.archivo.getName();
        
        final String pathDeposito = streamService.obtenerPathDeposito( pathRepositorio, sf.folio, nombreArchivo);
        
        final String downloadURL  = streamService.generarURLDescarga( serverName, webAppContext, sf.folio, nombreArchivo);
      
        streamService.firmarDocumento( pathDeposito, downloadURL, sf, resultado.getResultado(), organizacion);
        
        return pathDeposito;
    }

    protected void eliminarInfo( final SessionFirma sf) {
		// elimina unicamente los archivos confidenciales
        Log.info( "directorio upload: " + sf.archivo.getParentFile().getAbsolutePath());
        
        /*
        if ( sf.archivo != null) {
			streamService.eliminarDirectorio( sf.archivo.getParentFile());
		}
        */
	}
        
}
