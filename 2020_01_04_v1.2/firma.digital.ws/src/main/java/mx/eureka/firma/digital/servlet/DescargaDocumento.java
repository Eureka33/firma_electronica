package mx.eureka.firma.digital.servlet;

import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.eureka.firma.digital.bean.BeanInfoDocumento;
import mx.eureka.firma.digital.bean.InfoArchivo;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class DescargaDocumento extends HttpServlet {
        
	public DescargaDocumento() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        final BeanInfoDocumento infoDocumento = UtilDocumento.requestToInfoDocumento( request);
		
        try {
            final InfoArchivo infoArchivo = UtilDocumento.obtenerInfoArchivo( infoDocumento);
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

        response.setContentType( "application/pdf");
        response.setHeader("Content-disposition","attachment; filename=" + infoArchivo.getNombre());
        
        try {
            return response.getOutputStream();
        
        } catch( Exception ex) {
            throw new OperacionNoRealizadaException( "error.infraestrura", ex);
        }
    }
    
    protected void forwardTo( HttpServletRequest request, HttpServletResponse response, String pagina) throws IOException, ServletException {	
		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( pagina);
		dispatcher.forward( request, response);
	}
}