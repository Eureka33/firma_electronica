package mx.com.neogen.commons.util;

import java.util.Date;

public final class UtilThread {
	
	private UtilThread() {
		super();
	
	}
	
	/**
	 * 	Devuelve el identificador dado por la maquina virtual al Thread 
	 * invocador. 
	 * 
	 * @return
	 * 
	 */
	
	public static long getThreadID() {
		return Thread.currentThread().getId();
		
	}
	
	
	/**
	 *	Duerme el thread actual un tiempo aleatorio de segundos entre 1 y 
	 * tiempoMaximo
	 * 
	 */
	
	public static void sleepRandom( int tiempoMaximoSegundos) {
		int tiempo = (int) (Math.random() * tiempoMaximoSegundos ) + 1;
		
		sleepSegundos( tiempo);
		
		return;
	}
	
	/**
	 *	Duerme el thread actual por almenos el número de segundos indicado
	 *		
	 * @param segundos	
	 *			Numero de segundo que el thread será puesto a dormir
	 * @return
	 *			false si el thread despertó sin ser interrumpido. Si el thread
	 *		es interrumpido por alguna razón se regresa true
	 * 
	 */
	
	public static boolean sleepSegundos( int segundos) {
		
		boolean interrupted = false;
		long tiempo = 1000L * segundos;
		
		try {
			
			Thread.sleep( tiempo);
			
			
		} catch( InterruptedException iEx) {
			
			interrupted = true;
						
		}
		
		return interrupted;
		
	}
	
	/**
	 *	Duerme el thread actual por almenos el número de minutos indicado
	 *		
	 * @param minutos
	 *			Numero de minutos que el thread será puesto a dormir
	 * @return
	 *			true si el thread despertó sin ser interrumpido. Si el thread
	 *		es interrumpido por alguna razón se regresa false.
	 * 
	 */
	
	public static boolean sleepMinutes( int minutos) {
		
		return sleepSegundos( 60*minutos);
		
	}

	/**
	 * 	Stops current thread a minimum of 'delayTime' milliseconds. This
	 * method does not return until the desired delay has been met. 
	 *
	 * @param delayTime		delay time in millisecs
	 */
	public static void delay( final long delayTime) {
		
		final long startTime = new Date().getTime();
		long  tiempoFaltante = delayTime;
		
		do {
			try {
				Thread.sleep( tiempoFaltante);

			} catch ( InterruptedException iEx) {
				// It must wait at least 'delayTime' milliseconds
			}
			
			long elapsedTime = new Date().getTime() - startTime;
			
			tiempoFaltante = delayTime - elapsedTime;
			
		} while( tiempoFaltante > 0L);
		
	}

}