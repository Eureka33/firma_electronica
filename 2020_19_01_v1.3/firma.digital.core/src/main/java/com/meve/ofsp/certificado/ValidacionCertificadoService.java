package com.meve.ofsp.certificado;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import mx.com.neogen.commons.exception.OperacionNoRealizadaException;
import mx.neogen.log.Log;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.RevokedStatus;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;

@Service("validacionCertificadoService")
public class ValidacionCertificadoService implements IValidacionCertificadoService {

	@Autowired
	private ICertificadoDigitalService certificadoService;
	
	@Autowired
	private IConfiguracionService configService;
	
	
	/**
	 * 	Obtiene estatus de certificado a partir de cadena Base64
	 */
	public StatusCertificadoEnum obtenerEstatus(
									final String 				   infoBase64,
									final TipoCertificadoEnum tipoCertificado) {
		
		return obtenerEstatus( certificadoService.cargar( infoBase64), tipoCertificado);

	}


	/**
	 * Obtiene estatus de certificado a partir de certificado X509Certificate
	 */
	public StatusCertificadoEnum obtenerEstatus(
									final X509Certificate certificado,
									final TipoCertificadoEnum tipoCertificado) {

		// Obtiene el emisor del certificado
		final SujetoX509 emisor = certificadoService.extraerSujeto( certificado, SeccionX509Enum.EMISOR);

		// Obtiene los posibles certificados raiz
		final List<X509Certificate> certificadosRaiz = obtenCertificadosRaiz( emisor, tipoCertificado);
		final String ocspURL = obtenerURLServicioOCSP( tipoCertificado);

		StatusCertificadoEnum estatus = null;

		for(X509Certificate certificadoRaiz : certificadosRaiz) {
			estatus =  invocarServicioOCSP( certificado, certificadoRaiz, ocspURL);
			if ( estatus == StatusCertificadoEnum.VALIDO) {
				return estatus;
			}
		}

		return estatus;

	}
	
	
	
	protected List<X509Certificate> obtenCertificadosRaiz( 
			final SujetoX509 emisor,
			final TipoCertificadoEnum tipoCertificado) {

		final File directorio = new File(
			configService.getPropiedad( "path.certificados.raiz." + tipoCertificado.getIdentificador())
		);

		X509Certificate rootCert = null;
		SujetoX509 sujetoRaiz    = null;
		List<X509Certificate> certificados = new ArrayList<X509Certificate>();
		InputStream inputStream = null;
		
		final File[] contenido = directorio.listFiles();
		if( contenido == null || contenido.length == 0 ) {
			throw new OperacionNoRealizadaException(
				"No se encontraron certificados raiz." +
				"\nTipo Certificado	: " + tipoCertificado.getIdentificador() +
				"\nDirectorio      	: " + directorio.getAbsolutePath() + 
				"\nEmisor			: " + emisor
			);
		}

		for( File fileCertificado : contenido) {
			try {
				inputStream = new FileInputStream( fileCertificado);

			} catch (Exception ex) {
				throw new OperacionNoRealizadaException(
						"Certificado raiz no puede abrirse para lectura", ex
						);
			}

			rootCert = certificadoService.cargar( inputStream);
			sujetoRaiz = certificadoService.extraerSujeto( rootCert, SeccionX509Enum.SUJETO);

			if ( (emisor.getUniqueIdentifier() == null && sujetoRaiz.getUniqueIdentifier() == null) ||
					(emisor.getUniqueIdentifier() != null &&
					(emisor.getUniqueIdentifier().equals( sujetoRaiz.getUniqueIdentifier())) 
							) ) {
				certificados.add( rootCert);
			} 
		}
		
		if ( certificados.isEmpty()) {
			throw new OperacionNoRealizadaException(
				"No se encontraron certificados raiz." +
				"\nTipo Certificado	: " + tipoCertificado.getIdentificador() +
				"\nDirectorio      	: " + directorio.getAbsolutePath() + 
				"\nEmisor			: " + emisor
			);
		}
		
		return certificados;
	}

