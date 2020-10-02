package mx.com.neogen.commons.util;

public final class UtilStatistics {
	
	private UtilStatistics() {
		super();
	}
	
	
	/**
	 * 	Obtiene el promedio de un conjunto de valores enteros
	 * 
	 * @param valores	
	 * 	<p>
	 * 		Conjunto de valores que serán promediados
	 * 	</p>
	 * @return
	 * 	<p>
	 * 		El promedio de los valores, como un número entero resultado de la 
	 *  	operacion sum(valores)/ número_valores
	 *  </p> 
	 */
	public static int intAverage( Integer[] valores){
		
		int promedio = 0;
		
		for ( int valor : valores) {
			promedio += valor;
		}
		promedio /= valores.length;
		
		return promedio;
	}
}
