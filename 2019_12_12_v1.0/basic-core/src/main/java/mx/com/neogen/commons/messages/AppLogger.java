package mx.com.neogen.commons.messages;

/**
 * 	This class is just a connector to a real loggin framework.
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public class AppLogger {

	public static AppLogger getLogger( Class<?> clase) {
		return new AppLogger();
	}
	
	
	public void info(	final Object data) {
		System.out.println( data);
	}
	
	public void error(	final Object data) {
		System.err.println( data);
	}
	public void error(	final Object data, final Throwable causa) {
		error( data);
		causa.printStackTrace();
	}
}
