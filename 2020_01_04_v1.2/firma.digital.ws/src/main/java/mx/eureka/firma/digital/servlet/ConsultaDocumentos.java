package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.eureka.firma.digital.ws.core.FirmaElectronicaBsnsComponent;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.BeanInfoFirma;
import mx.eureka.firma.digital.bean.UtilDocumento;


public class ConsultaDocumentos extends BaseServlet {

	private static final long serialVersionUID = 2389655495605997697L;

    private FirmaElectronicaBsnsComponent firmaService;
	
    
	public ConsultaDocumentos() {
		super();
	}

    
    @Override
    public void init() throws ServletException {
        super.init();
        
        firmaService    = AppContext.getBean( FirmaElectronicaBsnsComponent.class);
    }
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
		forwardTo( request, response, "/jsp/consultaDocumentos.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final BeanInfoFirma bean = UtilDocumento.requestToInfoFirma( request);
       
        final Resultado<?> resultado = autenticarUsuario( bean);
        
        if( resultado.isError()) {
            request.getSession().setAttribute( "errorMessages", resultado.getMensaje().split( ":"));
            response.sendRedirect( "consultaDocumentos?ts=" + Math.random());
        
        } else {
            request.getSession().setAttribute( "usuario", resultado.getResultado());
            response.sendRedirect( "consultaDocumentos");
        
        }
	}
    
    private InfoConfidencial getInfoConfidencial( final BeanInfoFirma bean) {
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
    
    private Resultado<?> autenticarUsuario( BeanInfoFirma bean) {
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        solicitud.setInfoConfidencial( getInfoConfidencial( bean));
        
        return firmaService.autenticarUsuario( solicitud);
    }

}