package com.eureka.firma.digital.web;

import com.eureka.firma.digital.web.beans.RequestItem;
import mx.com.neogen.commons.interfaces.ICRUDService;
import mx.com.neogen.commons.interfaces.IConsultableService;
import mx.com.neogen.commons.interfaces.IItemCatalogoService;
import mx.com.neogen.commons.interfaces.IManageService;
import mx.com.neogen.commons.util.UtilText;
import mx.eureka.firma.digital.bean.AppContext;
import org.springframework.stereotype.Component;

@Component
public class ComponentLocator {
	
	
	public IConsultableService getConsultableService( RequestItem item) {
		
		final String clave =  item.getModulo() + UtilText.capitalize( item.getEntidad()) + "ServiceImpl";
	
		try {
			return AppContext.getBean( clave, IConsultableService.class);
			
		} catch ( Exception ex) {
			throw new RuntimeException(	"No fue posible encontrar el servicio: " + clave, ex);
			
		}
	}
	

	public ICRUDService getCRUDService(RequestItem item) {
		
		final String clave =  item.getModulo() + UtilText.capitalize( item.getEntidad()) + "ServiceImpl";
		
		try {
			return AppContext.getBean( clave, ICRUDService.class);
			
		} catch ( Exception ex) {
			throw new RuntimeException(	"No fue posible encontrar el servicio: " + clave, ex);
			
		}
	}
	

	
	public IManageService getManageService(RequestItem item) {
		
		final String clave =  item.getModulo() + UtilText.capitalize( item.getEntidad()) + "ServiceImpl";
		
		try {
			return AppContext.getBean( clave, IManageService.class);
			
		} catch ( Exception ex) {
			throw new RuntimeException(	"No fue posible encontrar el servicio: " + clave, ex);
			
		}
	}


	public IItemCatalogoService getCatalogService( RequestItem requestItem) {
		
		final String clave = requestItem.getModulo() + "CatalogServiceImpl";
		
		try {
			return AppContext.getBean( clave, IItemCatalogoService.class);
			
		} catch ( Exception ex) {
			throw new RuntimeException(	"No fue posible encontrar el servicio: " + clave, ex);
			
		}
	}
	
}