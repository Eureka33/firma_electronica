package com.meve.ofspapel.firma.digital.beans;

import com.eurk.core.beans.consulta.Consulta;
import com.eurk.core.beans.consulta.Paginacion;
import com.eurk.core.beans.consulta.enums.EnumTipoConsulta;
import java.io.Serializable;
import java.util.List;
import mx.com.neogen.commons.exception.MetodoNoImplementadoException;
import mx.com.neogen.commons.interfaces.Invoker;
import org.apache.ibatis.session.RowBounds;


public abstract class ConsultaBase<T, ID extends Serializable> extends BaseComponent {
		
	public ConsultaBase() {
		super();
	}
	
	/**
	 * 	<p>Implementación general de la consulta de registros de un tipo de entidad</p>
	 * 
	 * 	@param claveOrganizacion <p>Clave de la organización a la que se solitica la información</p>	
	 * 	@param invocador		<p>Usuario que solicita la información</p>
	 *  @param consulta		<p>Instancia de {@link Consulta} con información para limitar la búsqueda</p>
	 * 	
	 *  @return <p>Listado de entidades resultado de la consulta.</p>
	 */
	public List<T> consultar(	String claveOrganizacion, Invoker invocador, Consulta consulta) {
			
		final EnumTipoConsulta tipoConsulta = consulta.getTipo();
		final List<String> ordenacion = createOrdenacion( consulta);
		
		if( tipoConsulta == EnumTipoConsulta.AUTOCOMPLEATADA) {
			// devuelve primer pagina total de resultados
			consulta.getPaginacion().setPagina( 1);
			consulta.getPaginacion().setTotalItems( contarItems( consulta, invocador));
		
			return listarItems( consulta, invocador, ordenacion, getRowBounds( consulta, tipoConsulta));
			
		} else if ( tipoConsulta == EnumTipoConsulta.PAGINADA) {	
			if( consulta.getPaginacion().getPagina() == 1 || Boolean.TRUE.equals( consulta.getPropiedades().getPropiedad("actualizarTotalItems"))) {
				consulta.getPaginacion().setTotalItems( contarItems( consulta, invocador));
			}
			
			List<T> items = listarItems( consulta, invocador, ordenacion, getRowBounds( consulta, tipoConsulta));
			
			int pagina =  consulta.getPaginacion().getPagina();
			if( items.isEmpty() && pagina > 1 ) {
				// se ha solicitado una pagina que ya no tiene items (debido a eliminación)
				consulta.getPaginacion().setPagina( pagina - 1);
				items = listarItems( consulta, invocador, ordenacion, getRowBounds( consulta, tipoConsulta));
			}

			return items;

		} else {
			return listarItems( consulta, invocador, ordenacion, getRowBounds( consulta, tipoConsulta));
		}
	
	}
	
	/**
	 *	<p>Busca registro del tipo de entidad T, utilizando los criterios de consulta indicados.</p>
	 *	<p><ul>
	 *		<li>Este método debe ser implementado para que el método consultar() funcione correctamente</li>
	 *	</ul></p>
	 * 
	 * 	@param consulta 	<p>Instancia de {@link Consulta} con información para limitar la búsqueda</p>
	 *  @param invocador	<p>Usuario que solicita la información</p>
	 *  @param rowBounds 	<p>Objeto propio de MyBatis, identifica la pagina de resultados solicitada</p>
	 * 	
	 *  @return <p>Listado de entidades resultado de la consulta.</p>
	 */
	protected List<T> listarItems( Consulta consulta, Invoker invocador, List<String> ordenacion, RowBounds rowBounds) {
		throw new MetodoNoImplementadoException( "Este método debe ser implementado por una clase hija");
	}
	
	/**
	 * 	<p>Cuenta los items que cumplen con los criterios de consulta indicados.</p>
	 * 	<p><ul>
	 *		<li>Este método debe ser implementado para que el método consultar() funcione correctamente</li>
	 *	</ul></p>
	 *
	 *  @param consulta		<p>Instancia de {@link Consulta} con información para limitar la búsqueda</p>
	 * 	@param invocador	<p>Usuario que solicita la información</p>
	 * 
	 * 	@return	<p>La cantidad de registros que cumplen los criterios de consulta</p>
	 */
	protected int contarItems( Consulta consulta, Invoker invocador) {
		throw new MetodoNoImplementadoException( "Este método debe ser implementado por una clase hija");
	}
	
	protected List<String> createOrdenacion( Consulta consulta) {
		throw new MetodoNoImplementadoException( "Este método debe ser implementado por una clase hija");
	}
	
	/**
	 * 	<p>Obtiene la entidad identificada por el valor idItem.</p>
	 * 
	 *  @param claveOrganizacion <p>Clave de la organización a la que se solitica la información</p>	
	 * 	@param invocador		<p>Usuario que solicita la información</p>
	 * 	@param idItem			<p>Identificador unico del item solicitado</p>
	 * 
	 * 	@return	<p>La entidad solicitada, o bien una instancia vacía del tipo de entidad T.</p>
	 */
	protected T obtenerItem(	String claveOrganizacion, Invoker invocador, ID idItem) {
		throw new MetodoNoImplementadoException( "Este método debe ser implementado por una clase hija");
	}
	
	/**
	 * 	Configura un objeto RowBounds (MyBatis) de acuerdo al tipo de consulta
	 */
	private RowBounds getRowBounds( 		final Consulta consulta,
											final EnumTipoConsulta tipoConsulta) {
		
		if ( tipoConsulta == EnumTipoConsulta.NOPAGINADA) {
			return new RowBounds( RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
		
		} else {
			final Paginacion paginacion = consulta.getPaginacion();
			return new RowBounds(
				(paginacion.getPagina() - 1) * paginacion.getItemsPagina(), consulta.getPaginacion().getItemsPagina()
			);
		}
	}
	
}