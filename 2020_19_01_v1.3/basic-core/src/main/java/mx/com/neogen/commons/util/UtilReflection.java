package mx.com.neogen.commons.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import mx.com.neogen.commons.annotation.ExcludeFrom;
import mx.com.neogen.commons.exception.ReflectionException;

/**
 * 	Metodos útiles para aplicar reflection en instancias
 * 
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public final class UtilReflection {

	private UtilReflection() {
		super();
	}

	/**
	 * 	Este bean aloja la informacion devuelta durante la invocacion de getters
	 *  anidados
	 */
	private static class InstanciaPropiedad {
		private Object instancia;
		private String propiedad;
	}

	
	@SuppressWarnings("unchecked")
	public static <T> Class<T> getInstanceType( Object instancia) {
		return (Class<T>) instancia.getClass();
	}

	
	@SuppressWarnings("unchecked")
	public static <T> T getPropiedad( Object instancia, String nombreProp) {
		try {
			Method getter = getMetodoAccesor(instancia, "get", nombreProp);
			return (T)getter.invoke(instancia);
		} catch (Exception e) {
			throw new RuntimeException(
				"Al acceder a metodo accesor para la propiedad " + nombreProp +
				" de la clase " + instancia.getClass().getName(), e
			);
		}
		
	}

		
	/**
	 *  Devuelve todos los campos de una clase, incluidos los campos heredados 
	 *  ( excluyendo la clase Object)
	 * @param clase
	 * @return
	 */
	public static Field[] getAllFields( Class<? extends Object> clase) {
		
		Field[] campos = clase.getDeclaredFields();
		
		if( clase.getSuperclass() != Object.class) {
			Field[] camposPadre = getAllFields( clase.getSuperclass());
			if( camposPadre.length > 0) {
				Field[] nuevo = new Field[ campos.length + camposPadre.length];
				for( int i = 0; i < campos.length; ++i) {
					nuevo[i] = campos[i];
				}
				for( int i = 0; i < camposPadre.length; ++i) {
					nuevo[ i + campos.length] = camposPadre[i];
				} 
				campos = nuevo;
			}
		}
		
		return campos;
	}
	
	
	public static boolean isIterable( Class<? extends Object> tipo) {
		
		if ( tipo.equals( Iterable.class)) {
			return true;
		}
		Class<? extends Object>[] interfaces = tipo.getInterfaces();
		for ( Class<? extends Object> nextInterface: interfaces) {
			if ( isIterable( nextInterface)) {
				return true;
			}
		}
		
		return false;		
	}
	

	/**
	 * 	Determina si el tipo de dato es simple, es decir, si se trata de:
	 *  
	 *  - un tipo primitivo
	 *  - una clase del api de java
	 *  
	 *  Excepto algunas excepciones, no es necesario seguir analizando el 
	 * interior de un tipo simple.
	 * 
	 */
	public static boolean isTipoSimple( Class<? extends Object> tipo) {
		if ( tipo.isPrimitive() || tipo.isAnnotation() || tipo.isEnum()) {
			return true;
		}
		if ( tipo.getPackage().getName().startsWith( "java") ) {
			if ( ! isIterable( tipo)) {
				return true;
			}
		}
		
		return false;
		
	}
	
	
	public static Class<? extends Object> obtenTipoGenerico( String tipoGenerico) {
		
		int iniIndx = tipoGenerico.indexOf( '<') + 1;
		int endIndx = tipoGenerico.indexOf( '>');
		
		String className = tipoGenerico.substring( iniIndx, endIndx);
		try { 
			return Class.forName(className);
			
		} catch (Exception e) {
			throw new RuntimeException( e);
		}
		
	}
	

	public static String toString( Object instancia) {
		Object        valor = null;
		boolean isPrimerPropiedad = true;

		StringBuilder texto = new StringBuilder(32).
		append( instancia.getClass().getSimpleName()).append( "[" );

		try {
			for ( Method metodo: listaGetters( instancia)) {

				if ( ! isPrimerPropiedad) {
					texto.append( ", ");
				} 

				isPrimerPropiedad = false;

				valor = null;

				ExcludeFrom anotacion = obtenAnotacion( instancia.getClass(), metodo, ExcludeFrom.class);

				if (anotacion != null && anotacion.value().equals( "toString")) {					
					// Se indicó excluir la propiedad
					valor = "...";
				}

				if ( valor == null) {
					valor = metodo.invoke( instancia);
				}

				texto.append( getNombrePropiedad( metodo) );
				texto.append( "=" );
				texto.append( ((valor == null)? "null" : valor).toString());
			}

		} catch( IllegalAccessException iaEx) {

			System.err.println( "***Error de Acceso a metodo get " );
			iaEx.printStackTrace();

		} catch ( InvocationTargetException itEx) {

			System.err.println( "***Error de Invoacion a metodo get " );
			itEx.printStackTrace();

		}

		texto.append( "]");

		return texto.toString();

	}

	/**
	 *	Creación de una instancia de clase en base al nombre completamente
	 * calificado.
	 *
	 *	La clase debe contar con el constructor por default. Este constructor
	 * es utilizado para crear la nueva instancia.
	 *
	 */
	@SuppressWarnings("unchecked")
	public static <T> T creaInstancia(String className)
	throws ClassNotFoundException, NoSuchMethodException  ,
	IllegalAccessException , InvocationTargetException,
	InstantiationException   {

		Class<? extends Object> clase = Class.forName( className);
		return ( T) creaInstancia( clase);
	}


	public static <T> T creaInstancia( Class<T> clase)
	throws ClassNotFoundException, NoSuchMethodException  ,
	IllegalAccessException , InvocationTargetException,
	InstantiationException   {

		Constructor<T> constructor = clase.getConstructor();
		return ( T) constructor.newInstance();
	}

	/**
	 *  Obtiene el metodo accesor de una instancia. La firma del metodo se forma
	 *  de la siguiente forma:
	 *  	Si el prefijo no es nulo o vacio : 	prefijo + propiedad ( params )
	 *  	Si el prefijo es nulo o vacio    :  propiedad( params)
	 *
	 *   No es necesario que el nombre de la propiedad inicie con mayuscula,
	 *  incluso si se indica un prefijo no nulo ni vacio
	 *
	 * @throws  NoSuchMetodException
	 *		Si la instancia no contiene un método con el nombre formado por
	 *		el prefijo y el nombre de propiedad simple
	 */
	public static Method getMetodoAccesor(	final Object      instancia,
											final String	    prefijo,
											final String      propiedad,
											final Class<?>...    params)
											throws NoSuchMethodException {

		String nombre = prefijo.concat( UtilText.capitalize( propiedad));

		return instancia.getClass().getMethod( nombre, params);

	}

	/**
	 *	Obtiene un listado de todos los metodos públicos de una clase que
	 * que inician con 'get' o con 'is' y que no requieran parametros.
	 *
	 *  El listado regresado no incluye al método getClass()
	 *
	 */
	private static Method[] listaGetters( Object instancia) {

		Method[] metodos = instancia.getClass().getMethods();
		Method metodo    = null;
		int numeroGetters = 0;

		for ( int i = 0; i < metodos.length; ++i ) {
			metodo = metodos[ i];

			if ( isValidGetMethod( metodo) ) {
				// getter valido
				numeroGetters += 1;
			} else {
				metodos[i] = null;
			}
		}

		// Pasa todos los metodos validos a un array resultado
		Method[] getters = new Method[ numeroGetters];

		for ( int origen = 0, destino = 0 ; origen < metodos.length ; ++origen ) {
			if ( metodos[ origen] != null) {
				getters[ destino ] = metodos[ origen];
				destino += 1;
			}
		}

		return getters;

	}

	/**
	 * Checa si la propiedad o el metodo tienen la anotación indicada
	 * @param anotacion
	 * @return
	 */
	private static <T extends Annotation> T obtenAnotacion( Class<? extends Object> clase, Method metodo, Class<T> claseAnotacion) {
		// Checa si la propiedad tiene la anotacion
		String posiblePropiedad = getNombrePropiedad( metodo);
		try {
			Field[] propiedades = getAllFields( clase);
			for( Field propiedad: propiedades) {
				if (propiedad.getName().equals( posiblePropiedad)) {
					T instanciaAnotacion = propiedad.getAnnotation( claseAnotacion);
					if ( instanciaAnotacion != null) {
						return instanciaAnotacion;
					}
				}
			}			

		} catch (Exception e) {
			// No existe el campo
			e.printStackTrace();
		}
		// El campo no tiene la anotacion
		return  metodo.getAnnotation( claseAnotacion);

	}

	/**
	 *	Regresa true si el nombre del metodo inicia con 'get' o con 'is'. Este
	 * metodo regresa false si el nombre del metodo es getClass() o si el metodo
	 * no inicia con 'get o con 'is'.
	 *
	 */
	private static boolean isValidGetMethod(Method metodo) {

		String nombre = metodo.getName();

		return (metodo.getGenericParameterTypes().length == 0
				&& ((nombre.startsWith("get") && !nombre.equals("getClass"))
						|| nombre.startsWith("is")));

	}

	/**
	 *	Supone el nombre de la propiedad a partir del nombre del getter (inicia
	 * con 'get' o 'is'). 
	 *	
	 *	@return  el nombre de la propiedad
	 *
	 */
	private static String getNombrePropiedad( Method getter ){
		int startIndex = getter.getName().charAt(0) == 'g' ? 3 : 2;
		return UtilText.lowerize(getter.getName().substring( startIndex));
	}

	
	/**
	 * 	Ejecuta el metodo set inidicado por la propiedad indicada, utilizando
	 * el conjunto de parametros y valores indicado.
	 *
	 *   Una propiedad set anidada tiene la siguiente forma
	 *
	 *   	prop1.prop2.prop3
	 *
	 *   Las operaciones necesarias para ejecutar el set de esta propiedad
	 *  serian:
	 *  	instancia.getProp1().getProp2().setProp2( valores...);
	 *
	 *   El conjunto de valores puede ir de cero a cualquier numero de
	 *	parametros
	 *
	 */
	public static void ejecutaSetAnidado( Object   instanciaInicial,
			String     propiedadAnidada,
			Class<?>[]           params,
			Object...            valores)
	throws NoSuchMethodException,
	InvocationTargetException,
	IllegalAccessException {

		InstanciaPropiedad
		instanciaPropiedad = getsAnidados( instanciaInicial, propiedadAnidada);

		Method metodo = null;

		metodo = getMetodoAccesor( instanciaPropiedad.instancia,
				"set",
				instanciaPropiedad.propiedad,
				params);

		// 	Invoca el metodo elegido
		metodo.invoke( instanciaPropiedad.instancia, valores);

		return;

	}


	/**
	 * 	Ejecuta el metodo get indicado por la propiedad indicada, utilizando
	 * el conjunto de parametros y valores indicado.
	 *
	 *   Una propiedad set anidada tiene la siguiente forma
	 *
	 *   	prop1.prop2.prop3
	 *
	 *   Las operaciones necesarias para ejecutar el get de esta propiedad
	 *  serian:
	 *  	instanciaInicial.getProp1().getProp2().getProp2( valores...);
	 *
	 *   El conjunto de valores puede ir de cero a cualquier numero de
	 *	parametros
	 *
	 */
	@SuppressWarnings( "unchecked")
	public static <T> T ejecutaGetAnidado( Object instanciaInicial,
			String propiedadAnidada,
			Class<?>[]       params,
			Object...       valores) {

		InstanciaPropiedad insProp = 
			getsAnidados( instanciaInicial, propiedadAnidada);

		try {
			Method metodo = getMetodoAccesor(
					insProp.instancia, "get", insProp.propiedad, params
			);

			//	Invoca el metodo elegido
			return (T) metodo.invoke( insProp.instancia, valores);

		} catch ( Exception ex) {
			throw new ReflectionException(
					"Al intentar acceder a propiedad " + propiedadAnidada, ex
			);
		}

	}


	private static InstanciaPropiedad getsAnidados( Object instanciaInicial,
			String propiedadAnidada) {

		String[] propiedades = propiedadAnidada.split( "\\.");
		int anidamiento = propiedades.length - 1;

		Method metodo = null;
		InstanciaPropiedad insProp = new InstanciaPropiedad();
		insProp.instancia = instanciaInicial;

		for( int i = 0; i < propiedades.length; ++i) {
			insProp.propiedad = propiedades[ i];

			if ( anidamiento-- == 0) {
				break;
			}

			// Obtiene los objetos anidados
			// Ejemplo: la propiedad  info.titulo  tiene anidamiento 1
			//		mientras que la propiedad 'categoria' tiene anidamiento 0
			try {
				metodo = getMetodoAccesor( 
						insProp.instancia, "get", insProp.propiedad
				);

				// 	Invoca el metodo elegido
				insProp.instancia = metodo.invoke( insProp.instancia);

			} catch( Exception ex) {
				throw new ReflectionException( 
					"Al intentar acceder a propiedad anidada " + propiedadAnidada,
					ex
				);
			}

		}
		// En este punto 'instancia' apunta al ultimo objeto de la cadena de
		// gets y 'propiedad' tiene el nombre de la utlima propiedad en la
		// cadena

		return insProp;
	}

}
