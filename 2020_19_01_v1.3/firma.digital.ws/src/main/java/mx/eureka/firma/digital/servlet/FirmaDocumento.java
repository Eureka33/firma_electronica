package mx.eureka.firma.digital.servlet;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.InfoArchivo;
import com.eureka.firma.digital.ws.bean.InfoConfidencial;
import com.eureka.firma.digital.ws.bean.RespuestaFirma;
import com.eureka.firma.digital.ws.bean.RespuestaFirmaMasiva;
import com.eureka.firma.digital.ws.bean.SolicitudFirma;
import com.eureka.firma.digital.ws.core.FirmaElectronicaBsnsComponent;
import com.eureka.firma.digital.ws.core.FirmaMasivaBsnsComponent;
import com.eureka.firma.digital.ws.core.SolicitudFirmaBsnsComponent;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.service.MailSenderService;
import com.meve.ofspapel.firma.digital.core.service.UsuarioService;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.util.UtilStream;
import mx.eureka.firma.digital.bean.AppContext;
import mx.eureka.firma.digital.bean.ArchivoDepositado;
import mx.eureka.firma.digital.bean.BeanInfoFirma;
import mx.eureka.firma.digital.bean.UtilDocumento;
import mx.eureka.firma.digital.bean.ZipCreator;


public class FirmaDocumento extends BaseServlet {

	private static final long serialVersionUID = 2389655495605997697L;

