package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.eureka.firma.digital.ws.core.SolicitudFirmaBsnsComponent;
import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import com.meve.ofspapel.firma.digital.core.components.DocumentoSolicitadoBsnsComponent;
import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import com.meve.ofspapel.firma.digital.core.service.MailSenderService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.util.UtilStream;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.BeanInfoFirma;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class SolicitudDocumento extends BaseServlet {

	private static final long serialVersionUID = 2389655495605997697L;
    
    private SolicitudFirmaBsnsComponent solicitudFirma;
    private UtilDocumento util;
    
	public SolicitudDocumento() {
		super();
	}
    

    @Override
    public void init() throws ServletException {
        super.init();
        solicitudFirma = AppContext.getBean(   SolicitudFirmaBsnsComponent.class);
        util = new UtilDocumento( AppContext.getBean( DocumentoSolicitadoBsnsComponent.class));
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
       final DocumentoFirmado info = util.requestToDocumento( request);
        
		request.setAttribute( "info", info);
			
		final String mensaje = registrarVisita( info);
        if ( mensaje != null) {
            request.getSession().setAttribute( "errorMessages", new String[] {mensaje});
        }
        
		forwardTo( request, response, "/jsp/solicitudDocumento.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        final DocumentoFirmado info = util.requestToDocumento( request);
		final BeanInfoFirma infoFirma = UtilDocumento.requestToInfoFirma( request);
       
        String pagina = firmaDocumento( request, infoFirma, info);
        
        if ( pagina != null) {
            response.sendRedirect( pagina);
            
        } else {
            request.setAttribute( "info", info);
            forwardTo( request, response, "/jsp/solicitudDocumento.jsp?ts=" + Math.random());
        }
	}
    
    private String registrarVisita( DocumentoFirmado info) {
        EnumEstatusSolicitud estatus = solicitudFirma.registraVisitaLink( info.getSolicitud().getFolio(), info.getNombre());
        
        if ( estatus == null) {
            return "La información de la petición no esta registrada en el sistema.";
        }
        
        switch( estatus) {
            case REGISTRADA:
            case NOTIFICADA:
                return "El token de esta peticion todavía no ha sido activado (solicitud registrada/notificada)";
                
            case ATENDIDA:
                return "El token indicado ya ha sido utilizado (solicitud atendida)";
                
            case CANCELADA:
                return "El token solicitado ya no es valido (solicitud cancelada)";
                
            default:
                return null;
        }
    }
    
    private String firmaDocumento( HttpServletRequest request, BeanInfoFirma infoFirma, DocumentoFirmado info) {
        
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        InfoArchivo archivo = new InfoArchivo();
        archivo.setNombre(    UtilStream.getNombreArchivo(    info.getNombre()));
		archivo.setExtension( UtilStream.getExtensionArchivo( info.getNombre()));
        
        solicitud.setArchivoDatos(     archivo);
        solicitud.setInfoConfidencial( getInfoConfidencial( infoFirma));
      
        RespuestaFirma respuesta = solicitudFirma.firmarArchivo(
            solicitud, UtilDocumento.obtenerFileUploaded( info), info
        );
        
        final Firma firma = respuesta.getFirma();
        
        if( respuesta.getCodigo().equals( 0)) {
            sendNotificacion(
                infoFirma.getCorreo(), firma.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                info.getNombre()
            );
            
            sendNotificacion(
                info.getSolicitud().getEmailSolicitante(), firma.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                info.getNombre()
            );
            
            return firma.getUrlDescarga();
        
        } else {
            request.getSession().setAttribute( "processMessages", respuesta.getMensaje().split( ":"));
            
            return null;
        } 
    }
    
    private void sendNotificacion(
                                    final String correo, final String urlDescarga, final String titular      , 
                                    final String rfc   , final String fecha      , final String nombreArchivo) {
        if ( correo == null || correo.isEmpty()) {
            return;
        }
        
        final Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                final MailSenderService service = AppContext.getBean( MailSenderService.class);
                service.sendNotificacion( correo, urlDescarga, titular, rfc, fecha, nombreArchivo);
            }
        });
        
        thread.start();
    }
    
}