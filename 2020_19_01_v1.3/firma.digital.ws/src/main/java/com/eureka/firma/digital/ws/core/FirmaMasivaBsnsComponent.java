package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.RespuestaFirmaMasiva;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SessionFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import com.meve.ofspapel.firma.digital.core.service.RegistroService;
import com.meve.ofspapel.firma.digital.core.service.UsuarioService;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import mx.com.neogen.commons.util.UtilTime;
import mx.eureka.firma.digital.bean.ArchivoDepositado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FirmaMasivaBsnsComponent {
    
    @Autowired private UsuarioService usuarioService;
    @Autowired private RegistroService registroService;
    
	@Autowired private IConfiguracionService configService;
	@Autowired private IStreamService        streamService;
	
    @Autowired private FirmaBaseBsnsComponent  service;
    
    
    public void registrarZip ( Usuario usuario, ArchivoDepositado zip, List<com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado> documentos) throws ParseException {
        final File file = zip.getPathDeposito();
        
        com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado archivoZip = registroService.registraDocumentoBatch( 
            usuario, zip.getFechaHora(), file.getParentFile().getName(), file.getName()
        );
        
        registroService.asignaDocumentos( archivoZip, documentos);
    }
    
    public ArchivoDepositado generarDestinoZip( String rfc) {
        final Date currentTimeStamp = UtilTime.getFechaHoraActual();
        
        final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        final String serverName      = configService.getPropiedad(     "url.server.name");
        final String webAppContext   = configService.getPropiedad(    "path.app.context");
        
        final String nombreArchivo = 
            "firma_masiva_" + rfc + new SimpleDateFormat( "yyyy_MM_dd_HH_mm_ss").format( currentTimeStamp) + ".zip"
        ;
        
        final String folioArchivo = streamService.obtenerFolioArchivo(); 
        final String pathDeposito = streamService.obtenerPathDeposito( pathRepositorio, folioArchivo, nombreArchivo);
        
        try {
            final String downloadURL  = streamService.generarURLDescarga( serverName, webAppContext, folioArchivo, nombreArchivo);
            ArchivoDepositado archivo = new ArchivoDepositado( pathDeposito, downloadURL);
            archivo.setFechaHora( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss").format( currentTimeStamp));
            
            return archivo;
            
        } catch( Exception ex) {
            throw new RuntimeException( ex); 
        }
    }
    
    public RespuestaFirmaMasiva firmarArchivos( final SolicitudFirma solicitud, List<InfoArchivo> archivos) {
	
		SessionFirma sf = new SessionFirma( solicitud);
		Resultado<?> resultado;
		
		try {
			resultado = service.validarInfoConfidencial( solicitud);
			if ( resultado.isError()) { return getRespuestaMasiva( resultado); }
			
            resultado = service.descargarInfoConfidencial( sf);
			if ( resultado.isError()) { return getRespuestaMasiva( resultado); }
        
            resultado = service.validarCertificado( sf);
			if ( resultado.isError()) { return getRespuestaMasiva( resultado); }
        
            resultado = service.obtenerPathDescarga();
			if ( resultado.isError()) { return getRespuestaMasiva( resultado); }
			String pathDescarga = (String) resultado.getResultado();

            List<String> paths = new ArrayList<>();
            sf.folio = streamService.obtenerFolioArchivo(); 
            
            Usuario usuario = usuarioService.obtenerUsuario( sf.firma.getRfc(), sf.firma.getTitular());
            List<com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado> documentos = new ArrayList<>();
            
            for ( InfoArchivo archivo : archivos) {
                solicitud.setArchivoDatos(  archivo);
                
                resultado = service.validarArchivoDatos( solicitud);
                if ( resultado.isError()) { break; }
		
                resultado = service.descargarArchivoDatos( sf, pathDescarga);
                if ( resultado.isError()) { break; }
                
                resultado = service.generaFirma( sf);
                if ( resultado.isError()) { break; } 
                
                paths.add( service.actualizarArchivo( sf, (Resultado<Firma>) resultado));
                
                documentos.add( registroService.registraDocumento( usuario, sf.firma.getFecha(), sf.folio, sf.archivo.getName()));
            }
                       
            return getRespuestaMasiva( resultado, paths, documentos);
        
        } catch ( Exception ex) {
			ex.printStackTrace();
            return getRespuestaMasiva( ResultadoEnum.ERROR_DESCONOCIDO.getResultado( ex.getMessage()));
			
        } finally {
			service.eliminarDirectorioUpload( sf);
            
		}
	}
    
    private RespuestaFirmaMasiva getRespuestaMasiva( Resultado<?> resultado) {	   
		return getRespuestaMasiva(resultado, null, null);
	}
    
    private RespuestaFirmaMasiva getRespuestaMasiva( Resultado<?> resultado, List<String> paths, List<com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado> documentos) {
		final RespuestaFirmaMasiva rf = new RespuestaFirmaMasiva();
		
		rf.setCodigo( resultado.getCodigo());
		rf.setMensaje( resultado.getMensaje());
		
		if ( resultado.getResultado() != null) {
			rf.setFirma( (Firma) resultado.getResultado());
		} else {
			rf.setFirma( new Firma());
		}
        
        rf.setPaths( paths);
        rf.setDocumentos( documentos);
        
		return rf;
	}
    
    
    public String obtenerRealPath( String urlDescarga) {
        final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        return streamService.obtenerPathDeposito( pathRepositorio, urlDescarga);
    }
    
}
