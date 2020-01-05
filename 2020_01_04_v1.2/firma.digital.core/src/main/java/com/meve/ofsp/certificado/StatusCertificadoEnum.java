package com.meve.ofsp.certificado;

/**
 * <p>Define los estatus en los que se puede encontrar un certificado 
 * digital</p>
 * 
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 */
public enum StatusCertificadoEnum {
	VALIDO,				// vigente y activo
	CADUCADO,			// no vigente
	DESCONOCIDO,		// certificado no reconocido por una AC
	REVOCADO,			// vigente pero cancelado por la AC
}
