package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.RespuestaSolicitud;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.Solicitud;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.enums.EnumAccionSolicitud;
import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import com.meve.ofspapel.firma.digital.core.service.RegistroService;
import com.meve.ofspapel.firma.digital.core.service.SolicitudService;
import com.meve.ofspapel.firma.digital.core.service.UsuarioService;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import mx.eureka.firma.digital.bean.BeanInfoDocumento;
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
            
            sf.folio = obtenerFolioUpload( sf.archivo);
            
            sf.firma.setUrlDescarga( generarDownloadURL( sf));
            
            RegistroSolicitud entidad = registroService.registraSolicitud(
                usuario, sf.folio, sf.archivo.getName(), emailDestinatario
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
    
    public RespuestaFirma firmarArchivo( final SolicitudFirma solicitud, File archivo, BeanInfoDocumento infoDocumento) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = service.validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
			
			resultado = service.descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
            
			resultado = service.validarCertificado( sf);
			if ( resultado.isError()) { return service.getRespuesta( resultado); }
 
            Usuario usuario = usuarioService.obtenerUsuario( sf.firma.getRfc(), sf.firma.getTitular());
            
            sf.archivo = archivo;
            
            resultado = service.generaFirma( sf);
            
            sf.folio = streamService.obtenerFolioArchivo(); 
            
            service.actualizarArchivo( sf, (Resultado<Firma>) resultado);
            
            ArchivoDepositado documentoFirmado = registroService.registraDocumento( usuario, sf.firma.getFecha(), sf.folio, sf.archivo.getName());
            
            registraAtencionSolicitud( documentoFirmado, infoDocumento);
            
            // el archivo es eliminado una vez firmado
			service.eliminarDirectorioUpload( sf);
            
            return service.getRespuesta( resultado);
				
		} catch ( Exception ex) {
			ex.printStackTrace();
			
            return service.getRespuesta( 
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
        if ( idSolicitud == null) {
            return null;
        }
        return solicitudService.actualizaSolicitud( idSolicitud, EnumAccionSolicitud.LINK_VISITADO);
    }
    
    public void registraAtencionSolicitud( ArchivoDepositado documentoFirmado, BeanInfoDocumento infoDocumento) {
        final Integer idSolicitud = solicitudService.obtenerIdSolicitud( infoDocumento.getFolio(), infoDocumento.getNombre());
        
        solicitudService.registraAtencionSolicitud( documentoFirmado.getId(), idSolicitud);
        solicitudService.actualizaSolicitud( idSolicitud, EnumAccionSolicitud.DOCUMENTO_FIRMADO);
    }
    
    public void registraEnvioSolicitud( Integer idSolicitud) {
        solicitudService.actualizaSolicitud( idSolicitud, EnumAccionSolicitud.CORREO_ENVIADO);
    }
    
    private String obtenerFolioUpload( File file) {
        return file.getParentFile().getName();
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
