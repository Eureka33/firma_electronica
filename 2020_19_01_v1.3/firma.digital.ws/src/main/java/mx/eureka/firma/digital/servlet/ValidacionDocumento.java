package mx.eureka.firma.digital.servlet;

import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import com.meve.ofspapel.firma.digital.core.components.DocumentoSolicitadoBsnsComponent;
import java.io.IOException;
import java.net.URLEncoder;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class ValidacionDocumento extends BaseServlet {

	private static final long serialVersionUID = 2389655495605997697L;
    private UtilDocumento util;
    
	public ValidacionDocumento() {
		super();
	}
    
    
    @Override
    public void init() throws ServletException {
        super.init();
        util = new UtilDocumento( AppContext.getBean( DocumentoSolicitadoBsnsComponent.class));
    }

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        final DocumentoFirmado info = util.requestToDocumento( request);
        
        if( info == null) {
            request.setAttribute( "resultado", "No se encuentra el documento solicitado");   
            forwardTo( request, response, "/jsp/validacionDocumento.jsp?ts=" + Math.random());
            
            return;
        }
        
        request.setAttribute( "info", info);
        
		final String resultado =  request.getParameter( "resultado");
			
		if( resultado != null) {
			if ( "ok".equals( resultado)) {
				request.setAttribute( "resultado", "Los documentos son idénticos");
			} else {
				request.setAttribute( "resultado", "El documento no concuerda con el original");
			}
		}
		
		forwardTo( request, response, "/jsp/validacionDocumento.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        final DocumentoFirmado info = util.requestToDocumento( request);
        
        if( info == null) {
            request.setAttribute( "resultado", "No se encuentra el documento solicitado");   
            forwardTo( request, response, "/jsp/validacionDocumento.jsp?ts=" + Math.random());
            
            return;
        }	
        
        try {	
            final String cheksum = checksumStoredFile( info);
            final String resultado = cheksum.equals( checksumUploadedFile( request))? "ok": "error";
            final String params = 
                "&folio="     + info.getFolio()  + 
                "&nombre="    + URLEncoder.encode( info.getNombre(), "UTF-8") +
                "&resultado=" + resultado
            ;
			
            response.sendRedirect( "validacionDocumento?ts=" + Math.random() + params);
		
        } catch ( Exception ex) {
            if ( "error.negocio.entidad.inexistente".equals( ex.getMessage())) {
                request.setAttribute( "info", info);
                request.setAttribute( "resultado", "No existe documento con el folio y nombre solicitados");
                forwardTo( request, response, "/jsp/validacionDocumento.jsp?ts=" + Math.random());
                
            } else {
                throw ex;
            }
		}
	}
	
}