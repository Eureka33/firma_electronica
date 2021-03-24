package com.eureka.firma.digital.web.rest;

import com.eureka.firma.digital.web.beans.RequestItem;
import com.eureka.firma.digital.web.components.GenericWebComponent;
import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.respuesta.RespuestaWS;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mx.com.neogen.commons.bean.Propiedades;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GenericRESTServices {
    
	@Autowired GenericWebComponent component;
	
	
	@RequestMapping(
		value = "/_data_/list/{claveModulo}/{claveEntidad}",
		method = RequestMethod.POST, consumes = "application/json", produces = "application/json"
	)
	@ResponseBody
	public RespuestaWS listarItems(	final HttpServletRequest   request, 
									final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo, 
									final @PathVariable(      "claveEntidad") String      entidad,
									final @RequestBody                          Consulta consulta) {
		return component.listar( request, response, new RequestItem( "", modulo, entidad), consulta);
	}
	
	@RequestMapping(
		value = "/_data_/getItem/{claveModulo}/{claveEntidad}/{idItem}",
		method = RequestMethod.POST, consumes="application/json", produces="application/json"
	)
	@ResponseBody
	public RespuestaWS getItem(		final HttpServletRequest   request,
									final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo,
									final @PathVariable(      "claveEntidad") String      entidad,
									final @PathVariable(            "idItem") String       idItem,
									final @RequestBody Propiedades					  propiedades) {
			
		return component.getItem( request, response, new RequestItem( "", modulo, entidad, idItem), propiedades);
	}
	
	@RequestMapping(
		value = "/_data_/get/{claveModulo}/{claveEntidad}/{idItem}",
		method = RequestMethod.GET, produces = "application/json"
	)
	@ResponseBody
	public RespuestaWS get(			final HttpServletRequest   request,
									final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo,
									final @PathVariable(      "claveEntidad") String      entidad,
									final @PathVariable(            "idItem") String       idItem) {
		
		return component.getItem( request, response, new RequestItem( "", modulo, entidad, idItem), null);
	}
	
	@RequestMapping(
		value = "/_data_/create/{claveModulo}/{claveEntidad}",
		method = RequestMethod.POST, consumes = "application/json", produces = "application/json"
	)
	@ResponseBody
	public RespuestaWS crear(		final HttpServletRequest   request, 
									final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo, 
									final @PathVariable(      "claveEntidad") String      entidad) {
		
		return component.crear( request, response, new RequestItem( "", modulo, entidad));
	}
	
	@RequestMapping(
		value = "/_data_/save/{claveModulo}/{claveEntidad}",
		method = RequestMethod.POST, consumes = "application/json", produces = "application/json"
	)
	@ResponseBody
	public RespuestaWS actualizar(	final HttpServletRequest   request,
									final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo, 
									final @PathVariable(      "claveEntidad") String      entidad) {
		
		return component.actualizar( request, response, new RequestItem( "", modulo, entidad));
	}
	
	@RequestMapping(
		value = "/_data_/delete/{claveModulo}/{claveEntidad}/{idItem}",
		method = RequestMethod.POST, consumes = "application/json", produces = "application/json"
	)
	@ResponseBody
	public RespuestaWS borrar(		final HttpServletRequest   request, 
									final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo, 
									final @PathVariable(      "claveEntidad") String      entidad,
									final @PathVariable(            "idItem") String       idItem,
									final @RequestBody Propiedades                    propiedades) {
		
		return component.borrar( request, response, new RequestItem( "", modulo, entidad, idItem), propiedades);
	}
	
	@RequestMapping( 
		value="/_data_/manage/{claveModulo}/{claveEntidad}",
		method=RequestMethod.POST,consumes = "application/json", produces="application/json")
	@ResponseBody
	public RespuestaWS gestionar(	final HttpServletRequest   request, 
			                        final HttpServletResponse response,
									final @PathVariable(       "claveModulo") String       modulo, 
									final @PathVariable(      "claveEntidad") String      entidad) {
		
		return component.manage( request, response, new RequestItem( "", modulo, entidad));
	}
	
}