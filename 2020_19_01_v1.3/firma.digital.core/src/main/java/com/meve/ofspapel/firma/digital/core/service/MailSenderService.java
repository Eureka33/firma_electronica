package com.meve.ofspapel.firma.digital.core.service;

import java.io.File;
import javax.mail.internet.MimeMessage;
import mx.neogen.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Service
public class MailSenderService {
    @Value ("${private.string.email.from}") private String emailFrom;
    
    @Autowired private JavaMailSender mailSender;
    @Autowired private IConfiguracionService configService;
    
    
    public void sendNotificacion(   String email, String url, String titular, String rfc, String fechaHora, 
                                    String nombreArchivo) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);

            helper.setFrom( emailFrom);
            helper.setTo( email);
            helper.setSubject( "Notificación de Firma de Documento");
            
            String htmlText;
            
            final String organizacion  = configService.getPropiedad( "string.organizacion.nombre");
        
            if( nombreArchivo.endsWith( ".zip")) {
                htmlText = 
                    "<div style='font-family: Arial, sans-serif;'>" +
                        "<img src=\"cid:image\"><br/><br/>" +
                        "Los documentos seleccionados fueron firmados satisfactoriamente por " + titular + 
                        " con RFC " + rfc + " en la siguiente fecha y hora " + fechaHora + ".<br/><br/>" +
                        "Estos documentos se encuentran dentro de un archivo zip que se puede descargar de la " +
                        "siguiente dirección.<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""+ url + "\">"+ url + "</a><br/><br/>" +
                        "&nbsp;&nbsp;Atentamente<br/><br/>&nbsp;&nbsp;<b>" + organizacion + "</b>" +
                    "</div>"
                ;    
            } else {
                htmlText= 
                    "<div style='font-family: Arial, sans-serif;'>" +
                        "<img src=\"cid:image\"><br/><br/>" +
                        "El documento anexo fue firmado por " + titular + " con RFC " + rfc + " en la " +
                        "siguiente fecha y hora " + fechaHora + ".<br/><br/>" +
                        "Se puede validar la autenticidad del mismo en la siguiente dirección.<br/>" +
                        "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""+ url + "\">"+ url + "</a><br/><br/>" +
                        "&nbsp;&nbsp;Atentamente<br/><br/>&nbsp;&nbsp;<b>" + organizacion + "</b>" +
                    "</div>"
                ;
            }

            helper.setText(htmlText, true);

            final String pathRepositorio = configService.getPropiedad( "path.repositorio.fs");
            final String serverName    = configService.getPropiedad( "url.server.name");
            final String webAppContext = configService.getPropiedad( "path.app.context");
            
            final Resource image = new UrlResource( serverName + webAppContext + "/images/logo_organizacion.png");
            
            helper.addInline( "image", image, "image/png");
            
            String carpeta = obtenerCarpeta( url);
            
            String filePath = pathRepositorio + File.separator + carpeta + File.separator +  nombreArchivo;
            
            helper.addAttachment( nombreArchivo, new File( filePath));
            
            
            this.mailSender.send(mime);
        
            System.out.println("notificacion enviada a: " + email);
            
        } catch( Exception ex) {
            Log.error( ex);
        }
    }
    
    public boolean sendSolicitud(   String emailDestinatario, String url, String titular, String rfc, String fechaHora, 
                                    String pathArchivo) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);

            helper.setFrom( emailFrom);
            helper.setTo( emailDestinatario);
            helper.setSubject( "Solicitud de Firma de Documento");
            
            String htmlText;
            
            final String organizacion  = configService.getPropiedad( "string.organizacion.nombre");
        
            htmlText= 
                "<div style='font-family: Arial, sans-serif;'>" +
                    "<img src=\"cid:image\"><br/><br/>" +
                    titular + " con RFC " + rfc + " solicit&oacute; la firma del documento que usted puede descargar " +
                    "y firmar en la siguiente <a href=\""+ url + "\">direcci&oacute;n</a><br/><br/>" +
                    "También puede acceder al documento copiando la siguiente direcci&oacute;n en su navegador web: " +
                    url + "<br/><br/>" +
                    "&nbsp;&nbsp;Atentamente<br/><br/>&nbsp;&nbsp;<b>" + organizacion + "</b>" +
                "</div>"
            ;
            
            helper.setText(htmlText, true);

            final String serverName      = configService.getPropiedad( "url.server.name");
            final String webAppContext   = configService.getPropiedad( "path.app.context");
            
            final Resource image = new UrlResource( serverName + webAppContext + "/images/logo_organizacion.png");
            
            helper.addInline( "image", image, "image/png");
            File archivo = new File( pathArchivo);
            
            helper.addAttachment( archivo.getName(), archivo);
                      
            this.mailSender.send(mime);
        
            System.out.println("notificacion enviada a: " + emailDestinatario);
            
            return true;
            
        } catch( Exception ex) {
            Log.error( ex);
            return false;
        }
    }
    
    
    public void sendNotificacionSolicitud(  String email, String titular, String rfc, String fechaHora, 
                                            String pathArchivo) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);

            helper.setFrom( emailFrom);
            helper.setTo( email);
            helper.setSubject( "Notificación de Registro de Solicitud de  Firma de Documento");

            final String serverName      = configService.getPropiedad( "url.server.name");
            final String webAppContext   = configService.getPropiedad( "path.app.context");
            final String organizacion    = configService.getPropiedad( "string.organizacion.nombre");
        
            String url = serverName + webAppContext + "/consultaDocumentos";
            
            String htmlText= 
                "<div style='font-family: Arial, sans-serif;'>" +
                    "<img src=\"cid:image\"><br/><br/>" +
                    "El documento anexo fue enviado por " + titular + " con RFC " + rfc + " en la " +
                    "siguiente fecha y hora " + fechaHora + ".<br/><br/>" +
                    "Usted puede consultar el estatus de su solicitudes ingresando a la siguiente direccion.<br/>" +
                    "&nbsp;&nbsp;&nbsp;&nbsp;<a href=\""+ url + "\">"+ url + "</a><br/><br/>" +
                    "&nbsp;&nbsp;Atentamente<br/><br/>&nbsp;&nbsp;<b>" + organizacion + "</b>" +
                "</div>"
            ;
            
            helper.setText( htmlText, true);

            final Resource image = new UrlResource( serverName + webAppContext + "/images/logo_organizacion.png");
            helper.addInline( "image", image, "image/png");
            
            File archivo = new File( pathArchivo);            
            helper.addAttachment( archivo.getName(), archivo);
            
            this.mailSender.send( mime);
        
            System.out.println("notificacion de registro de solicitud enviada a: " + email);
            
        } catch( Exception ex) {
            Log.error( ex);
        }
    }
    
    private String obtenerCarpeta( String url) {
        final int iniIdx = url.indexOf( "?folio=") + 7;
        final int endIdx = url.indexOf( "&nombre=", iniIdx);
        
        return url.substring( iniIdx, endIdx);
    }
    
}
