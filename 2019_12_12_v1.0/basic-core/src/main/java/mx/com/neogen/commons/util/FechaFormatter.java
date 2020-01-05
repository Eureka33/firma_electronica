package mx.com.neogen.commons.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import mx.com.neogen.commons.exception.FormatoInvalidoException;

/*
 *	Esta clase solo sirve para formatear objetos de Fecha (java.util.Date)
 * como cadenas de texto.
 *  También se incluyen metodos para parsear cadenas de texto y o
 * 
 */

public class FechaFormatter {
	
	private static final Locale mxLocale = new Locale( "es", "MX");
	
	public static final SimpleDateFormat FORMAT_DMA_NUM_GUION = 
		new SimpleDateFormat( "dd-MM-yyyy", mxLocale );
	
	public static final SimpleDateFormat FORMAT_DMA_NUM_DIAGONAL = 
		new SimpleDateFormat( "dd/MM/yyyy", mxLocale );

	public static final SimpleDateFormat FORMAT_DMA_HMS_NUM_GUION = 
		new SimpleDateFormat( "dd-MM-yyyy HH:mm:ss", mxLocale );
	
	public static final SimpleDateFormat FORMAT_DMA_HMS_NUM_DIAGONAL = 
		new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss", mxLocale );

	public static final SimpleDateFormat  FORMAT_DS_DMA_TEXT= 
		new SimpleDateFormat( "E, dd 'de' MMMM 'de' yyyy" , mxLocale );
	
	public static final SimpleDateFormat FORMAT_DS_DMA_HMS = 
		new SimpleDateFormat( "E, dd MMMM yyyy HH:mm:ss", mxLocale );
	
	
	// Esta clase solo cuenta con métodos de utileria
	private FechaFormatter() {
		super();
		
	}
	
	
	/**
	 * 	Fomatea un objeto Date como: dd/mm/yyyy 
	 * 
	 */
	
	public static String formatFecha( Date fecha ) {

		String fechaString = null;

		if ( fecha != null ) {
			
			fechaString = FORMAT_DMA_NUM_DIAGONAL.format( fecha);

		} else {
			// si la fecha es nula, regresa un string vacio
			
			fechaString = "";

		}

		return fechaString;

	}
	
	/**
	 * 	Formatea la fecha recibida utilizando el string de formato indicado
	 * 
	 */
	public static String formatFecha( Date fecha, String stringFormat) {
		String fechaString = null;
		
		if (fecha != null) {
			SimpleDateFormat 
			formato = new SimpleDateFormat( stringFormat, mxLocale);
			
			fechaString = formato.format( fecha); 
		
		} else {
			
			fechaString = "";
			
		}
		
		return fechaString;
		
	}

	public static Date parseFechaJSON( String fecha) {

		try {
			if ( fecha.length() > 16) {
				if ( fecha.indexOf( 'T') > 0) {
					return new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse( fecha);
				} else {
					return new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSSSSSS").parse( fecha);
				}
			}
			if ( fecha.length() > 10) {
				return new SimpleDateFormat( "dd/MM/yyyy HH:mm").parse( fecha); 
			} else if ( fecha.contains( "-")){
				return new SimpleDateFormat( "yyyy-MM-dd").parse( fecha);
			} else {
				return new SimpleDateFormat( "dd/MM/yyyy").parse( fecha);
			} 
		
		} catch ( Exception ex) {
			ex.printStackTrace();
			return null;
		}
		
	}
	public static String formatToJSON( Date timeStamp) {

		return new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format( timeStamp);
	}

	
	public static Date applyDefaultTimeZone( 	final Date timestamp ) {
		TimeZone tz = TimeZone.getDefault();
		return new Date(  
			timestamp.getTime() + tz.getRawOffset() + tz.getDSTSavings()
		);
	}
	
	
	
	public static Date disApplyDefaultTimeZone( 	final Date timestamp ) {
		TimeZone tz = TimeZone.getDefault();
		return new Date(  
			timestamp.getTime() - tz.getRawOffset() - tz.getDSTSavings()
		);
	}
	
