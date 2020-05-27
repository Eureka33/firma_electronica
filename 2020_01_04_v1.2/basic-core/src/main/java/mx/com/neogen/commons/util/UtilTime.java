/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mx.com.neogen.commons.util;

import java.util.Date;

/**
 *
 * @author g2marco
 */
public class UtilTime {

	private UtilTime() {
		super();
	}
	
	/**
	 * 	Regresa un objeto Date representando la fecha y hora actuales 
	 * 
	 * @return
	 *			objeto Date recien creado
	 */
	
	public static Date getFechaHoraActual() {
		return new Date();
	}

	
	/** 
	 * Obtiene la diferencia absoluta de tiempo en minutos entre dos fechas
	 * 
	 * @param fechaA
	 * @param fechaB
	 * @return
	 */
	
	public static int getLapsoMinutos( Date fechaA, Date fechaB) {
	
		long diferencia = Math.abs( fechaA.getTime() - fechaB.getTime());
		int minutos = ( int ) (diferencia /( 1000L * 60L));
		
		return minutos;
		
	}
	
	public static int getLapsoSegundos( Date fechaA, Date fechaB) {
		
		long diferencia = Math.abs( fechaA.getTime() - fechaB.getTime());
		return ( int ) (diferencia / 1000L);	
	}
	
}
