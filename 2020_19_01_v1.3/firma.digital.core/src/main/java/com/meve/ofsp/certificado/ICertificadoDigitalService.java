package com.meve.ofsp.certificado;

import java.io.InputStream;
import java.security.cert.X509Certificate;

import mx.com.neogen.commons.exception.OperacionNoRealizadaException;

/**
 * <p>Define el conjunto de operaciones disponibles en certificados 
 * digitales.</p>
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public interface ICertificadoDigitalService {

	
	/**
	 * <p>Carga la información de un certificado en una instancia de la clase
	 * X509Certificate.</p>
	 * 
	 * @param infoBase64	
	 * 	<p>Información del certificado codificada en formato Base64</p>
	 * 
	 * @return
	 * 	<p>Una instancia X509Certificate con la información del certificado</p>
	 *  <p>Arroja la excepcion {@link OperacionNoRealizadaException} si la 
	 *  operación no pudo ser completada correctamente por cualquier mótivo</p>
	 */
	X509Certificate cargar( final String infoBase64);
	
	/**
	 * <p>Carga la información de un certificado en una instancia de la clase
	 * X509Certificate.</p>
	 * 
	 * @param inputStream	
	 * 	<p>Flujo de datos con la información del certificado.</p>
	 * 
	 * @return
	 * 	<p>Una instancia X509Certificate con la información del certificado.</p>
	 *  <p>El flujo de datos de entrada es cerrado sin importar el resultado 
	 *  de este método.</p>
	 *  <p>Arroja la excepcion {@link OperacionNoRealizadaException} si la 
	 *  operación no pudo ser completada correctamente por cualquier mótivo</p>
	 */
	X509Certificate cargar( final InputStream inputStream);
	
	
	SujetoX509 extraerSujeto( 	final X509Certificate certificado,
								final SeccionX509Enum seccion) ;
	
}