	public static Date disApplyDefaultTimeZone( 	final String timestamp ) {
		try {
			if ( timestamp.contains( "T")) {
				
				return disApplyDefaultTimeZone(  
						new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").
						parse( timestamp)
					);
			} else {
				return disApplyDefaultTimeZone(  
					new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss.SSS").
					parse( timestamp)
				);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException( "Error al parsear la fecha: " + timestamp +
			", usando el formato: yyyy-MM-dd HH:mm:ss.SSS");
	} 
	
	/**
	 * 	Fomatea un objeto Date como: dd/mm/yyyy HH:mm:ss
	 * 
	 */
	
	public static String formatFullFecha( Date fecha ) {

		String fechaString = null;

		if ( fecha != null ) {
			
			fechaString = FORMAT_DMA_HMS_NUM_DIAGONAL.format( fecha);

		} else {
			// si la fecha es nula, regresa un string vacio
			
			fechaString = "";

		}

		return fechaString;

	}	
	
	/**
	 * 	Formatea un objeto Date como: DIA MES AÑO HORAS:MINUTOS
	 *
	 */
	
	public static String formatTimeStamp( Date fecha) {

		String fechaString = "";

		if ( fecha != null ) {
			fechaString = FORMAT_DS_DMA_HMS.format( fecha);
			
		}

		return fechaString;
		
	}

	
	/**
	 * 	Formatea un objeto Date como: dia mes a�o 
	 *
	public static String formatFechaDia( Date fecha) {

		String fechaString = null;

		if ( fecha != null ) {
			
			fechaString = formatterVista.format( fecha);

		} else {
			// si la fecha es nula, regresa un string vacio
			
			fechaString = "";

		}

		return fechaString;
		
	}

	*/
	
	/**
	 *	Parsea un String de fecha en el formato dd-mm-yyyy o bien en el 
	 * formato dd/mm/yyyy, devolviendo el bobjeto Date correspondiente
	 * 
	 *  Una excepcion ParseException es arrojada si el string no puede ser 
	 * convertido a un objeto Date, por ello, es necesario que las llamadas 
	 * a esta clase se realizen despues de validar el formato de la fecha
	 *  
	 */
	
	public static Date parseFecha( String fechaStr) {
		
		DateFormat formater = null;
		
		if ( fechaStr.contains( "-")) {
			formater = FORMAT_DMA_NUM_GUION;
		} else {
			formater = FORMAT_DMA_NUM_DIAGONAL;
		}
	
		Date fecha = null;
		
		try {
			fecha = formater.parse( fechaStr);
			
		} catch ( ParseException pEx) {
			throw new 
				FormatoInvalidoException(" Formato de fecha no valido: " +
				fechaStr);
		}
		
		return fecha;
		
	}
	
	/**
	 *	Parsea un  un string timestamp dd-mm-yyyy HH:MM:ss o bien en el 
	 * formato dd/mm/yyyy HH:MM:ss, devolviendo el objeto Date correspondiente
	 * 
	 *  Una excepcion ParseException es arrojada si el string no puede ser 
	 * convertido a un objeto Date, por ello, es necesario que las llamadas 
	 * a esta clase se realizen despues de validar el formato de la fecha
	 *  
	 */
	
	public static Date parseFechaTimeStamp( String timeStampStr) {
		
		DateFormat formater = null;
		
		if ( timeStampStr.contains( "-")) {
			formater = FORMAT_DMA_HMS_NUM_GUION;
		} else {
			formater = FORMAT_DMA_HMS_NUM_DIAGONAL;
		}
	
		Date timeStamp = null;
		
		try {
			timeStamp = formater.parse( timeStampStr);
			
		} catch ( ParseException pEx) {
			throw new FormatoInvalidoException(
				"Formato de fecha no valido: " + timeStampStr, pEx
			);
		}
		
		return timeStamp;
		
	}
	
	
	/**
	 * 	Devuelve un objeto fecha con la fecha actual ( iniciando a las 00:00)
	 * 
	 *
	public static Date getFechaInicioHoy() {
		
		Date hoy = new Date();
		Date inicioHoy = null;
		
		try {
			inicioHoy = parseFecha( formatter.format( hoy));
		
		} catch (Exception e) {
			// Se espera que esta llamada no falle
		}
		
		return inicioHoy;
		
	}
	
	
	/**
	 *	Obtiene un objeto Date que representa el inicio del día indicado por
	 * el string de fecha. La fecha puede indicarse como dd-mm-yyyy o bien 
	 * como dd/mm/yyyy
	 *	El objeto fecha regresado tendra la siguiente información
	 *		dd mm yyy 00:00:00:000
	 * 
	 */
	
	public static Date getFechaInicio( String fechaDDmmyy) {
		
		return parseFecha( fechaDDmmyy );
	
	}
	
	/**
	 * 	Otiene un objeto Date que representa el fin del día indicado por el 
	 * string de fecha. La fecha puede indicarse como dd-mm-yyyy o bien como 
	 * dd/mm/yyyy
	 *	El objeto fecha regresado tendrá la siguiente información
	 *		dd mm yyyy 23:59:59
	 * 
	 */
	
	public static Date getFechaFin( String fechaDia) {
		
		return parseFechaTimeStamp( fechaDia + " 23:59:59");
		
	}
	
	/*
	 *	Toma un string de hora y minutos en el formato HH:MM y lo convierte 
	 * en un long que representa la hora como un numero de milisegundos
	 * 
	 */
	
	public static long getHorainMillis( String  hhmmStr) {
	
		String hhStr = hhmmStr.substring(0, 2);
		String mmStr = hhmmStr.substring(3, 5);
		
		long hora = Long.valueOf( hhStr);
		long mins = Long.valueOf( mmStr);
		
		
		return (1000L * 60L) * ((60L * hora)  + mins);
		
	}
	
}
