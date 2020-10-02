package mx.com.neogen.commons.util;

public final class UtilSystem {
	
	private UtilSystem() {
		super();
	}
	
	
	/**
	 * 	Obtiene el ClassLoader del contexto actual o bien el del sistema.
	 * 
	 */
	public static ClassLoader getClassLoader (){
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		
		if ( loader != null) {
			return loader;
		}
		
		return ClassLoader.getSystemClassLoader();
	}
}
