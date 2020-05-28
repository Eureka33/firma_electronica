package com.eureka.firma.digital.ws.appenders;

import com.eureka.firma.digital.ws.bean.Firma;
import com.eureka.firma.digital.ws.core.IPDFSignAppender;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import java.io.FileOutputStream;


public class PDFSignAppenderV2 extends BasePDFSignAppender implements IPDFSignAppender {
    
    private static final int COLUMNAS_TABLA = 2;
    private static final int COLUMNAS_QR    = 1;
    private static final int BORDER_WIDTH   = 1;
    
    private static final Font TITLE_FONT  = new Font( FontFactory.getFont( "cambria_bold", 10));
    private static final Font NORMAL_FONT = new Font( FontFactory.getFont( "cambria_bold",  9));
    private static final Font SMALL_FONT  = new Font( FontFactory.getFont( "cambria_bold",  7));
    
    private static final Font SUBTITLE_FONT = new Font( FontFactory.getFont( "cambria_bold", 8));
    private static final Font MEDIUM_FONT   = new Font( FontFactory.getFont( "cambria_bold", 9));
    
    static {
        TITLE_FONT.setStyle( Font.BOLD);
        SUBTITLE_FONT.setStyle( Font.BOLD);
    }
    
    private static final String LEYENDA_TRAZABILIDAD =
        "  La integridad y autoría del presente documento electrónico se podrá comprobar a través de la liga que " +
        "se encuentra debajo del QR.\n\n" +
        "  De igual manera, se podrá verificar el documentro electrónico por medio del código QR, para lo cual se " +
        "recomienda descargar una aplicación de lectura de este tipo de códigos a su dispositivo móvil."
    ;
    
    private static final String LEYENDA_FUNDAMENTO_LEGAL = 
        "La presente hoja forma parte integral del documento que nos ocupa el cual ha sido firmado mediante el uso de " +
        "la firma electrónica avanzada (e.firma) del funcionario y/o servidor público competente, que contiene la "     +
        "cadena de caracteres asociados al documento electrónico original y a la Firma Electrónica Avanzada del "       +
        "funcionario y/o servidor público competente, así como el sello digital que permite comprobar la autenticidad " +
        "de su contenido conforme a lo dispuesto por los artículos 7 y 10 de la Ley de Firma Electrónica Avanzada; y "  +
        "12 de su Reglamento. Por un uso responsable del papel, y en caso de ser necesario, las copias del presente "   +
        "documento se enviarán de forma electrónica, de conformidad con lo establecido en la fracción V y VI del "      +
        "artículo 11 del “Decreto que establece las medidas para el uso eficiente, transparente y eficaz de los "       +
        "recursos públicos, y las acciones de disciplina presupuestaría en el ejercicio del gasto público, así como "   +
        "para la modernización de la Administración Pública Federal”. La versión electrónica del presente documento, "  +
        "se podrá verificar a través del Código QR para lo cual, se recomienda descargar una aplicación de lectura de " +
        "éste tipo de códigos a su dispositivo móvil o computadora."
    ;
    
    
    @Override
    public void firmarPDF( String source, String target, String urlDescarga, Firma firma) {
       
        PdfReader reader = null;
        FileOutputStream  fos = null;
        
        try {
            reader = new PdfReader( source);

            fos = new FileOutputStream( target);
        
            final PdfStamper stamper = new PdfStamper( reader, fos);
            
            int lastPage = reader.getNumberOfPages() + 1;
            stamper.insertPage( lastPage, PageSize.LETTER);
            
            PdfContentByte content = stamper.getOverContent( lastPage);
            
            ColumnText ct = new ColumnText( content );
            
            ct.setSimpleColumn( 60, 650, 550, 30);
            ct.addElement( crearSeccionFirma(urlDescarga, firma));
            
            ct.go();
                       
            stamper.close();
            
        } catch( Exception ex) {
            ex.printStackTrace();
            
        } finally {
            try { reader.close(); } catch(Exception exInt) {}
            try { fos.close(); } catch(Exception exInt) {}
        }

    }
    
    PdfPTable crearSeccionFirma( String urlDescarga, Firma firma) {
        final PdfPTable table = new PdfPTable( COLUMNAS_TABLA);
        table.setWidthPercentage( 100);
        
        agregarDatosFirma( table, firma);
        
        agregarTituloContenido( table, "Cadena Original", obtenerCadenaOriginal( firma, urlDescarga), NORMAL_FONT);
        agregarTituloContenido( table, "Sello Digital", firma.getFirmaElectronica(), NORMAL_FONT);
        
        agregarTitulo( table, "Trazabilidad");
        agregarContenidoFirma( table, urlDescarga);
       
        
        final Chunk link = new Chunk( urlDescarga, NORMAL_FONT);
        link.setAnchor( urlDescarga);
        agregarCeldaFrase(table, new Phrase( link));
        
        agregarCeldaFrase( table, new Phrase( LEYENDA_FUNDAMENTO_LEGAL, SMALL_FONT));
        
        return table;
    }
    
