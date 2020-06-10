package com.meve.ofspapel.firma.digital.core.service;

import java.io.File;
import javax.mail.internet.MimeMessage;
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
                                    String organizacion, String nombreArchivo) {
        try {
            MimeMessage mime = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, true);

            helper.setFrom( emailFrom);
            helper.setTo( email);
            helper.setSubject( "Notificación de Firma de Documento");
            
            String htmlText;
            
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
            System.out.println("error al enviar notificación de correo a: " + email);
            ex.printStackTrace();
        }
    }
    
    private String obtenerCarpeta( String url) {
        final int iniIdx = url.indexOf( "?folio=") + 7;
        final int endIdx = url.indexOf( "&nombre=", iniIdx);
        
        return url.substring( iniIdx, endIdx);
    }
    
}
