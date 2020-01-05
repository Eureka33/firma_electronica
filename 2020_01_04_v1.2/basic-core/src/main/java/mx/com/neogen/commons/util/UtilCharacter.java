package mx.com.neogen.commons.util;

public class UtilCharacter {
	private static final char[] HEX_DIGITS = 
	{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
	  'a', 'A', 'b', 'B', 'c', 'd', 'D', 'e', 'E', 'f', 'F'	
	};

	public static boolean isHexadecimal( char caracter) {
		for ( char hexDigit : HEX_DIGITS) {
			if ( caracter == hexDigit) {
				return true;
			}
		}
		
		return false;
	}
}
