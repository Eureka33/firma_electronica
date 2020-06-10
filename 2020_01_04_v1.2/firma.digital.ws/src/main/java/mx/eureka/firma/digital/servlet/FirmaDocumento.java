package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.RespuestaFirmaMasiva;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.eureka.firma.digital.ws.core.FirmaElectronicaBsnsComponent;
import com.meve.ofspapel.firma.digital.core.service.MailSenderService;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.util.UtilStream;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.ArchivoDepositado;
import mx.eureka.firma.digital.bean.BeanInfoFirma;
import mx.eureka.firma.digital.bean.UtilDocumento;
import mx.eureka.firma.digital.bean.ZipCreator;


public class FirmaDocumento extends HttpServlet {

	private static final long serialVersionUID = 2389655495605997697L;
    private static final String NOMBRE_ORGANIZACION = "Aeropuertos y Servicios Auxiliares";

    private FirmaElectronicaBsnsComponent firmaService;
	
    
	public FirmaDocumento() {
		super();
	}

    
    @Override
    public void init() throws ServletException {
        super.init();
        
        firmaService = AppContext.getBean( FirmaElectronicaBsnsComponent.class);
    }
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		forwardTo( request, response, "/jsp/firmaDocumento.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final BeanInfoFirma bean = UtilDocumento.requestToInfoFirma( request);
       
        
        if( bean.getArchivos().size() == 1) {
            final mx.eureka.firma.digital.bean.InfoArchivo archivo = bean.getArchivos().get( 0);
            
            final RespuestaFirma respuesta = firmarDocumento( archivo, bean);
            final Firma firma = respuesta.getFirma();
        
            if( respuesta.getCodigo().equals( 0)) {
                sendNotificacion( 
                    bean.getCorreo(), firma.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                    NOMBRE_ORGANIZACION, archivo.getNombre()
                );
            
                response.sendRedirect( firma.getUrlDescarga());
            } else {
                request.getSession().setAttribute( "errorMessages", respuesta.getMensaje().split( ":"));
                response.sendRedirect( "firmaDocumento?ts=" + Math.random());
            }
            
        } else {
            
            final RespuestaFirmaMasiva respuesta = firmarDocumentos( bean.getArchivos(), bean);
            final Firma firma = respuesta.getFirma();
                        
            if( respuesta.getCodigo().equals( 0)) {
            
                final ArchivoDepositado zip = generarZip( respuesta.getPaths());
                       
                sendNotificacion(
                    bean.getCorreo(), zip.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                    NOMBRE_ORGANIZACION, zip.getPathDeposito().getName()
                );
                
                response.sendRedirect( zip.getUrlDescarga());
            
            } else {
                request.getSession().setAttribute( "errorMessages", respuesta.getMensaje().split( ":"));
                response.sendRedirect( "firmaDocumento?ts=" + Math.random());
            }
        }
	}
    
    private void sendNotificacion(  final String correo, final String urlDescarga, final String titular, 
                                    final String rfc, final String fecha, final String organizacion,
                                    final String nombreArchivo) {
        if ( correo == null || correo.isEmpty()) {
            return;
        }
        
        final Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                final MailSenderService service = AppContext.getBean( MailSenderService.class);
                
                service.sendNotificacion( correo, urlDescarga, titular, rfc, fecha, organizacion, nombreArchivo);
            }
        });
        
        thread.start();
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
    
    private RespuestaFirma firmarDocumento( mx.eureka.firma.digital.bean.InfoArchivo archivo, BeanInfoFirma bean) {
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        solicitud.setArchivoDatos(     getArchivoDatos(     archivo));
        solicitud.setInfoConfidencial( getInfoConfidencial( bean));
        solicitud.setOrganizacion( NOMBRE_ORGANIZACION);
        
        return firmaService.firmarArchivo( solicitud);
    }
    
    private RespuestaFirmaMasiva firmarDocumentos( List<mx.eureka.firma.digital.bean.InfoArchivo> archivos, BeanInfoFirma bean) {
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        final List<InfoArchivo> archivosDS = new ArrayList<>();
        for( mx.eureka.firma.digital.bean.InfoArchivo archivo : archivos) {
            archivosDS.add( getArchivoDatos( archivo));
        }
        
        solicitud.setInfoConfidencial( getInfoConfidencial( bean));
        solicitud.setOrganizacion( NOMBRE_ORGANIZACION);
        
        return firmaService.firmarArchivos( solicitud, archivosDS);
    }
    
    private ArchivoDepositado generarZip( List<String> archivos) {
        final ArchivoDepositado zip = firmaService.generarDestinoZip();
        
        final ZipCreator creator = new ZipCreator();
        creator.create( zip.getPathDeposito(), archivos);
        
        return zip;
    }
    
    private String[] obtenerMensajesError( String nombreArchivo, String[] errores) {
        final List<String> mensajes = new ArrayList<>();
        
        mensajes.add( "Archivo: " + nombreArchivo);
        for( String error : errores) {
            mensajes.add( error);
        }
        
        return mensajes.toArray( new String[] {});
    }
    
    private static InfoArchivo getArchivoDatos( final mx.eureka.firma.digital.bean.InfoArchivo info) {
		final InfoArchivo archivo = new InfoArchivo();
        
		archivo.setHandler( UtilDocumento.createInstance( info.getContenido(), info.getNombre(), "documento"));	
		archivo.setNombre(    UtilStream.getNombreArchivo(    info.getNombre()));
		archivo.setExtension( UtilStream.getExtensionArchivo( info.getNombre()));
		
		return archivo;
	}
    
	protected void forwardTo( HttpServletRequest request, HttpServletResponse response, String pagina) throws IOException, ServletException {	
		final RequestDispatcher dispatcher = getServletContext().getRequestDispatcher( pagina);
		dispatcher.forward( request, response);
	}
    
}