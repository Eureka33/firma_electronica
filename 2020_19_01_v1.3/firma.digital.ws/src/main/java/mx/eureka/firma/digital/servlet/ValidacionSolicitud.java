package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.web.UtilWeb;
import com.meve.ofspapel.firma.digital.beans.DocumentoSolicitado;
import com.meve.ofspapel.firma.digital.core.components.DocumentoSolicitadoBsnsComponent;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.bean.Propiedades;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.BeanInfoDocumento;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class ValidacionSolicitud extends BaseServlet {

	private static final long serialVersionUID = 2389655495605997697L;
    private DocumentoSolicitadoBsnsComponent bsnsComponent;
	
	public ValidacionSolicitud() {
		super();
	}

    @Override
    public void init() throws ServletException {
        super.init();
        bsnsComponent = AppContext.getBean( DocumentoSolicitadoBsnsComponent.class);
    }
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
        DocumentoSolicitado info;
        if( (info = obtenerInfoDocumento( request)) == null) {
            forwardTo( request, response, "/jsp/validacionSolicitud.jsp?ts=" + Math.random());
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
		
		forwardTo( request, response, "/jsp/validacionSolicitud.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		DocumentoSolicitado info;
        if( (info = obtenerInfoDocumento( request)) == null) {
            forwardTo( request, response, "/jsp/validacionSolicitud.jsp?ts=" + Math.random());
            return;
        }

        BeanInfoDocumento infoDocumento = UtilDocumento.requestToInfoDocumento( info);
        
        try {
            final String cheksum = checksumStoredFile( infoDocumento);
            final String resultado = cheksum.equals( checksumUploadedFile( request))? "ok": "error";
            final String params = "&rand="     + info.getId() + "-" + Math.random() + "&resultado=" + resultado;
			
            response.sendRedirect( "validacionSolicitud?ts=" + Math.random() + params);
		
        } catch ( Exception ex) {
            if ( "error.negocio.entidad.inexistente".equals( ex.getMessage())) {
                request.setAttribute( "info", info);
                request.setAttribute( "resultado", "No existe documento con el folio y nombre solicitados");
                forwardTo( request, response, "/jsp/validacionSolicitud.jsp?ts=" + Math.random());
                
            } else {
                throw ex;
            }
		}
	}
    
    private DocumentoSolicitado obtenerInfoDocumento( HttpServletRequest request) {
        String rand = request.getParameter( "rand");
        
        if ( rand == null) {
            request.setAttribute( "resultado", "No se encuentra el documento solicitado");
            return null;
        }
        
        String[] numeros = rand.split( "-");
        
        if ( numeros.length != 2) {
            request.setAttribute( "resultado", "No se encuentra el documento solicitado");
            return null;
        }
         
        final Usuario invocador = UtilWeb.getUsuarioPuesto( request);
        
        return bsnsComponent.obtenerItem( "", invocador, numeros[0], new Propiedades());
    }
      
	
}