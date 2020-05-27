package mx.com.neogen.commons.util;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 *  Clases de utileria para tratar cadenas de texto
 *
 * @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public final class UtilText {

	private UtilText() {
		super();
	}

	/**
	 *	Adds the given character in the front of the string until it reaches
	 * the given length.
	 *
	 * @param fieldValue    : The original string which will be padded
	 * @param padCharacter  : The character used for padding the string
	 * @param goalLength    : Final lenght of the string
	 * @return		   		: The padded string
	 */
	public static String padWithChar( String fieldValue, char padCharacter, int goalLength) {
		String field = "";
		if ( fieldValue != null) {
			field = fieldValue;
		}
		
		StringBuilder strb = new StringBuilder( field);
		
		while( strb.length() < goalLength) {
			strb.insert( 0, padCharacter); 
		}
		
		return strb.toString();
	}

	/**
	 *	Adds the given character in the front of the int value until it reaches
	 * the given length.
	 *
	 * @param value	    	: Integer value which will be padded
	 * @param padCharacter  : The character used for padding the string
	 * @param goalLength    : Final lenght of the string
	 * @return		    	: The padded string
	 */
	public static String padWithChar( int value, char padCharacter, int goalLength) {
		return padWithChar( String.valueOf( value), padCharacter, goalLength);
	}


	/**
	 *	Limits the length of a string to maxLength.
	 *
	 * @param string		: String that is going to be limited
	 * @param maxLength		: Maximum number of characters in the string
	 * 			
	 * @return				: A limited string 
	 */
	public static String limitString( String string, int maxlength) {
		if ( string != null && (string.length() > (maxlength - 3))) {
			return string.substring( 0, maxlength - 3) + "...";
		}
		return string;
	}


	/**
	 *	Returns a string with the same data but the first character uppercase
	 */
	public static String capitalize(String data) {
		if ( data == null || data.length() == 0) {
			return data;
		}
		return new StringBuilder( data).
			replace( 0, 1, String.valueOf( data.charAt(0)).toUpperCase()).
			toString();
	}


	/**
	 * 	Returns a string with the same data but the first character lowercase
	 */
	public static String lowerize(String data) {
		if ( data == null || data.length() == 0) {
			return data;
		}

		return new StringBuilder( data).
			replace( 0, 1, String.valueOf( data.charAt(0)).toLowerCase()).
			toString();
	}

	
	/**
	 *  Crea arbol de cadenas, las cadenas contenidas en el arreglo son terminadas
	 *  con un mapa que contiene unicamente una entrada '\u0000'
	 *  
	 * 
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<Character, Map> crearArbolCadenas(	final String[] cadenas) {
		final Map<Character, Map>   arbol = new HashMap<Character, Map>();
		
		if ( cadenas == null || cadenas.length == 0) {
			return arbol;
		}
			
		Map<Character, Map>    mapa = null;
		Map<Character, Map> mapaTmp = null;
		Character          caracter = null;
		
		for( String cadena : cadenas) {
			mapa = arbol;
			for( int i = 0; i < cadena.length(); ++i) {
				caracter = Character.valueOf( cadena.charAt( i));
				mapaTmp = mapa.get( caracter);
				
				if ( mapaTmp == null) {
					mapaTmp = new HashMap<Character, Map>();
					mapa.put( caracter, mapaTmp);
				}
				
				mapa = mapaTmp;
			}
			
			mapa.put( '\u0000', null);
		}
	
		return arbol;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static boolean arbolContieneCadena( 	
										final Map<Character, Map> arbolCadena,
										final String 			 	   cadena) {
		if ( arbolCadena.isEmpty()) {
			return false;
		}

		Map<Character, Map> mapa = arbolCadena;
		Character caracter = null;
		
		for( int i = 0; i < cadena.length(); ++i) {
			caracter = Character.valueOf( cadena.charAt( i));
			mapa = mapa.get( caracter);
			if ( mapa == null) {
				return false;
			}
		}
		if ( mapa.containsKey( '\u0000')) {
			return true;
		}
		
		return false;
	}

	/**
	 * Anexa caracteres tantos caracteres TAB como sea necesario.
	 * 
	 */
	public static void appendTabs( final StringBuilder strb, final int anidamiento) {
		for ( int i = 0; i < anidamiento; ++i) {	strb.append( "    "); 	}
	}
	
	
	/**
	 * 	Anexa el stack trace de cualquier excepción a una instancia de 
	 * StringBuilder.
	 */
	public static String stackTraceToString( Throwable tirable) {

		CharArrayWriter  array = null;
		PrintWriter 		pw = null;
		
		try {
			array = new CharArrayWriter();
			pw 	= new PrintWriter( array);
			
			tirable.printStackTrace( pw);
			
			pw.flush();
			
			return array.toString();

		} catch( Exception ex) {
			ex.printStackTrace();
			
			return "";
			
		} finally {
			try { pw.close();	} catch( Exception inner) { inner.printStackTrace();}
			try { array.close();} catch( Exception inner) { inner.printStackTrace();}
		}
	
	}
	
	/**
	 *  Regresa un substring de la cadena original eliminando los caracters '/' 
	 *  al principio y/o final de la cadena (si existen).
	 *  
	 */
	public static String trimSlashes(	final String cadena) {
		
		final int low = cadena.startsWith("/") ? 1 : 0;
		final int high = cadena.length() - (cadena.endsWith("/") ? 1 : 0);

		return low < high ? cadena.substring(low, high) : "";
	}

}