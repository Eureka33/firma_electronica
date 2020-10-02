package mx.com.neogen.commons.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;

import mx.com.neogen.commons.messages.AppLogger;

/**
 *  Funciones utiles al tratar con archivos
 *
 * @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public class UtilStream {
	
	public static final AppLogger LOG = AppLogger.getLogger( UtilStream.class);
	
	/**
	 * 	Busca el archivo como recurso utilizando el cargador de classes
	 * 
	 * @param fileName
	 * @return
	 */
	public static InputStream obtenerRecursoAsStream( String fileName) {
		// 	Obtiene el classloader relacionado con la aplicacion actual
		ClassLoader cLoader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = cLoader.getResourceAsStream( fileName);

		if( inputStream == null) {
			LOG.error( "Recurso no encontrado: " + fileName);
		}
		
		return inputStream;
	}
		
	/**
	 *	Lee el contenido de un archivo buscandolo como un recurso utilizando 
	 * el cargador de clases.
	 * 
	 * @param fileName	Nombre del recurso
	 * @return	Un listado de lineas leidas del archivo.
	 */

	public static List<String> leerRecurso( String fileName) {

		BufferedReader entrada = new BufferedReader(
			new InputStreamReader( obtenerRecursoAsStream( fileName) )
		);

		List<String> contenido = new LinkedList<String>();
		String linea = null;

		try {
			while ( (linea = entrada.readLine()) != null) {
				contenido.add( linea);
			}

			return contenido;

		} catch( IOException ioEx) {
			LOG.error( "Error al leer recurso " + fileName, ioEx);
			return null;

		} finally {
			close( entrada);
		}
	}
	
	public static byte[] leerArchivo( String filePath) {
		return leerArchivo( new File( filePath));
	}
	
	public static byte[] leerArchivo( File file) {

		BufferedInputStream inputStream = null;

		try {
			inputStream = new BufferedInputStream( new FileInputStream( file));
			byte[] buffer = new byte[ (int) file.length()];

			return (inputStream.read( buffer) == buffer.length)? buffer : null;

		} catch( IOException ioEx) {
			LOG.error(
				"Al intentar leer archivo " + file.getAbsolutePath(), ioEx
			);
			return null;

		} finally {
			close( inputStream);
		}
		
	}
	
	
	public static List<String> leerLineasArchivo( String filePath) {
		return leerLineasArchivo( new File( filePath));
	}
	
	public static List<String> leerLineasArchivo( File file) {
		if ( file == null || (!file.exists() || !file.isFile())) {
			throw new IllegalArgumentException(
				"The argument is null or not represents a valid file: " + file
			); 
		}
		
		BufferedReader entrada = null;

		List<String> contenido = new LinkedList<String>();
		String linea = null;

		try {
			entrada =  new BufferedReader( new FileReader( file) );
			
			while ( (linea = entrada.readLine()) != null) {
				contenido.add( linea);
			}

			return contenido;

		} catch( IOException ioEx) {
			LOG.error( "Error al leer recurso " + file, ioEx);
			return null;

		} finally {
			close( entrada);
		}
	}
		
	public static void close( Reader reader) {
		if( reader == null) {
			return;
		}
		
		try {
			reader.close();
		} catch( IOException ioEx) {
			LOG.error( "Al intentar cerrar flujo de lectura:", ioEx);
		}
		
	}
	
	
	public static void close( Writer writer) {
		if( writer == null) {
			return;
		}
		
		try {
			writer.flush();
		} catch( IOException ioEx) {
			LOG.error( "Al intentar flush(ear) flujo de escritura:", ioEx);
		}
		
		try {
			writer.close();
		} catch( IOException ioEx) {
			LOG.error( "Al intentar cerrar flujo de escritura:", ioEx);
		}
		
	}
	
	
	public static void close( InputStream inputStream) {
		if ( inputStream == null) {
			return;
		}
		
		try{
			inputStream.close();
		} catch( IOException ioEx) {
			LOG.error( "Al intentar cerrar flujo de lectura:", ioEx);
		}
	}


	public static  void close( OutputStream outputStream) {
		if ( outputStream == null) {
			return;
		}
		
		try{
			outputStream.flush();
		} catch( IOException ioEx) {
			LOG.error( "Al intentar flush(ear) flujo de escritura:", ioEx);
		}
		
		try{
			outputStream.close();
		} catch( IOException ioEx) {
			LOG.error( "Al intentar cerrar flujo de escritura:", ioEx);
		}
		
	}
	
	/**
	 * 	Obtiene la extension de un archivo, sin el punto
	 *  Por ejemplo, para la ruta ".../alfa/beta/archivo.patito" 
	 *  , este método regresa ".patito"
	 * 
	 */
	public static String getExtensionArchivo( String fileName) {
		final int idx = obtenerIndiceExtension( fileName);      
        return  fileName.substring( idx + 1).toLowerCase();
	}
    
    public static String getNombreArchivo( String fileName) {
        final int idx = obtenerIndiceExtension( fileName);
        return fileName.substring( 0, idx);
    }
    
    private static int obtenerIndiceExtension( String fileName) {
        if( fileName == null) {
            throw new IllegalArgumentException( "No se acepta un nombre de archivo nulo");
        }
        
        int idx = fileName.lastIndexOf( '.');
        
        if( idx < 0) {
            throw new IllegalArgumentException( "El nombre de archivo no incluye extensión");
        }
        
        return idx;
    }
    
}