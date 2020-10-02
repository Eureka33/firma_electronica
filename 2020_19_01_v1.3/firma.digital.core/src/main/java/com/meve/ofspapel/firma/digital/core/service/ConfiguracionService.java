package com.meve.ofspapel.firma.digital.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.meve.ofspapel.firma.digital.core.mappers.ConfiguracionMapper;

@Service( "configuracionService")
public class ConfiguracionService implements IConfiguracionService {

	@Autowired
	private ConfiguracionMapper configuracionDAO;
		
	@Transactional( readOnly=true)
	public String getPropiedad( String nombrePropiedad) {
		return configuracionDAO.findPropiedad( nombrePropiedad);
		
	}
	
}