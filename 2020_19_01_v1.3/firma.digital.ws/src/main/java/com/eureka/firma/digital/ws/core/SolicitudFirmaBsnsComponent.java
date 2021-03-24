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
    
	@Autowired private IStreamService        streamService;
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
            
            sf.folio = streamService.obtenerFolioArchivo(); 
                        
            registroService.registraSolicitud( usuario, sf.folio, sf.archivo);
            
			return service.getRespuesta( resultado);
				
		} catch ( Exception ex) {
			Log.error( ex);
            
			service.eliminarDirectorioUpload( sf);
			
            return service.getRespuesta( 
				ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage())
			);
            
		}		
    }
    
}
