package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import java.io.IOException;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.eureka.firma.digital.bean.BeanInfoFirma;
import mx.eureka.firma.digital.bean.UtilDocumento;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;


public class BaseServlet extends HttpServlet {
    
    
	protected void forwardTo( HttpServletRequest request, HttpServletResponse response, String pagina) throws IOException, ServletException {	
		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( pagina);
		dispatcher.forward( request, response);
	}
    
    
    protected InfoConfidencial getInfoConfidencial( final BeanInfoFirma bean) {
		final InfoConfidencial info = new InfoConfidencial();
		
		info.setPasswordLlave( bean.getPassword());
		
		InfoArchivo archivo;
        
        archivo = new InfoArchivo();
		archivo.setHandler( UtilDocumento.createInstance( bean.getLlavePrivada(), "llavePrivada.key", "key"));
		archivo.setNombre( "llavePrivada");
		archivo.setExtension( "key");
		
        info.setArchivoLlave( archivo);
			
		archivo = new InfoArchivo();
		archivo.setHandler( UtilDocumento.createInstance( bean.getCertificado(), "certificado.cer", "cert"));	
		archivo.setNombre( "certificadoPublico");
		archivo.setExtension( "cer");
		
        info.setCertificado( archivo);
		
		return info;
	}
    
    protected String checksumUploadedFile( final HttpServletRequest request) {
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
	
	protected String checksumStoredFile( DocumentoFirmado info) { 
        mx.eureka.firma.digital.bean.InfoArchivo infoArchivo = UtilDocumento.obtenerInfoArchivo( info, false);
        return UtilDocumento.getMd5( infoArchivo.getContenido());
    }
    
}
