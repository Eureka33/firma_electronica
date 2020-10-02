package com.eureka.firma.digital.ws.core;

import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.Resultado;

public interface IFirmaDigitalService {

	Resultado<PublicKey> validaCertificado( 	String certificadoBase64, 
												Firma firma);
	
	Resultado<PrivateKey> validaLlavePrivada( 	String llavePrivadaBase64, String password);
	
	
	Resultado<Void> validaPareja( PrivateKey llavePrivada, PublicKey llavePublica);
	
	Resultado<Firma> generaFirma( 	final PrivateKey llavePrivada,
									final Firma firma,
									final File archivo,
									final String cadena
	);
	
}
