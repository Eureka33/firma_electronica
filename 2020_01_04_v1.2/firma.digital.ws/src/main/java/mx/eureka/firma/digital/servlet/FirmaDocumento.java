package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.eureka.firma.digital.ws.core.FirmaElectronicaBsnsComponent;
import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.util.UtilStream;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.BeanInfoFirma;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class FirmaDocumento extends HttpServlet {

	private static final long serialVersionUID = 2389655495605997697L;

	
	public FirmaDocumento() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		forwardTo( request, response, "/jsp/firmaDocumento.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final BeanInfoFirma bean = UtilDocumento.requestToInfoFirma( request);
       
        final FirmaElectronicaBsnsComponent service = AppContext.getBean( FirmaElectronicaBsnsComponent.class);
        
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        solicitud.setArchivoDatos(     getArchivoDatos(     bean));
        solicitud.setInfoConfidencial( getInfoConfidencial( bean));
        
        final RespuestaFirma respuesta = service.firmarArchivo( solicitud);
        
        final Firma firma = respuesta.getFirma();
        
        if( respuesta.getCodigo().equals( 0)) {
            response.sendRedirect( firma.getUrlDescarga());
        } else {
            request.getSession().setAttribute( "errorMessages", respuesta.getMensaje().split( ":"));
            response.sendRedirect( "firmaDocumento?ts=" + Math.random());
        }
	}
    
    
    private static InfoConfidencial getInfoConfidencial( final BeanInfoFirma bean) {
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
    
    
    private static InfoArchivo getArchivoDatos( final BeanInfoFirma bean) {
		InfoArchivo archivo = new InfoArchivo();
        
		archivo.setHandler( UtilDocumento.createInstance( bean.getContenidoDocumento(), bean.getNombreDocumento(), "documento"));	
		archivo.setNombre(    UtilStream.getNombreArchivo(    bean.getNombreDocumento()));
		archivo.setExtension( UtilStream.getExtensionArchivo( bean.getNombreDocumento()));
		
		return archivo;
	}
    
    
	protected void forwardTo( HttpServletRequest request, HttpServletResponse response, String pagina) throws IOException, ServletException {	
		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( pagina);
		dispatcher.forward( request, response);
	}
    
}