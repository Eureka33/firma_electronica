package mx.eureka.firma.digital.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.eureka.firma.digital.bean.BeanInfoDocumento;
import mx.eureka.firma.digital.bean.InfoArchivo;
import mx.eureka.firma.digital.bean.UtilDocumento;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;


public class ValidacionDocumento extends HttpServlet {

	private static final long serialVersionUID = 2389655495605997697L;

	
	public ValidacionDocumento() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setAttribute( "info", UtilDocumento.requestToInfoDocumento( request));
			
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
		
		final BeanInfoDocumento infoDocumento = UtilDocumento.requestToInfoDocumento(request);
		
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
	}
    
	protected void forwardTo( HttpServletRequest request, HttpServletResponse response, String pagina) throws IOException, ServletException {	
		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( pagina);
		dispatcher.forward( request, response);
	}
	
	private String checksumUploadedFile( final HttpServletRequest request) {
		boolean isMultipart = FileUploadBase.isMultipartContent(request);

		if (!isMultipart) {
			return "";
		}
			
		final DiskFileUpload upload = new DiskFileUpload();
        try { 
            List items = upload.parseRequest( request);
            for ( Object nextItem : items) {
                FileItem item = (FileItem) nextItem;
                if ( !item.isFormField()) {
                    return UtilDocumento.getMd5( item.getInputStream());
                }
            }
        
        } catch( Exception ex) {
            throw new OperacionNoRealizadaException( "error.infraestructura", ex);
        
        }
        
		return "";
	}
	
	public static String checksumStoredFile( BeanInfoDocumento info) { 
        InfoArchivo infoArchivo = UtilDocumento.obtenerInfoArchivo( info);
        return UtilDocumento.getMd5( infoArchivo.getContenido());
    }
	
}