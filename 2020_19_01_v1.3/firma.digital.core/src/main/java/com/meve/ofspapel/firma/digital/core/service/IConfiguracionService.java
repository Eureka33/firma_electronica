package com.meve.ofspapel.firma.digital.core.service;


public interface IConfiguracionService {

	/**
	 * 	Obtiene el valor de la propiedad identificada con el nombre dado como
	 * argumento.
	 * 
	 * @param nombrPropiedad	Nombre de la propiedad que desea obtenerse
	 * 
	 * @return
	 * 	El valor actual de la propiedad o bien un resultado erroneo 
	 * si la propiedad no se encuentra. 
	 */
	String getPropiedad( final String nombrePropiedad);

}