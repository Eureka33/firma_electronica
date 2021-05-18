package mx.eureka.firma.digital.bean;

import com.eureka.firma.digital.web.UtilWeb;
import com.eureka.firma.digital.ws.core.IStreamService;
import com.meve.ofspapel.firma.digital.beans.DocumentoFirmado;
import com.meve.ofspapel.firma.digital.core.components.DocumentoSolicitadoBsnsComponent;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.List;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.servlet.http.HttpServletRequest;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.com.neogen.commons.util.UtilStream;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;

public class UtilDocumento {
    
    private final DocumentoSolicitadoBsnsComponent solicitudService; 
    
    
    public UtilDocumento( DocumentoSolicitadoBsnsComponent solicitudService) {
        super();
        this.solicitudService = solicitudService;
    }
    
    
    public DocumentoFirmado requestToDocumento( HttpServletRequest request) throws UnsupportedEncodingException {
            
        // el id de la solicitud puede venir oculta en el parametro rand
        
        final String idSolicitudStr = obtenerIdSolicitud( request);
        
        if ( idSolicitudStr != null) {
            final Usuario invocador = UtilWeb.getUsuarioPuesto( request);
            return solicitudService.obtenerItem( "", invocador, idSolicitudStr, new Propiedades());
        }
        
        // un documento firmado puede indicarse por folio y nombre
        Propiedades propiedades = obtenerInfoDocumento( request);
        return solicitudService.obtenerItem( "", null, null, propiedades);
    }
    
    private Propiedades obtenerInfoDocumento( HttpServletRequest request) throws UnsupportedEncodingException {
        
        final Propiedades info = new Propiedades();
        
		info.setPropiedad( "folio", request.getParameter( "folio"));
        
        final String queryString = request.getQueryString();
        
        final int idx = queryString.indexOf( "&nombre=");
        int endIdx = queryString.indexOf( '&', idx + 8);
        endIdx = (endIdx < 0)? queryString.length() : endIdx;
		
        info.setPropiedad( "nombre", URLDecoder.decode( request.getQueryString().substring( idx + 8, endIdx), "UTF-8"));
        
        return info;
	}
    
    private String obtenerIdSolicitud( HttpServletRequest request) {
        final String rand = request.getParameter( "rand");
        
        if ( rand == null) {
            return null;
        }
        
        String[] numeros = rand.split( "-");
        
        if ( numeros.length != 2) {
            return null;
        }
         
        return numeros[0];    
    }
    
    public static BeanInfoFirma requestToInfoFirma( HttpServletRequest request) {
        boolean isMultipart = FileUploadBase.isMultipartContent( request);

		if (!isMultipart) {
			return null;
		}
        
        final BeanInfoFirma bean = new BeanInfoFirma();
        
		final DiskFileUpload upload = new DiskFileUpload();
       
        try { 
            List items = upload.parseRequest( request);
            String value;
            
            for ( Object nextItem : items) {
                FileItem item = (FileItem) nextItem;
                    
                if ( !item.isFormField()) {
                    
                    switch( item.getFieldName()) {
                        case "documento":
                            final InfoArchivo archivo = new InfoArchivo();
                        
                            archivo.setNombre( item.getName());
                            archivo.setContenido( item.getInputStream());
                        
                            bean.addArchivo( archivo);
                            
                            break;
                            
                        case "certificado":
                            bean.setCertificado( item.getInputStream());
                            break;
                        
                        case "llavePrivada":
                            bean.setLlavePrivada( item.getInputStream());
                            break;
                            
                        default:
                            throw new IllegalArgumentException( "Campo de archivo no conocido: " + item.getFieldName());
                    }
                 
                } else {
                    value = item.getString().trim();
                    
                    switch( item.getFieldName()) {
                        case "idOperacion":
                            bean.setIdOperacion( Integer.valueOf( value));
                            break;
                            
                        case "password":
                            bean.setPassword( value);
                            break;
                            
                        case "correo":
                            bean.setCorreo(   value);
                            break;
                            
                        case "correoDestinatario":
                            bean.setCorreoDestinatario( value);
                            break;
                        
                        default:
                            throw new IllegalArgumentException( "Campo de formulario no conocido: " + item.getFieldName());
                    }
                }
            }
        
        } catch( Exception ex) {
            throw new OperacionNoRealizadaException( "error.infraestructura", ex);
        
        }
        
		return bean;
    }
    
    
    public static void copiarContenido( InputStream in, OutputStream out) {
        final byte[] buffer = new byte[4*1024];
        
        BufferedInputStream bin = null;
        
        try {
            bin = new BufferedInputStream( in);
            
            int leidos;
            while( (leidos = bin.read( buffer)) != -1) {
                out.write( buffer, 0, leidos);
            }
            out.flush();
            
        } catch(Exception ex) {
            throw new OperacionNoRealizadaException( "error.infraestructure.fs", ex);
        
        } finally {
            if( bin != null) { try { bin.close(); } catch( Exception ex) {} }
            else { try { in.close(); } catch( Exception ex) {} }
            if( out != null) { try { out.close(); } catch( Exception ex) {} }
        }
        
    }
    
