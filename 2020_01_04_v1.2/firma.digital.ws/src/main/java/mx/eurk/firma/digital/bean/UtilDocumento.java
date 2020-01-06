package mx.eurk.firma.digital.bean;

import com.meve.firma.digital.ws.core.IStreamService;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.exception.OperacionNoRealizadaException;

public class UtilDocumento {

    public static BeanInfoDocumento requestToBean( HttpServletRequest request) {
		final BeanInfoDocumento info = new BeanInfoDocumento();
		
		info.setFolio( request.getParameter( "folio"));
		info.setNombre( request.getParameter( "nombre"));
		
		return info;
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
            if( bin != null) { try { bin.close(); } catch( Exception ex) {} }
            else { try { in.close(); } catch( Exception ex) {} }
        }
    
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
    
}
