package mx.eureka.firma.digital.bean;

import com.eureka.firma.digital.ws.core.IStreamService;
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
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.com.neogen.commons.util.UtilStream;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;

public class UtilDocumento {

    public static BeanInfoDocumento requestToInfoDocumento( HttpServletRequest request) throws UnsupportedEncodingException {
		final BeanInfoDocumento info = new BeanInfoDocumento();
		
		info.setFolio(  request.getParameter(  "folio"));
     
        final String queryString = request.getQueryString();
        
        final int idx = queryString.indexOf( "&nombre=");
        int endIdx = queryString.indexOf( '&', idx + 8);
        endIdx = (endIdx < 0)? queryString.length() : endIdx;
		info.setNombre( URLDecoder.decode( request.getQueryString().substring( idx + 8, endIdx), "UTF-8"));
		
		return info;
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
            for ( Object nextItem : items) {
                FileItem item = (FileItem) nextItem;
                    
                if ( !item.isFormField()) {
                    
                    if ( "documento".equals( item.getFieldName())) {
                        final InfoArchivo archivo = new InfoArchivo();
                        
                        archivo.setNombre( item.getName());
                        archivo.setContenido( item.getInputStream());
                        
                        bean.addArchivo( archivo);
                        
                    } else if( "certificado".equals( item.getFieldName())) {
                        bean.setCertificado( item.getInputStream());
                    
                    } else if ( "llavePrivada".equals( item.getFieldName())) {
                        bean.setLlavePrivada( item.getInputStream());
                    
                    }
                 
                } else {
             
                    if( "password".equals( item.getFieldName())) {
                        bean.setPassword( item.getString().trim());
                    
                    } else if ( "correo".equals( item.getFieldName())) {
                        bean.setCorreo( item.getString().trim());
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
    
    public static InfoArchivo obtenerInfoArchivo( BeanInfoDocumento infoDocumento) {
        final String rutaDeposito = obtenerRutaDeposito( infoDocumento);
        final File file = new File( rutaDeposito);
        
        if( file.exists()) {
                    
            final InfoArchivo info = new InfoArchivo();
        
            info.setPath( rutaDeposito);
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
       
    static String obtenerRutaDeposito( BeanInfoDocumento info) {
        IConfiguracionService configService = AppContext.getBean( IConfiguracionService.class);
        IStreamService        streamService = AppContext.getBean(        IStreamService.class);
        
        final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
        return streamService.obtenerPathDeposito( pathRepositorio, info.getFolio(), info.getNombre());
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
                    throw new IllegalArgumentException( "Tipo de contenido no valido: " + tipo);
                
                }
            }

            @Override
            public String getName() {
                return nombre;
            }
        });
    }
    
}