    private void agregarDatosFirma( PdfPTable table, Firma firma) {
        PdfPCell celda = new PdfPCell();
        celda.setColspan( COLUMNAS_TABLA - 1);
        celda.setRowspan( 4);
        celda.setBorderWidth( 0);
        table.addCell( celda);
        
        celda = new PdfPCell( new Phrase( firma.getTitular(), MEDIUM_FONT));
        celda.setColspan( COLUMNAS_TABLA - 1);
        celda.setBorderWidth( 0);
        table.addCell( celda);
        
        celda = new PdfPCell( new Phrase( "Firmante", SUBTITLE_FONT));
        celda.setColspan( COLUMNAS_TABLA - 1);
        celda.setBorderWidth( 0);
        celda.setBorderWidthTop( 1);
        table.addCell( celda);
        
        
        celda = new PdfPCell( new Phrase( firma.getFecha(), MEDIUM_FONT));
        celda.setColspan( COLUMNAS_TABLA - 1);
        celda.setBorderWidth( 0);
        table.addCell( celda);
        
        celda = new PdfPCell( new Phrase( "Fecha - Hora Firma", SUBTITLE_FONT));
        celda.setColspan( COLUMNAS_TABLA - 1);
        celda.setBorderWidth( 0);
        celda.setBorderWidthTop( 1);
        table.addCell( celda);
        
        celda = new PdfPCell( new Phrase( " ", SMALL_FONT));
        celda.setColspan( COLUMNAS_TABLA);
        celda.setBorderWidth( 0);
        table.addCell( celda);

    }
    
    private String obtenerCadenaOriginal( Firma firma, String urlDescarga) {
        return "||" + firma.getFecha() + "||" + firma.getRfc() + "||" + firma.getTitular() + "||" + urlDescarga + "||";
    }
    
    private void agregarTituloContenido( PdfPTable table, String titulo, String contenido, Font fontContenido) {
        agregarTitulo( table, titulo);
        agregarCeldaFrase( table, new Phrase( contenido, fontContenido));
    }
    
    private void agregarTitulo( PdfPTable table, String texto) {
        final PdfPCell celda = new PdfPCell( new Phrase( texto, TITLE_FONT));
        
        celda.setColspan( COLUMNAS_TABLA);
        celda.setPadding( 5);
        celda.setBackgroundColor( BaseColor.LIGHT_GRAY);
        celda.setBorderWidth( BORDER_WIDTH);
        
        celda.setVerticalAlignment(   PdfPCell.ALIGN_MIDDLE);
        celda.setHorizontalAlignment( PdfPCell.ALIGN_CENTER);
        
        table.addCell( celda);
    }
    
    private void agregarCeldaFrase( PdfPTable table, Phrase frase) {
        final PdfPCell celda = new PdfPCell( frase);
        
        celda.setColspan( COLUMNAS_TABLA);
        celda.setPadding( 5);
        
        celda.setBorderWidth( BORDER_WIDTH);
        celda.setMinimumHeight( 20);
        
        celda.setVerticalAlignment(   PdfPCell.ALIGN_MIDDLE);
        celda.setHorizontalAlignment( PdfPCell.ALIGN_JUSTIFIED);
        
        table.addCell( celda);
    }
    
    private void agregarContenidoFirma( PdfPTable table, String urlDownload) {
        PdfPCell celda;
                 
        final byte[] codigo = generateQRCodeImage( urlDownload, 200, 200);
        
        if( codigo != null) {
            try {
                celda = new PdfPCell( Image.getInstance( codigo));
                
            } catch ( Exception ex) {
                celda = new PdfPCell( new Paragraph( "[Código QR]"));    
            }
            
        } else { 
            celda = new PdfPCell( new Paragraph( "[Código QR]"));
            
        }
        
        celda.setBorderWidth( BORDER_WIDTH);
        celda.setColspan( COLUMNAS_QR);
        celda.setPadding( 1);
        
        celda.setHorizontalAlignment( PdfPCell.ALIGN_CENTER);
        celda.setVerticalAlignment( PdfPCell.ALIGN_MIDDLE);
        
        table.addCell( celda);
        
        celda = new PdfPCell( new Phrase( LEYENDA_TRAZABILIDAD, NORMAL_FONT));
        
        celda.setBorderWidth( BORDER_WIDTH);
        celda.setColspan( COLUMNAS_TABLA - COLUMNAS_QR);
        celda.setPadding( 10);
        
        celda.setHorizontalAlignment( PdfPCell.ALIGN_JUSTIFIED);
        celda.setVerticalAlignment( PdfPCell.ALIGN_TOP);
        
        table.addCell(celda);
    }
    
}
