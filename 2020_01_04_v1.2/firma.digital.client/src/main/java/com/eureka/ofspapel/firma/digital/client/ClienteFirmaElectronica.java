package com.eureka.ofspapel.firma.digital.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.eureka.firma.digital.ws.service.Firma;
import com.eureka.firma.digital.ws.service.IFirmaElectronica;
import com.eureka.firma.digital.ws.service.InfoArchivo;
import com.eureka.firma.digital.ws.service.InfoConfidencial;
import com.eureka.firma.digital.ws.service.RespuestaFirma;
import com.eureka.firma.digital.ws.service.SolicitudFirma;


public class ClienteFirmaElectronica implements ActionListener {

	private final static String SERVICIO_WEB_FIRMA_URL =
        "http://localhost:8080/digitalSignWS/servicios/FirmaElectronica";
	
	private Map<String, ?> modelo;
	
	
	public ClienteFirmaElectronica( Map<String, ?> modelo) {
		super();
		this.modelo = modelo;
	}
	
	public void actionPerformed(ActionEvent e) {
		JTextArea textArea = ((JTextArea) modelo.get( "firma"));
		textArea.setText( "");
		
		String pathArchivo = ((JTextField) modelo.get( "archivo")).getText();
		String cadena      = (modelo.get( "cadena") == null)? null : ((JTextField) modelo.get( "cadena")).getText();
		
		String pathCertificado = ((JTextField) modelo.get( "certificado")).getText();
		String pathLlavePrivada = ((JTextField) modelo.get( "llave")).getText();
		String password = ((JTextField) modelo.get( "password")).getText();
		
		RespuestaFirma respuesta = null;
		
        try {
			respuesta = invocarServicio( pathArchivo, pathCertificado, pathLlavePrivada, password);

			if ( respuesta.getCodigo() != 0) {
                StringBuilder strb = new StringBuilder();
				
				strb.append("URL: ").append( SERVICIO_WEB_FIRMA_URL).append("\n");
				strb.append( "código: ").append( respuesta.getCodigo());
				strb.append( "\nmensaje: ").append( respuesta.getMensaje());
				
				textArea.setText( strb.toString());
			
			} else {
				StringBuilder strb = new StringBuilder();
				
				strb.append("peticion: {\n\turl:").append( SERVICIO_WEB_FIRMA_URL).append("\n}\n");
				strb.append( "respuesta: {");
                strb.append( "\n\tcódigo: ").append( respuesta.getCodigo());
				strb.append( ",\n\tmensaje: ").append( respuesta.getMensaje());
				
				Firma firma = respuesta.getFirma();
				
				strb.append( ",\n\ttitular: ").append( firma.getTitular());
				strb.append( ",\n\tcurp: ").append( firma.getCurp());
				strb.append( ",\n\trfc: ").append( firma.getRfc());
				strb.append( ",\n\tfecha: ").append( firma.getFecha());
				strb.append( ",\n\tfirma: ").append( firma.getFirmaElectronica());
				strb.append( "\n\turlDescarga: ").append( firma.getUrlDescarga());
                strb.append( "\n}");
                
                textArea.setText( strb.toString() );
			}

		} catch( Exception ex) {
			StringBuilder strb = new StringBuilder( "ERROR DE INVOCACIÓN:");
			
			printStackTrace(strb, ex);
			
			textArea.setText( strb.toString());
		}
	
	}

	private void printStackTrace( StringBuilder strb, Throwable tirable) {
	
		if ( tirable != null) {
			strb.append( "\n - - - - - - - - - - - - - - - - - - - - - - - - - -");
			strb.append( "\n\t[CAUSA]: ").append( tirable.getMessage());
			strb.append( "\n - - - - - - - - - - - - - - - - - - - - - - - - - -");
			
			StackTraceElement[] stack = tirable.getStackTrace();
			
			if ( stack != null) {
				for ( int i = 0; i < stack.length; ++i) {
					strb.append( "\n").append( stack[i]);
				}
			}
			
			printStackTrace(strb, tirable.getCause());
		}
	}
	
	public RespuestaFirma invocarServicio( 	String pathArchivo,
											String pathCertificado,
											String pathLlavePrivada,
											String password) {
		
		JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();

    	factory.getInInterceptors().add(new LoggingInInterceptor());
    	factory.getOutInterceptors().add(new LoggingOutInterceptor());
    	
    	factory.setServiceClass( IFirmaElectronica.class);
    	factory.setAddress( SERVICIO_WEB_FIRMA_URL);
    	
    	IFirmaElectronica servicio =(IFirmaElectronica) factory.create();

     	SolicitudFirma solicitud = new SolicitudFirma();
    	
    	solicitud.setInfoConfidencial(
    		getInfoConfidencial(pathCertificado, pathLlavePrivada, password)
    	);
    	
    	if( pathArchivo!= null && !pathArchivo.trim().isEmpty()) {
    		solicitud.setArchivoDatos(		getArchivoDatos(pathArchivo));
    	}
    	
    	RespuestaFirma respuesta = servicio.firmarArchivo( solicitud);
    
    	return respuesta;
	}
	
	
	
	private static InfoConfidencial getInfoConfidencial( String pathCertificado, String pathLlavePrivada, String password) {
		final InfoConfidencial info = new InfoConfidencial();
		
		info.setPasswordLlave( password);
		
		InfoArchivo archivo = new InfoArchivo();
		info.setArchivoLlave( archivo);
		
		archivo.setHandler( new DataHandler( new FileDataSource(new File( pathLlavePrivada))));	
		archivo.setNombre( "llavePrivada");
		archivo.setExtension( "key");
		
		
		archivo = new InfoArchivo();
		info.setCertificado( archivo);
		
		archivo.setHandler( new DataHandler( new FileDataSource(new File( pathCertificado))));	
		archivo.setNombre( "certificadoPublico");
		archivo.setExtension( "cer");
		
		return info;
	}
	
	private static InfoArchivo getArchivoDatos( String pathArchivo) {
		InfoArchivo archivo = new InfoArchivo();
		
		System.out.println( "Creating DataHandler for File: " + pathArchivo);
		
		archivo.setHandler( new DataHandler( new FileDataSource(new File( pathArchivo))));	
		archivo.setNombre( "ArchivoDatos");
		archivo.setExtension( "pdf");
		
		return archivo;
	}
}
