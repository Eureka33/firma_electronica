package com.meve.ofspapel.firma.digital.core.service;

import com.meve.ofspapel.firma.digital.core.entidades.ArchivoDepositado;
import com.meve.ofspapel.firma.digital.core.entidades.RegistroSolicitud;
import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.mappers.RegistroDAO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        documentos.forEach( (ArchivoDepositado documento) -> {
            registroDAO.asignaDocumento( documento.getId(), archivoZip.getId());
        });
    }
    
    public RegistroSolicitud registraSolicitud( Usuario usuario, String folio, String pathUpload, String emailDestinatario) {
        final RegistroSolicitud entidad = new RegistroSolicitud();
        final Date fechaHora = new Date();
        
        entidad.setIdUsuario( usuario.getId());
        entidad.setFechaHora( fechaHora);
        entidad.setFolio( folio);
        entidad.setNombre( pathUpload);
        entidad.setEmailDestinatario( emailDestinatario);
        
        entidad.setEstatus( 0);
        entidad.setUltimaActualizacion( fechaHora);
        
        registroDAO.insertSolicitud( entidad);
        
        return entidad;
    }
    
}
