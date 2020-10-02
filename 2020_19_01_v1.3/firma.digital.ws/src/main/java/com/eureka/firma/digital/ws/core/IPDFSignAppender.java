package com.eureka.firma.digital.ws.core;

import com.eureka.firma.digital.ws.bean.Firma;


public interface IPDFSignAppender {
    
    void firmarPDF( String source, String target, String urlDescarga, Firma firma, String organizacion, String checksum);

}