    private FirmaElectronicaBsnsComponent  firmaSimple;
    private FirmaMasivaBsnsComponent       firmaMasiva;
    private SolicitudFirmaBsnsComponent solicitudFirma;
	private UsuarioService usuarioService;
    
    
	public FirmaDocumento() {
		super();
	}

    
    @Override
    public void init() throws ServletException {
        super.init();
        
        firmaSimple    = AppContext.getBean( FirmaElectronicaBsnsComponent.class);
        firmaMasiva    = AppContext.getBean(      FirmaMasivaBsnsComponent.class);
        solicitudFirma = AppContext.getBean(   SolicitudFirmaBsnsComponent.class);
        usuarioService = AppContext.getBean( UsuarioService.class);
    }
    
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		forwardTo( request, response, "/jsp/firmaDocumento.jsp?ts=" + Math.random());
	}
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		final BeanInfoFirma bean = UtilDocumento.requestToInfoFirma( request);
       
        if( bean.getArchivos().size() == 1) {           // 1 archivo
            
            String pagina;
            
            if( bean.getIdOperacion() == 0) {
                pagina = firmaDocumento( request, bean);
            
            } else {
                pagina = registraSolicitud( request, bean); 
                
            }
            
            response.sendRedirect( pagina);
            
        } else {                                        // N archivos
            
            final RespuestaFirmaMasiva respuesta = firmarDocumentos( bean.getArchivos(), bean);
            final Firma firma = respuesta.getFirma();
                        
            if( respuesta.getCodigo().equals( 0)) {
            
                final ArchivoDepositado zip = generarZip( firma, respuesta.getPaths(), respuesta.getDocumentos());
                       
                sendNotificacion(
                    bean.getCorreo(), zip.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                    zip.getPathDeposito().getName()
                );
                
                response.sendRedirect( zip.getUrlDescarga());
            
            } else {
                request.getSession().setAttribute( "errorMessages", respuesta.getMensaje().split( ":"));
                response.sendRedirect( "firmaDocumento?ts=" + Math.random());
            }
        }
	}
    
    private String firmaDocumento( HttpServletRequest request, BeanInfoFirma bean) {
        
        final mx.eureka.firma.digital.bean.InfoArchivo archivo = bean.getArchivos().get( 0);
        
        final RespuestaFirma respuesta = firmarDocumento( archivo, bean);
        final Firma firma = respuesta.getFirma();
        
        if( respuesta.getCodigo().equals( 0)) {
            sendNotificacion(
                bean.getCorreo(), firma.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                archivo.getNombre()
            );
            
            return firma.getUrlDescarga();
            
        } else {
            request.getSession().setAttribute( "errorMessages", respuesta.getMensaje().split( ":"));
            
            return "firmaDocumento?ts=" + Math.random();
        }
    }
    
    private String registraSolicitud( HttpServletRequest request, BeanInfoFirma bean) {
        
        final mx.eureka.firma.digital.bean.InfoArchivo archivo = bean.getArchivos().get( 0);
        
        final RespuestaFirma respuesta = registraSolicitud( archivo, bean);
        final Firma firma = respuesta.getFirma();
        
        if( respuesta.getCodigo().equals( 0)) {
            sendNotificacion(
                bean.getCorreoDestinatario(), firma.getUrlDescarga(), firma.getTitular(), firma.getRfc(), firma.getFecha(),
                archivo.getNombre()
            );
            
            request.getSession().setAttribute( "errorMessages", new String[] {"Su solicitud ha sido enviada al destinatario"});
            
            return firma.getUrlDescarga();
            
        } else {
            request.getSession().setAttribute( "errorMessages", respuesta.getMensaje().split( ":"));
            
            return "firmaDocumento?ts=" + Math.random();
        }
    }
    
    private void sendNotificacion(  final String correo, final String urlDescarga, final String titular, 
                                    final String rfc, final String fecha, final String nombreArchivo) {
        if ( correo == null || correo.isEmpty()) {
            return;
        }
        
        final Thread thread = new Thread( new Runnable() {
            @Override
            public void run() {
                final MailSenderService service = AppContext.getBean( MailSenderService.class);
                
                service.sendNotificacion( correo, urlDescarga, titular, rfc, fecha, nombreArchivo);
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
        
        return firmaSimple.firmarArchivo( solicitud);
    }
    
    private RespuestaFirma registraSolicitud( mx.eureka.firma.digital.bean.InfoArchivo archivo, BeanInfoFirma bean) {
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        solicitud.setArchivoDatos(     getArchivoDatos(     archivo));
        solicitud.setInfoConfidencial( getInfoConfidencial( bean));
        
        return solicitudFirma.registrarArchivo( solicitud, bean.getCorreoDestinatario());
    }
    
    private RespuestaFirmaMasiva firmarDocumentos( List<mx.eureka.firma.digital.bean.InfoArchivo> archivos, BeanInfoFirma bean) {
        final SolicitudFirma solicitud = new SolicitudFirma();
        
        final List<InfoArchivo> archivosDS = new ArrayList<>();
        archivos.forEach(archivo -> {
            archivosDS.add( getArchivoDatos( archivo));
        });
        
        solicitud.setInfoConfidencial( getInfoConfidencial( bean));
        
        return firmaMasiva.firmarArchivos( solicitud, archivosDS);
    }
    
    private ArchivoDepositado generarZip( Firma firma, List<String> archivos, List<com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado> documentos) {
        
        Usuario usuario = usuarioService.obtenerUsuario( firma.getRfc(), firma.getTitular());
        
        final ArchivoDepositado zip = firmaMasiva.generarDestinoZip( usuario.getClave());
        
        final ZipCreator creator = new ZipCreator();
        creator.create( zip.getPathDeposito(), archivos);
        
        try {
            firmaMasiva.registrarZip ( usuario, zip, documentos);
        } catch (ParseException ex) {
            throw new RuntimeException( ex);
        }
        
        return zip;
    }
    
    private static InfoArchivo getArchivoDatos( final mx.eureka.firma.digital.bean.InfoArchivo info) {
		final InfoArchivo archivo = new InfoArchivo();
        
		archivo.setHandler( UtilDocumento.createInstance( info.getContenido(), info.getNombre(), "documento"));	
		archivo.setNombre(    UtilStream.getNombreArchivo(    info.getNombre()));
		archivo.setExtension( UtilStream.getExtensionArchivo( info.getNombre()));
		
		return archivo;
	}
    
}