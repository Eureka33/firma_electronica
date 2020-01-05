package com.meve.ofsp.certificado;

import java.security.cert.X509Certificate;

import mx.com.neogen.commons.exception.OperacionNoRealizadaException;

public interface IValidacionCertificadoService {
	
	/**
	 * <p>Determina el estatus de un certificado en base a:</p>
	 * <ul>
	 * 		<li>su vigencia</li>
	 * 		<li>su estatus (protocolo OCSP)</li>
	 * </ul>
	 * 	<p>Esta operación hace uso del certificado raiz del certificado</p>
	 * 
	 * @param infoBase64
	 *  <p>Información del certificado codificada en formato Base64</p>
	 *     
	 * @return
	 * 	<p>El estatus del certificado
	 *  <p>Arroja la excepcion {@link OperacionNoRealizadaException} si la 
	 *  operación no pudo ser completada correctamente por cualquier mótivo</p>
	 */
	StatusCertificadoEnum obtenerEstatus(
									final String infoBase64, 
			  						final TipoCertificadoEnum tipoCertificado);

	/**
	 * <p>Determina el estatus de un certificado en base a:</p>
	 * <ul>
	 * 		<li>su vigencia</li>
	 * 		<li>su estatus (protocolo OCSP)</li>
	 * </ul>
	 * 	<p>Esta operación hace uso del certificado raiz del certificado</p>
	 * 
	 * @param certificado
	 *  <p>Información del certificado en una instancia X509Certificate</p>
	 *     
	 * @return
	 * 	<p>El estatus del certificado
	 *  <p>Arroja la excepcion {@link OperacionNoRealizadaException} si la 
	 *  operación no pudo ser completada correctamente por cualquier mótivo</p>
	 */
	StatusCertificadoEnum obtenerEstatus(
									final X509Certificate certificado, 
									final TipoCertificadoEnum tipoCertificado);

}
