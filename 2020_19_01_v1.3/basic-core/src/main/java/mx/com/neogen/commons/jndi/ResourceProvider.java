package mx.com.neogen.commons.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import mx.com.neogen.commons.exception.RecursoNoEncontradoException;

/**
 *  Esta clase busca y provee recursos JNDI solicitados
 *
 * @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public final class ResourceProvider {

	private ResourceProvider() {
		super();
	}

	/**
	 *	Recupera el recurso JNDI del contexto de la aplicacion.
	 * 
	 * @param <T>		- Tipo de objeto que debe ser devuelto
	 * @param jndiId	- Identificador del recurso solicitado
	 * @return			- El recurso solicitado de tipo T
	 *
	 * @throws <@link RecursoNoEncontradoException> en caso de que el recurso
	 *	solicitado no pueda ser obtenido por alguna causa.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T obtenRecurso(String jndiId) {
		if ( jndiId == null || jndiId.trim().length() == 0) {
			throw new IllegalArgumentException( "Identificador JNDI nulo o vacio");
		}

		T recurso = null;

		try {
			Context initialContext = new InitialContext();

			recurso = (T) initialContext.lookup( jndiId);

		} catch (NamingException namingEx) {
			throw new RecursoNoEncontradoException(
				"Error al obtener el recurso: " + jndiId, namingEx
			);
		}

		if ( recurso == null) {
			throw new RecursoNoEncontradoException(
				"Se obtuvo null al intentar obtener el recurso: " + jndiId
			);
		}

		return recurso;
	}
}
