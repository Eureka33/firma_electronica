package com.meve.ofsp.certificado;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import mx.com.neogen.commons.exception.OperacionNoRealizadaException;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.stereotype.Service;

@Service("certificadoDigitalService")
public class CertificadoDigitalService implements ICertificadoDigitalService {

	/**
	 * 	Carga información de certificado en instancia X509Certificate.
	 *  Cierra el flujo de entrada al terminar.
	 */
	public X509Certificate cargar(InputStream inputStream) {
		try {
			return (X509Certificate)
			CertificateFactory.getInstance("X.509").
			generateCertificate( inputStream);
		
		} catch( Exception ex) {
			throw new OperacionNoRealizadaException(
				"Error al crear certificado a partir de flujo de datos", ex
		    );
		} finally {
			try { inputStream.close(); } catch( Exception ex) { ex.printStackTrace(); }
		}
	}
	
	/**
	 * 	Carga información de certificado en instanca X509Certificate.
	 */
	public X509Certificate cargar(	final String infoBase64) {
		if( infoBase64 == null || infoBase64.trim().isEmpty()) {
			throw new IllegalArgumentException(
				"Información de certificado nula o vacía"
			);
		}
		
		return cargar(new ByteArrayInputStream( Base64.decode( infoBase64)));
	}
	
		
	public SujetoX509 extraerSujeto( 	final X509Certificate certificado,
										final SeccionX509Enum seccion) {
		
		SujetoX509 sujeto = new SujetoX509();
		
		try {
			X500Name x500Name = null;
			
			switch (seccion) {
			case SUJETO:
				x500Name = new JcaX509CertificateHolder(certificado).getSubject();
				break;
			case EMISOR:
				x500Name = new JcaX509CertificateHolder(certificado).getIssuer();
				break;
			}
			
			sujeto.setCN( 				getX500Property( x500Name, BCStyle.CN));
			sujeto.setSerialNumber( 	getX500Property( x500Name, BCStyle.SERIALNUMBER));
			sujeto.setUniqueIdentifier(	getX500Property( x500Name, BCStyle.UNIQUE_IDENTIFIER));
				
		} catch( Exception ex) {
			ex.printStackTrace();

		}
		
		return sujeto;
	}
	
	
	protected String getX500Property (final X500Name x500Name,
			final ASN1ObjectIdentifier identificador) {

		if ( x500Name.getRDNs( identificador).length > 0) {
			final RDN rdn = x500Name.getRDNs( identificador)[0];
			return IETFUtils.valueToString( rdn.getFirst().getValue());
		}

		return null;
	}


}