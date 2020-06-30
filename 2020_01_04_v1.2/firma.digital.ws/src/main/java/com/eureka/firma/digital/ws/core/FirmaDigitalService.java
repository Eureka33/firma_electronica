package com.eureka.firma.digital.ws.core;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.ssl.PKCS8Key;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateHolder;
import org.bouncycastle.cert.ocsp.BasicOCSPResp;
import org.bouncycastle.cert.ocsp.CertificateID;
import org.bouncycastle.cert.ocsp.CertificateStatus;
import org.bouncycastle.cert.ocsp.OCSPException;
import org.bouncycastle.cert.ocsp.OCSPReq;
import org.bouncycastle.cert.ocsp.OCSPReqBuilder;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.bouncycastle.cert.ocsp.SingleResp;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.bean.Resultado;
import com.meve.ofspapel.firma.digital.core.service.IConfiguracionService;
import mx.com.neogen.commons.util.UtilStream;
import mx.neogen.log.Log;

@Service("firmaDigitalService")
public class FirmaDigitalService implements IFirmaDigitalService {
	
	@Autowired
	private IConfiguracionService configService;
	

    @Override
	public Resultado<Firma> generaFirma(	final PrivateKey llavePrivada,
											final Firma firma,
											final File archivo, 
											final String cadena) {
		try {		
			
			byte[] firmaDigital = null;
			if ( archivo != null) {
				firmaDigital = obtenFirmaDigital( 
					new FileInputStream( archivo), llavePrivada, configService.getPropiedad( "algoritmo.digest.cipher")
				);
			} else {
				firmaDigital = obtenFirmaDigital( 
					new ByteArrayInputStream( cadena.getBytes()), llavePrivada, configService.getPropiedad( "algoritmo.digest.cipher")
				);
			}
			
			final Resultado<Firma> resultado = ResultadoEnum.OK.getResultado( "Firma digital exitosa");
			
			firma.setFecha( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss").format(new Date()));
			firma.setFirmaElectronica( Base64.encodeBase64String( firmaDigital));
			
			resultado.setResultado( firma);
			
			return resultado;
			
		} catch ( Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_FIRMADO.getResultado( "[" + ex.getMessage() +"]");
		}

	}
	
    @Override
	public Resultado<PublicKey> validaCertificado(
											final String certificadoBase64, 
											final Firma firma) {
		InputStream fis = null;
		X509Certificate certificado = null;
		
		try {
			fis = new ByteArrayInputStream( Base64.decodeBase64( certificadoBase64));
			certificado = (X509Certificate)	CertificateFactory.getInstance("X.509").generateCertificate( fis);		

		} catch ( Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_VALIDACION.getResultado(
				"La información del certificado no puede leerse."
			);

		} finally {
			try { fis.close(); } catch( Exception ex) { ex.printStackTrace(); }
		}
		
		try {
			X500Name x500name = new JcaX509CertificateHolder(certificado).getSubject();
		
			RDN rdn = x500name.getRDNs(BCStyle.CN)[0];
			firma.setTitular( IETFUtils.valueToString( rdn.getFirst().getValue() ));
		
			rdn = x500name.getRDNs(BCStyle.SERIALNUMBER)[0];
			firma.setCurp( IETFUtils.valueToString( rdn.getFirst().getValue() ));
		
			rdn = x500name.getRDNs(BCStyle.UNIQUE_IDENTIFIER)[0];
			firma.setRfc( IETFUtils.valueToString( rdn.getFirst().getValue() ));
		
		} catch( Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_VALIDACION.getResultado(
				"Imposible obtener información del titular del certificado"
			);
		}
		
		// Checa las fechas de vigencia
		Date today = new Date();
		
		Date fecha = certificado.getNotBefore();
		if ( today.before( fecha)) {
			return ResultadoEnum.ERROR_VALIDACION.getResultado(
				"La vigencia del certificado no ha iniciado aún."
			);
		}
		
		fecha = certificado.getNotAfter();
		if ( today.after( fecha)) {
			return ResultadoEnum.ERROR_VALIDACION.getResultado(
				"La vigencia del certificado ya ha concluido."
			);
		}
		
		// valida estatus con el servidor OCSP
        
		Resultado<String> validacion = validaCertificado( certificado);
        
        if ( validacion.isError()) {
			return ResultadoEnum.ERROR_VALIDACION.getResultado( validacion.getMensaje());
		}
		
		final PublicKey key =  certificado.getPublicKey();
		
		Resultado<PublicKey> resultado = ResultadoEnum.OK.getResultado("");
		resultado.setResultado( key);
		return resultado;
	}
	
	public Resultado<PrivateKey> validaLlavePrivada(
											final String llavePrivadaBase64,
											final String password) {
		
		DataInputStream fis = null;

		try {
			ByteArrayInputStream bais =  new ByteArrayInputStream( Base64.decodeBase64( llavePrivadaBase64));
			
			fis = new DataInputStream( bais);

			final byte[] keyBytes = new byte[bais.available()];
			fis.readFully( keyBytes);
			
			// If the provided InputStream is encrypted, we need a password to decrypt
			// it. If the InputStream is not encrypted, then the password is ignored
			// (can be null).  The InputStream can be DER (raw ASN.1) or PEM (base64).
			PKCS8Key pkcs8 = new PKCS8Key( keyBytes, password.toCharArray() );

			// If an unencrypted PKCS8 key was provided, then this actually returns
			// exactly what was originally passed in (with no changes).  If an OpenSSL
			// key was provided, it gets reformatted as PKCS #8 first, and so these
			// bytes will still be PKCS #8, not OpenSSL.
			byte[] decrypted = pkcs8.getDecryptedBytes();
			PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec( decrypted );

			// A Java PrivateKey object is born.
			PrivateKey key = null;
			if ( pkcs8.isDSA()) {
				key = KeyFactory.getInstance( "DSA" ).generatePrivate( spec );
			} else if ( pkcs8.isRSA()) {
				key = KeyFactory.getInstance( "RSA" ).generatePrivate( spec );
			}

			Resultado<PrivateKey> resultado = ResultadoEnum.OK.getResultado("");
			resultado.setResultado( key);
			
			return resultado;

		} catch ( Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_VALIDACION.getResultado(
				"La información de llave privada no puede leerse o el password " +
				"es incorrecto."
			);

		} finally {
			try { fis.close(); } catch( Exception ex) { ex.printStackTrace(); }
		}
		
	}
		
	public Resultado<Void> validaPareja(final PrivateKey llavePrivada,
										final PublicKey  llavePublica) {
		
		String texto = 
			"Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellente" +
			"sque vitae quam et velit gravida tempus. In viverra, dui eget lac" +
			"inia tincidunt, turpis mi commodo leo, a rhoncus quam mauris vel " +
			"nulla. Integer pulvinar lectus sem, id laoreet massa feugiat a. F" +
			"usce vitae sem ullamcorper, posuere mauris sit amet, pretium odio" +
			". Sed condimentum lorem in viverra commodo. Aenean nec orci bland" +
			"it, interdum quam nec, luctus magna. Pellentesque id felis fermen" +
			"tum, laoreet arcu id, dapibus diam. Suspendisse aliquet turpis se" +
			"m, at suscipit mauris blandit non. Morbi sapien quam, luctus ut e" +
			"ros at, lacinia posuere velit. Vivamus non ipsum magna. Integer s" +
			"agittis erat nec neque elementum consequat. Etiam consectetur sus" +
			"cipit nunc, in ornare lorem fermentum in. Mauris sit amet orci du" +
			"i. Curabitur cursus blandit tempor. ";
		
		try {		
			
			final String algoritmo = configService.getPropiedad( "algoritmo.digest.cipher");
			if ( algoritmo == null) {
				return ResultadoEnum.ERROR_INTERNAL_CONFIG.getResultado(  
					"Algoritmo de resumen y cifrado no configurado: 'algoritmo.digest.cipher'"
				);
			}
			
			byte[] firmaDigital = obtenFirmaDigital( 
				new ByteArrayInputStream( texto.getBytes()), llavePrivada, algoritmo
			);

			boolean isValido = verificaFirmaDigital(
				new ByteArrayInputStream( texto.getBytes()), firmaDigital, llavePublica, algoritmo
			);
			
			if ( isValido) {
				return ResultadoEnum.OK.getResultado("Verificación de pareja exitosa");
			} else {
				return ResultadoEnum.ERROR_VALIDACION.getResultado(
					"La llave privada no corresponde al certificado."
				);
			}
			
		} catch ( Exception ex) {
			ex.printStackTrace();
			
			return ResultadoEnum.ERROR_VALIDACION.getResultado(
				"Imposible completar la operación de acoplamiento de llave y " +
				"certificado."
			);
		}
	}
	
	protected byte[] obtenFirmaDigital(		final InputStream dataInputStream,
											final PrivateKey privateKey,
											final String algoritmo) throws Exception {

		Signature dsa = Signature.getInstance( algoritmo);
		dsa.initSign( privateKey);

		final  BufferedInputStream bufin = new BufferedInputStream( dataInputStream);
		final byte[] buffer = new byte[1024];
		int len;
		while (bufin.available() != 0) {
			len = bufin.read(buffer);
			dsa.update(buffer, 0, len);
		}

		bufin.close();

		// Obtiene la firma digital (encripta la huella digital)
		return dsa.sign();
	}
	
	protected boolean verificaFirmaDigital( InputStream informacion,
											byte[] firmaDigital,
											PublicKey llavePublica,
											String algoritmo) throws Exception {

		Signature sig = Signature.getInstance( algoritmo);
		sig.initVerify( llavePublica);

		BufferedInputStream bufin = new BufferedInputStream( informacion);

		byte[] buffer = new byte[1024];
		int len;
		while (bufin.available() != 0) {
			len = bufin.read(buffer);
			sig.update(buffer, 0, len);
		}

		bufin.close();

		// 4) Verifica la firma digital original contra la firma digital
		// generada con la llave pública.
		return sig.verify( firmaDigital);
	}
	
	
	protected Resultado<String> validaCertificado( X509Certificate certificado) {
    	
		final String pathCertificadosRaiz = configService.getPropiedad( "path.certificados.raiz");
		if ( pathCertificadosRaiz == null) {
			return ResultadoEnum.ERROR_INTERNAL_CONFIG.getResultado(  
				"Trayecto de certificados raíz no configurado: 'path.certificados.raiz'"
			);
		}
		
		final String urlServicioOCSP = configService.getPropiedad( "url.servicio.ocsp");
		if ( urlServicioOCSP == null) {
			return ResultadoEnum.ERROR_INTERNAL_CONFIG.getResultado(  
				"URL de servicio ocsp no configurada: 'url.servicio.ocsp'"
			);
		}
		
		try {
			final File directorioRootCerts = new File( pathCertificadosRaiz);
			final CertificateFactory cf = CertificateFactory.getInstance("X.509");
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

			Resultado<String> resultado = null;

			for ( File certiFile : directorioRootCerts.listFiles()) {
				InputStream isCertificadoRoot = null;
				
				try {
					isCertificadoRoot = new FileInputStream( certiFile);
					X509Certificate rootCert = (X509Certificate) cf.generateCertificate(isCertificadoRoot);
					
					//Generamos el ID del cetificado que estamos buscando
					final CertificateID id = new CertificateID(
						new JcaDigestCalculatorProviderBuilder().setProvider( "BC").build().get(CertificateID.HASH_SHA1), 
						new X509CertificateHolder( rootCert.getEncoded()), 
						certificado.getSerialNumber()
					);

					//Instanciamos el generador de requestOCSP
					final OCSPReqBuilder ocspPeticionBuilder = new OCSPReqBuilder();
					ocspPeticionBuilder.addRequest( id);

					//create details for nonce extension
					//final BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
					//final Extension ext = new Extension(
					//	OCSPObjectIdentifiers.id_pkix_ocsp_nonce, true, 
					//	new DEROctetString(nonce.toByteArray())
					//);
					//ocspPeticionBuilder.setRequestExtensions(new Extensions(new Extension[] { ext }));

					//Se genera la petición con el certificado a verificar y su número de serie
					final OCSPReq ocspPeticion = ocspPeticionBuilder.build();

					//Se establece la conexión HTTP con el ocsp del DNIe
					final URL url = new URL( urlServicioOCSP);
                    final HttpURLConnection con = (HttpURLConnection)url.openConnection();

                    //Se configuran las propiedades de la petición HTTP
					con.setRequestProperty("Content-Type", "application/ocsp-request");
					con.setRequestProperty("Accept", "application/ocsp-response");
					con.setDoOutput(true);
                    
                    DataOutputStream dataOut = null;
                    
                    try {
                        final OutputStream out = con.getOutputStream();
                        dataOut = new DataOutputStream( new BufferedOutputStream( out));
                        
                        //Se obtiene la respuesta del servidos OCSP del DNIe
                        dataOut.write( ocspPeticion.getEncoded());
                    
                    } finally {
                        UtilStream.close( dataOut);
                    }

                    int estado = -1;
                    
					//Se parsea la respuesta y se obtiene el estado del certificado retornado por el OCSP
                    InputStream in = null;
                    OCSPResp ocspRespuesta = null;
					
                    try {
                        in = (InputStream) con.getContent();
                        ocspRespuesta = new OCSPResp( in);
                        
                        estado = ocspRespuesta.getStatus();
                        
                    } finally {
                        UtilStream.close( in);
                    }
                    
                  
					if (estado == OCSPResp.SUCCESSFUL) {
						resultado =  tratarRespuestaOK( ocspRespuesta);

					} else if (estado==OCSPResp.MALFORMED_REQUEST) {
						resultado = ResultadoEnum.ERROR_VALIDACION.getResultado( 
							"OCSP-RESPONDER: ERROR. PETICION MAL FORMADA"
						);
					
					} else if (estado==OCSPResp.INTERNAL_ERROR) {
						resultado = ResultadoEnum.ERROR_VALIDACION.getResultado( 
							"OCSP-RESPONDER: ERROR. ERROR INTERNO DEL SERVIDOR"
						);
					
					} else if (estado==OCSPResp.TRY_LATER) {
						resultado = ResultadoEnum.ERROR_VALIDACION.getResultado( 
							"OCSP-RESPONDER: ERROR. REPETIR LA PETICION MAS TARDE"
						);
					
					} else if (estado==OCSPResp.SIG_REQUIRED) {
						resultado = ResultadoEnum.ERROR_VALIDACION.getResultado( 
							"OCSP-RESPONDER: ERROR. PETICION DEBE SER FIRMADA"
						);
				
					} else if (estado==OCSPResp.UNAUTHORIZED) {
						resultado = ResultadoEnum.ERROR_VALIDACION.getResultado( 
							"OCSP-RESPONDER: ERROR. PETICION OCSP NO ESTA AUTORIZADA"
						);
					
					} else {
						resultado = ResultadoEnum.ERROR_VALIDACION.getResultado( 
							"OCSP-RESPONDER: ERROR. TIPO DE RESPUESTA DESCONOCIDO"
						);
					}
                    
                    Log.info( "resultado: (" + resultado.getCodigo() + ") " + resultado.getMensaje());
                    
					if ( resultado.getCodigo() == 0) {
						Log.info( "resultado ok");
						return resultado;
					} 
					
				} catch ( Exception ex) {
					ex.printStackTrace();
					return ResultadoEnum.ERROR_VALIDACION.getResultado("Error de Invocación al Servidor OCSP");
							
				} finally {
					try { isCertificadoRoot.close(); } catch ( Exception ex) { ex.printStackTrace(); }
				}
			}
 		    
			return resultado;
			
		} catch ( Exception ex) {
			ex.printStackTrace();
			return ResultadoEnum.ERROR_VALIDACION.getResultado("Error de Invocación al Servidor OCSP");
			
		} 
 	}

	
	protected Resultado<String> tratarRespuestaOK(final OCSPResp ocspRespuesta) throws OCSPException, IOException {

		final Object responseObject = ocspRespuesta.getResponseObject();
		final BasicOCSPResp basicOCSPResp = (BasicOCSPResp) responseObject;

		final SingleResp[] responses = basicOCSPResp.getResponses();
		if (responses.length == 1) {
			final SingleResp resp = responses[0];
			final Object status = resp.getCertStatus();
			
			if (status == CertificateStatus.GOOD) {
				return ResultadoEnum.OK.getResultado(
					"OCSP-RESPONDER: PETICION TRATADA CORRECTAMENTE: ESTADO DEL CERTIFICADO: VALIDO"
				);
		
			} else if (status instanceof org.bouncycastle.ocsp.RevokedStatus) {
				return ResultadoEnum.ERROR_VALIDACION.getResultado(
					"OCSP-RESPONDER: PETICION TRATADA CORRECTAMENTE: ESTADO DEL CERTIFICADO: REVOCADO"
				);
			
			} else {
				return ResultadoEnum.ERROR_VALIDACION.getResultado(
					"OCSP-RESPONDER: PETICION TRATADA CORRECTAMENTE: ESTADO DEL CERTIFICADO: DESCONOCIDO"
				);
				
			}
		}
		
		return ResultadoEnum.ERROR_VALIDACION.getResultado(
			"OCSP-RESPONDER: PETICION TRATADA CORRECTAMENTE: SITUACION DESCONOCIDA"
		);
	}
}