	protected String obtenerURLServicioOCSP( final TipoCertificadoEnum tipoCertificado) {

		return configService.getPropiedad( "url.servicio.ocsp." + tipoCertificado.getIdentificador());

	}

	
	/** 
	 * Invoca el servicio OCSP, devuelve el estatus del certificado o genera
	 * una excepción. 
	 */
	protected  StatusCertificadoEnum invocarServicioOCSP(	
										final X509Certificate 	  certificado,
										final X509Certificate certificadoRaiz,
										final String 	  	  ocspEndpointURL) { 

		OCSPReq ocspRequest = null;
		
		Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
		
		Log.info( "Servicio OCSP: " + ocspEndpointURL);
		
		try {
			//Generamos el ID del cetificado: 
			// 	serial = certificado.serialNumber
			//  holder = certificado raíz

			final CertificateID id = new CertificateID(
					new JcaDigestCalculatorProviderBuilder().setProvider( "BC").
					build().get( CertificateID.HASH_SHA1), 
					new X509CertificateHolder( certificadoRaiz.getEncoded()), 
					certificado.getSerialNumber()
					);

			//Instanciamos el generador de requestOCSP
			final OCSPReqBuilder ocspRequestBuilder = new OCSPReqBuilder();
			ocspRequestBuilder.addRequest(id);

			//create details for nonce extension
			//final BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
			//final Extension ext = new Extension(
			//	OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, 
			//	new DEROctetString(nonce.toByteArray())
			//);
			//ocspPeticionBuilder.setRequestExtensions(new Extensions(new Extension[] { ext }));


			//Se genera la petición con el certificado a verificar y su número de serie
			ocspRequest = ocspRequestBuilder.build();

		} catch( Exception ex) {			
			throw new OperacionNoRealizadaException(
				"Error al crear petición para el servicio OCSP", ex
			);

		}

		//Se establece la conexión HTTP con el ocsp del DNIe
		HttpURLConnection connection = null;
		DataOutputStream dataOut = null;
		InputStream in  = null;

		try {
			connection = (HttpURLConnection) new URL( ocspEndpointURL).openConnection();

			// propiedades de la petición HTTP
			connection.setRequestProperty( "Content-Type", "application/ocsp-request");
			connection.setRequestProperty( "Accept", "application/ocsp-response");
			connection.setDoOutput(true);

			dataOut = new DataOutputStream(new BufferedOutputStream(
				connection.getOutputStream()
			));
				
			// Se envia la información de la petición
			dataOut.write(ocspRequest.getEncoded());
			dataOut.flush();

			try { dataOut.close();  } catch( Exception intEx) { intEx.printStackTrace(); }
						
			// parseo de respuesta
			in = (InputStream) connection.getContent();

			final OCSPResp ocspRespuesta = new OCSPResp( in);
			
			return  getEstatusCertificado( ocspRespuesta);

		} catch( OperacionNoRealizadaException onrEx) {
			throw onrEx;
	
		} catch ( Exception ex) {
			throw new OperacionNoRealizadaException(
				"Error de Invocación al Servidor OCSP", ex
			);

		} finally {
			try { in.close();	 	} catch( Exception intEx) { intEx.printStackTrace(); }
			try { connection.disconnect();	} catch( Exception intEx) { intEx.printStackTrace(); }
		}
		
	}
		
	
	/**
	 *  Obtiene el estatus del certificado o genera una excepcion.
	 */
	protected StatusCertificadoEnum getEstatusCertificado( OCSPResp respuesta) {

		String mensaje = null;
		
		switch ( respuesta.getStatus()) {
		
		case OCSPResp.SUCCESSFUL:
			
			BasicOCSPResp basicOCSPResp = null;
			try {
				basicOCSPResp = (BasicOCSPResp) respuesta.getResponseObject();
				
			} catch (Exception ex) {
				throw new OperacionNoRealizadaException(
					"[Respuesta OCSP] Petición tratada correctamente: Error " +
					"al obtener objeto de respuesta"
				);
			}
			
			final SingleResp[] responses = basicOCSPResp.getResponses();

			if (responses.length == 1) {
				final SingleResp resp = responses[0];
				final Object status = resp.getCertStatus();
				
				if (status == CertificateStatus.GOOD) {
					return StatusCertificadoEnum.VALIDO;
			
				} else if (status instanceof RevokedStatus) {
					return StatusCertificadoEnum.REVOCADO;
				
				} else {
					return StatusCertificadoEnum.DESCONOCIDO;					
				}
			} 
			
			throw new OperacionNoRealizadaException(
				"[Respuesta OCSP] Petición tratada correctamente: SITUACION DESCONOCIDA"
			);

		
		case OCSPResp.MALFORMED_REQUEST:
			mensaje = "PETICION MAL FORMADA";				break;
			
		case OCSPResp.INTERNAL_ERROR:				
			mensaje = "ERROR INTERNO DEL SERVIDOR";			break;
			
		case OCSPResp.TRY_LATER:
			mensaje = "REPETIR LA PETICION MAS TARDE";		break;
			
		case OCSPResp.SIG_REQUIRED:
			mensaje = "PETICION DEBE SER FIRMADA";			break;
				
		case OCSPResp.UNAUTHORIZED:
			mensaje = "PETICION OCSP NO ESTA AUTORIZADA";	break;
		
		default: 
			mensaje = "TIPO DE RESPUESTA DESCONOCIDO";
		}
		
		throw new OperacionNoRealizadaException("[Respuesta OCSP] Error: " + mensaje);
	}
	
}