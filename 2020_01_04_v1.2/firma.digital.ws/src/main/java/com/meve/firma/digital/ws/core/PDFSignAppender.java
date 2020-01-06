package com.meve.firma.digital.ws.core;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.meve.firma.digital.ws.bean.Firma;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import org.springframework.stereotype.Component;

@Component
public class PDFSignAppender {
    public static Font FONT_2  = new Font( FontFactory.getFont("cambria_bold", 7));
    public static Font FONT_9  = new Font( FontFactory.getFont("cambria_bold", 9));
    public static Font FONT_11 = new Font( FontFactory.getFont("cambria_bold", 7, Font.ITALIC));
    public static Font FONT_12 = new Font( FontFactory.getFont("cambria_bold", 9, Font.UNDERLINE));
    
    
    public void firmarPDF( String source, String target, String urlDescarga, Firma firma) {
       
        PdfReader reader = null;
        FileOutputStream  fos = null;
        
        try {
            reader = new PdfReader( source);

            fos = new FileOutputStream( target);
        
            final PdfStamper stamper = new PdfStamper( reader, fos);
            
            int lastPage = reader.getNumberOfPages() + 1;
            stamper.insertPage( lastPage, PageSize.A4);
            
            PdfContentByte content = stamper.getOverContent( lastPage);
            
            ColumnText ct = new ColumnText( content );
            
            ct.setSimpleColumn( 30, 800, 550, 30);
            ct.addElement( crearSeccionFirma_VER2(urlDescarga, firma));
            
            ct.go();
                       
            stamper.close();
            
        } catch( Exception ex) {
            ex.printStackTrace();
            
        } finally {
            try { reader.close(); } catch(Exception exInt) {}
            try { fos.close(); } catch(Exception exInt) {}
        }

    }
    
    PdfPTable crearSeccionFirma_VER2( String urlDescarga, Firma firma) {
        int COLUMNAS = 7;
        int RENGLONES = 3;
		int ANCHO_BORDER = 0;
		
		PdfPTable table = new PdfPTable( COLUMNAS);
        table.setWidthPercentage( 100.0F);
        PdfPCell celda;
                
        celda = new PdfPCell( generarParrafoFirmas( firma) );
        celda.setColspan( COLUMNAS - 2);
        celda.setPadding(0);
        celda.setMinimumHeight( 100);
        celda.setBorderWidth( ANCHO_BORDER);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell( celda);
        
        byte[] codigo = generateQRCodeImage( urlDescarga, 150, 150);
        
        if( codigo != null) {
            try {
                celda = new PdfPCell( Image.getInstance( codigo));
            } catch ( Exception ex) {
                celda = new PdfPCell( new Paragraph( "[Código QR]"));
            
            } 
        } else { 
            celda = new PdfPCell( new Paragraph( "[Código QR]"));
            
        }
        celda.setRowspan( RENGLONES);
        celda.setColspan( 2);
        celda.setPadding(0);
        celda.setBorderWidth( ANCHO_BORDER);
        celda.setHorizontalAlignment( PdfPCell.ALIGN_RIGHT);
        table.addCell( celda);
        
        
        Phrase phrase = new Phrase( "Descarge o valide este documento con el código QR, o dando clic en este ", FONT_9);
        Chunk chunk = new Chunk( "enlace", FONT_12);
        chunk.setAnchor( urlDescarga);
        phrase.add(chunk);
        
        celda = new PdfPCell( phrase);
        celda.setColspan( COLUMNAS - 2);
        celda.setBorderWidth( ANCHO_BORDER);
        celda.setHorizontalAlignment( PdfPCell.ALIGN_RIGHT);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell(celda);
        
        
        celda = new PdfPCell( new Phrase( " "));
        celda.setColspan( COLUMNAS - 2);
        celda.setBorderWidth( ANCHO_BORDER);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell(celda);
        
        return table;
    }
    
    Paragraph generarParrafoFirmas( Firma firma) {
		final Paragraph parrafo = new Paragraph();
		
		parrafo.setAlignment( Element.ALIGN_LEFT);
        
		parrafo.add( new Phrase("Fecha y hora de la firma: ", FONT_11));
		parrafo.add(new Phrase( firma.getFecha(), FONT_2));
		parrafo.add(new Phrase(" Nombre: ", FONT_11));
		parrafo.add(new Phrase( firma.getTitular(), FONT_2));
		parrafo.add(new Phrase(" Cadena Hash: ", FONT_11));		
        parrafo.add(new Phrase( firma.getFirmaElectronica(), FONT_2));
		
		return parrafo;
	}
    
    PdfPTable crearSeccionFirma_VER1( String urlDescarga, Firma firma) {
        PdfPTable table = new PdfPTable(3);
        
        PdfPCell celda;
        Paragraph parrafo;
                
        celda = new PdfPCell( new Paragraph( "Firma Electrónica"));
        celda.setColspan(2);
        celda.setBorderWidthLeft( 0);
        celda.setBorderWidthTop(  0);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        
        table.addCell( celda);
        
        byte[] codigo = generateQRCodeImage( urlDescarga, 150, 150);
     
        if( codigo != null) {
            try {
                celda = new PdfPCell( Image.getInstance( codigo));
            } catch ( Exception ex) {
                celda = new PdfPCell( new Paragraph( "[Código QR]"));
            
            } 
        } else { 
            celda = new PdfPCell( new Paragraph( "[Código QR]"));
            
        }
        
        celda.setRowspan(4);
        celda.setPadding(0);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell( celda);
        
        celda = new PdfPCell( new Paragraph( firma.getFirmaElectronica()));
        celda.setColspan( 2);
        celda.setBorderWidthLeft( 0);
        celda.setBorderWidthTop(  0);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        celda.setMinimumHeight( 40);
        table.addCell( celda);
        
        celda = new PdfPCell( new Paragraph( "Datos Firmante"));
        celda.setColspan( 2);
        celda.setBorderWidthLeft( 0);
        celda.setBorderWidthTop(  0);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell( celda);
        
        celda = new PdfPCell( 
            new Paragraph( "Fecha: " + firma.getFecha() + " CURP: " + firma.getCurp() + "\nTitular: " + firma.getTitular() )
        );
        
        celda.setColspan( 2);
        celda.setBorderWidthLeft( 0);
        celda.setBorderWidthTop(  0);
        celda.setBorderWidthBottom( 0);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell( celda);
        
        parrafo = new Paragraph( "Eureka Consulting @ 2019");
        
        celda = new PdfPCell( parrafo);
        celda.setColspan(3);
        celda.setBorderWidthLeft( 0);
        celda.setBorderWidthRight( 0);
        celda.setBorderWidthBottom( 0);
        
        celda.setHorizontalAlignment( PdfPCell.ALIGN_RIGHT);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        table.addCell(celda);
        
        return table;
    }
    
    byte[] generateQRCodeImage( String text, int width, int height) {
        QRCodeWriter qrCodeWriter;
        BitMatrix bitMatrix;
        ByteArrayOutputStream pngOutputStream = null;
        
        try {
            qrCodeWriter = new QRCodeWriter();
            bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);
            
            pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream( bitMatrix, "PNG", pngOutputStream);
            
            return pngOutputStream.toByteArray(); 
        
        } catch( Exception ex) {
            ex.printStackTrace();
            return null;
            
        } finally {
            try{ pngOutputStream.close(); } catch( Exception ex) {}
        
        }
        
    }
    
}
