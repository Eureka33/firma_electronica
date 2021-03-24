package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.service.UsuarioService;
import mx.neogen.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutenticacionBsnsComponent {
    
    @Autowired private UsuarioService usuarioService;
	@Autowired private FirmaBaseBsnsComponent  service;
         
    public Resultado<?> autenticarUsuario( final SolicitudFirma solicitud) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = service.validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return resultado; }
			
            resultado = service.descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return resultado; }
        
            resultado = service.validarCertificado( sf);
            if ( resultado.isError()) { return resultado; }
        
            return service.getResultado( resultado, usuarioService.obtenerUsuario( sf.firma.getRfc(), sf.firma.getTitular()));
			
        } catch ( Exception ex) {
			Log.error( ex);
            return ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage());
			
        } finally {
			service.eliminarDirectorioUpload( sf);
            
		}
	}
    
}
