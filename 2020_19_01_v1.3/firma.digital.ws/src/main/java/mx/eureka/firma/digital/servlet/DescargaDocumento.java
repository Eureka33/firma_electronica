package mx.eureka.firma.digital.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.eureka.firma.digital.bean.BeanInfoDocumento;
import mx.eureka.firma.digital.bean.InfoArchivo;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class DescargaDocumento extends BaseServlet {
        
	public DescargaDocumento() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final BeanInfoDocumento infoDocumento = UtilDocumento.requestToInfoDocumento( request);
		String value = request.getParameter( "isUpload");
        Boolean isUpload = (value == null || value.length() == 0)? false : Boolean.valueOf( value);
        
        try {
            final InfoArchivo infoArchivo = UtilDocumento.obtenerInfoArchivo( infoDocumento, isUpload);
            final OutputStream out = prepararDescarga( infoArchivo, response);
        
            UtilDocumento.copiarContenido( infoArchivo.getContenido(), out);
        
        } catch ( Exception ex) {
            if ( "error.negocio.entidad.inexistente".equals( ex.getMessage())) {
                
                request.setAttribute( "info", infoDocumento);
                request.setAttribute( "resultado", "No existe documento con el folio y nombre solicitados");
                
                forwardTo( request, response, "/jsp/validacionDocumento.jsp?ts=" + Math.random());
                
            } else {
                throw ex;
            }
		}
		
	}

    private OutputStream prepararDescarga( InfoArchivo infoArchivo, HttpServletResponse response) {

        if( infoArchivo.getNombre().endsWith( "zip")) {
            response.setContentType( "application/zip");
        
        } else {
            response.setContentType( "application/pdf");
        
        }
        
        response.setHeader("Content-disposition","attachment; filename=\"" + infoArchivo.getNombre() + "\"");
        
        try {
            return response.getOutputStream();
        
        } catch( Exception ex) {
            throw new OperacionNoRealizadaException( "error.infraestrura", ex);
        }
    }
    
}