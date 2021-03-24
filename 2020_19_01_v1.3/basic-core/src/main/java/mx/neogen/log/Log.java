package mx.neogen.log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public final class Log {

	private static int messageNumber;
	
	
	private Log() {
		super();
	}
	
	
	public static void info( final Object data) {
		System.out.println( "[" + messageNumber++ + "] " + data); 
	}

    public static void error( Throwable causa) {
        DateFormat formatter = new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss");
        
        System.err.println( "[" + formatter.format( new Date()) + "]" );
        causa.printStackTrace( System.err);
    }
}