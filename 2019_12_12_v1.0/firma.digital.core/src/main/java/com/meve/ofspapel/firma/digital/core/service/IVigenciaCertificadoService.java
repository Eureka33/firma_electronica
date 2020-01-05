package com.meve.ofspapel.firma.digital.core.service;

import com.meve.ofspapel.firma.digital.core.entidades.VigenciaCertificado;

import mx.com.neogen.commons.bean.Resultado;



public interface IVigenciaCertificadoService {

	Resultado<VigenciaCertificado> encuentraCertificado( final String serial, 
														final String rfc);
		
	Resultado<VigenciaCertificado> encuentraCertificado( final String serial);
	
	
	Resultado<Boolean> updateFromFile( final String filePath);
	
}