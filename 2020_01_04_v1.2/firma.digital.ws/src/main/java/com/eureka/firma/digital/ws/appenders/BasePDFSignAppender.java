package com.eureka.firma.digital.ws.appenders;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.io.ByteArrayOutputStream;


public class BasePDFSignAppender {
    
    protected byte[] generateQRCodeImage( String text, int width, int height) {
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
