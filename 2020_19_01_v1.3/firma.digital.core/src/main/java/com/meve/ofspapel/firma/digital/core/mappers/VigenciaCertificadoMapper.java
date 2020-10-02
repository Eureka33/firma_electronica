package com.meve.ofspapel.firma.digital.core.mappers;

import com.meve.ofspapel.firma.digital.core.entidades.VigenciaCertificado;


public interface VigenciaCertificadoMapper {

	/**
	 * Obtiene el certificado en base al numero serial indicado.
	 * @param serial	numero serial de un certificado
	 * @return			
	 * 	El registro de vigencia del certificado encontrado o null si no existe 
	 * un registro en la base de datos. 
	 * 					
	 */
	VigenciaCertificado findVigenciaCert( 		final String serial,
														final String rfc);
	
	VigenciaCertificado findVigenciaCertBySerial( final String serial);
	
	void actualizaVigenciaCertificado( 	final VigenciaCertificado vigencia);
	
	void insertaVigenciaCertificado( 	final VigenciaCertificado vigencia);
	
	
}