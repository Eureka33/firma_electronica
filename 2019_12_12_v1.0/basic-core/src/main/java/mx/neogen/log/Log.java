package mx.neogen.log;

public final class Log {

	private static int messageNumber;
	
	
	private Log() {
		super();
	}
	
	
	public static void info( final Object data) {
		System.out.println( "[" + messageNumber++ + "] " + data); 
	}
	
}