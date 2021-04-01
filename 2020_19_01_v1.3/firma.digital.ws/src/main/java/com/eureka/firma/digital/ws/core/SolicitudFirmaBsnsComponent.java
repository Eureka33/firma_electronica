package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.RespuestaSolicitud;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.Solicitud;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.enums.EnumAccionSolicitud;
import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import com.meve.ofspapel.firma.digital.core.service.RegistroService;
import com.meve.ofspapel.firma.digital.core.service.SolicitudService;
import com.meve.ofspapel.firma.digital.core.service.UsuarioService;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import mx.neogen.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SolicitudFirmaBsnsComponent  {
    
    @Autowired private IConfiguracionService configService;
    @Autowired private UsuarioService     usuarioService;
    @Autowired private RegistroService   registroService;
    @Autowired private SolicitudService solicitudService;
    
	@Autowired private IStreamService      streamService;
    @Autowired private FirmaBaseBsnsComponent    service;
    
    
	/**
	 * 	Firma el archivo recibido como argumento y devuelve el archivo firmado
	 */
	public RespuestaSolicitud registrarSolicitud( final SolicitudFirma solicitud, String emailDestinatario) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = service.validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
            resultado = service.validarArchivoDatos( solicitud);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
			resultado = service.descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return getRespuesta( resultado); }
            
   			resultado = service.obtenerPathDescarga();
			if ( resultado.isError()) { return getRespuesta( resultado); }
			String pathDescarga = (String) resultado.getResultado();

            resultado = service.descargarArchivoDatos( sf, pathDescarga);
			if ( resultado.isError()) { return getRespuesta( resultado); }
			
			resultado = service.validarCertificado( sf);
			if ( resultado.isError()) { return getRespuesta( resultado); }
            
            Usuario usuario = usuarioService.obtenerUsuario( sf.firma.getRfc(), sf.firma.getTitular());
            
            sf.folio = streamService.obtenerFolioArchivo();
            
            sf.firma.setUrlDescarga( generarDownloadURL( sf));
            
            RegistroSolicitud entidad = registroService.registraSolicitud(
                usuario, sf.folio, sf.archivo.getAbsolutePath(), emailDestinatario
            );
            
			return getRespuesta( resultado, entidad, sf.firma);
            
		} catch ( Exception ex) {
			Log.error( ex);
            
			service.eliminarDirectorioUpload( sf);
			
            return getRespuesta( 
				ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage())
			);   
		}
    }
    
    public String generarDownloadURL( SessionFirma sf) throws UnsupportedEncodingException, FileNotFoundException {
        if( sf.archivo == null ) {
            return null;
        }
        
        final String extension = sf.solicitud.getArchivoDatos().getExtension();
        if( !"pdf".equalsIgnoreCase( extension)) {
            return null;
        }
        
        String serverName    = configService.getPropiedad( "url.server.name");
        String webAppContext = configService.getPropiedad( "path.app.context");
        String nombreArchivo = sf.archivo.getName();
                
        return streamService.generarURLSolicitud( serverName, webAppContext, sf.folio, nombreArchivo);
    }
    
    public EnumEstatusSolicitud registraVisitaLink( String folio, String nombre) {
        final Integer idSolicitud = solicitudService.obtenerIdSolicitud( folio, nombre);
        return solicitudService.actualizaSolicitud( idSolicitud, EnumAccionSolicitud.LINK_VISITADO);
    }
    
    public void registraEnvioSolicitud( Integer idSolicitud) {
        solicitudService.actualizaSolicitud( idSolicitud, EnumAccionSolicitud.CORREO_ENVIADO);
    }
        
    private RespuestaSolicitud getRespuesta( Resultado<?> resultado) {
		final RespuestaSolicitud rf = new RespuestaSolicitud();
		
		rf.setCodigo(   resultado.getCodigo());
		rf.setMensaje( resultado.getMensaje());
		
		return rf;
	}
    
    private RespuestaSolicitud getRespuesta( Resultado<?> resultado, RegistroSolicitud entidad, Firma firma) {
		final RespuestaSolicitud rf = new RespuestaSolicitud();
		
		rf.setCodigo(   resultado.getCodigo());
		rf.setMensaje( resultado.getMensaje());
		
        rf.setSolicitud( entidadToItem( entidad));
        rf.setFirma( firma);
        
        DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm");
        firma.setFecha( formatter.format( entidad.getFechaHora()));
        
		return rf;
	}
    
    private Solicitud entidadToItem( RegistroSolicitud entidad) {
        Solicitud item = new Solicitud();
        
        item.setId( entidad.getId());
        item.setPathArchivo( entidad.getNombre());
        
        return item;
    }
    
}
