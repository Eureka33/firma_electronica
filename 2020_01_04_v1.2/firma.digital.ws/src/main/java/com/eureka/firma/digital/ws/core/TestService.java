package com.eureka.firma.digital.ws.core;

import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {
	
	@Autowired
	private IConfiguracionService configService;
	

	public void printPropiedades() {
		
        System.out.println( configService.getPropiedad( "algoritmo.digest.cipher"));
		System.out.println( configService.getPropiedad( "algoritmo.digest.cipher"));

	}
}