    public static String getMd5( InputStream in) { 
        final byte[] buffer = new byte[4*1024];
        
        BufferedInputStream bin = null;
        
        try { 
            final MessageDigest md = MessageDigest.getInstance( "MD5");
        
            bin = new BufferedInputStream( in);
            
            int leidos;
            while( (leidos = bin.read( buffer)) != -1) {
                md.update(buffer, 0, leidos);
            }
                       
            byte[] messageDigest = md.digest(); 
  
            BigInteger no = new BigInteger( 1, messageDigest); 
            String hashtext = no.toString(16); 
  
            while (hashtext.length() < 32) { 
                hashtext = "0" + hashtext; 
            }
            
            return hashtext; 
        
        } catch (Exception ex ) { 
            throw new OperacionNoRealizadaException( "error.infraestructura", ex); 
        
        } finally {
            UtilStream.close( bin);
            UtilStream.close(  in);
        }
    }
    
    public static String getMd5( String pathFile) throws FileNotFoundException {
        return getMd5( new FileInputStream( new File ( pathFile)));
    } 
        
    public static InfoArchivo obtenerInfoArchivo( DocumentoFirmado infoDocumento, boolean isUpload) {
        
        final String path = isUpload? obtenerRutaUpload( infoDocumento) : obtenerRutaDeposito( infoDocumento);
        final File file = new File( path);
        
        if( file.exists()) {
                    
            final InfoArchivo info = new InfoArchivo();
        
            info.setPath( path);
            info.setNombre( file.getName());
            info.setLongitud( file.length());
            
            try {
                info.setContenido( new BufferedInputStream( new FileInputStream( file)));
            } catch( FileNotFoundException ex) {
                throw new OperacionNoRealizadaException( "error.negocio.entidad.inexistente", ex);
            }
            
            return info;
        
        } else {
            throw new OperacionNoRealizadaException( "error.negocio.entidad.inexistente");
        
        }
    }
       
    static String obtenerRutaDeposito( DocumentoFirmado info) {
        IConfiguracionService configService = AppContext.getBean( IConfiguracionService.class);
        IStreamService        streamService = AppContext.getBean(        IStreamService.class);
        
        final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        return streamService.obtenerPathDeposito( pathRepositorio, info.getFolio(), info.getNombre());
    }
    
    
    public static File obtenerFileUploaded( DocumentoFirmado info) {
        final String path = obtenerRutaUpload( info);
        return new File( path);
    }
    
    static String obtenerRutaUpload( DocumentoFirmado info) {
        IConfiguracionService configService = AppContext.getBean( IConfiguracionService.class);
        IStreamService        streamService = AppContext.getBean(        IStreamService.class);
        
        final String pathRepositorio = configService.getPropiedad( "path.directorio.descarga");
        return streamService.obtenerPathDeposito( pathRepositorio, info.getSolicitud().getFolio(), info.getNombre());
    }
    
    public static DataHandler createInstance(final InputStream inputStream, final String nombre, final String tipo) {
    
        return new DataHandler( new DataSource() {
            @Override
            public InputStream getInputStream() throws IOException {
                return inputStream;
            }

            @Override
            public OutputStream getOutputStream() throws IOException {
                throw new UnsupportedOperationException( "Operacion no soportada");
            }

            @Override
            public String getContentType() {
                if ( "cert".equals( tipo)) {
                    return "application/x-x509-ca-cert";
                
                } else if( "key".equals( tipo)) {
                    return "application/pkcs8";
                
                } else if( "document".equals( tipo)) {
                    return "application/pdf";
                
                } else {
                    throw new IllegalArgumentException( "Tipo de contenido no válido: " + tipo);
                
                }
            }

            @Override
            public String getName() {
                return nombre;
            }
        });
    }
    
}
