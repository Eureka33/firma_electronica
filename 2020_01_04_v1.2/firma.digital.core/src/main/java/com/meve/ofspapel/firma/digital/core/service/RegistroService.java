package com.meve.ofspapel.firma.digital.core.service;

import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.mappers.RegistroDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RegistroService {

    @Autowired private RegistroDAO registroDAO;
            
    
    public ArchivoDepositado registraDocumento( Usuario usuario, String fechaHora, String folio, String nombre) throws ParseException {
        
        final ArchivoDepositado entidad = new ArchivoDepositado();
        
        entidad.setIdUsuario( usuario.getId());
        entidad.setFechaHora( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss").parse( fechaHora));
        entidad.setFolio( folio);
        entidad.setNombre( nombre);
        
        registroDAO.insertDocumento( entidad);       
       
        return entidad;
    }
    
    public ArchivoDepositado registraDocumentoBatch( Usuario usuario, String fechaHora, String folio,  String nombre) throws ParseException {
        
        final ArchivoDepositado entidad = new ArchivoDepositado();
        
        entidad.setIdUsuario( usuario.getId());
        entidad.setFechaHora( new SimpleDateFormat( "dd/MM/yyyy HH:mm:ss").parse( fechaHora));
        entidad.setFolio( folio);
        entidad.setNombre( nombre);
        
        registroDAO.insertDocumentoBatch( entidad);
       
        return entidad;
    }
        
    public void asignaDocumentos( ArchivoDepositado archivoZip, List<ArchivoDepositado> documentos) {
        for( ArchivoDepositado documento : documentos) {
            registroDAO.asignaDocumento( documento.getId(), archivoZip.getId());
        }
    }
    
}
