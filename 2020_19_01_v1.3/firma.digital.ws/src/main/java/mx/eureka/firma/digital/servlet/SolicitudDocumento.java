package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.core.SolicitudFirmaBsnsComponent;
import com.meve.ofspapel.firma.digital.core.enums.EnumEstatusSolicitud;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.BeanInfoDocumento;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class SolicitudDocumento extends BaseServlet {

	private static final long serialVersionUID = 2389655495605997697L;
    
    private SolicitudFirmaBsnsComponent solicitudFirma;

	
	public SolicitudDocumento() {
		super();
	}
    

    @Override
    public void init() throws ServletException {
        super.init();
         solicitudFirma = AppContext.getBean(   SolicitudFirmaBsnsComponent.class);
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        BeanInfoDocumento info = UtilDocumento.requestToInfoDocumento( request);
		request.setAttribute( "info", info);
			
		final String resultado =  request.getParameter( "resultado");
			
		if( resultado != null) {
			if ( "ok".equals( resultado)) {
				request.setAttribute( "resultado", "Los documentos son idénticos");
			} else {
				request.setAttribute( "resultado", "El documento no concuerda con el original");
			}
            
        } else {
            final String mensaje = registrarVisita( info);
            request.setAttribute( "error", mensaje);
        }
        
		forwardTo( request, response, "/jsp/solicitudDocumento.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		final BeanInfoDocumento infoDocumento = UtilDocumento.requestToInfoDocumento(request);
		/*
        try {	
            final String cheksum = checksumStoredFile( infoDocumento);
            final String resultado = cheksum.equals( checksumUploadedFile( request))? "ok": "error";
            final String params = 
                "&folio="     + infoDocumento.getFolio()  + 
                "&nombre="    + URLEncoder.encode( infoDocumento.getNombre(), "UTF-8") +
                "&resultado=" + resultado
            ;
			
            response.sendRedirect( "validacionDocumento?ts=" + Math.random() + params);
		
        } catch ( Exception ex) {
            if ( "error.negocio.entidad.inexistente".equals( ex.getMessage())) {
                request.setAttribute( "info", infoDocumento);
                request.setAttribute( "resultado", "No existe documento con el folio y nombre solicitados");
                forwardTo( request, response, "/jsp/validacionDocumento.jsp?ts=" + Math.random());
                
            } else {
                throw ex;
            }
		}
        */
	}
    
    private String registrarVisita( BeanInfoDocumento info) {
        EnumEstatusSolicitud estatus = solicitudFirma.registraVisitaLink( info.getFolio(), info.getNombre());
        
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
    
}