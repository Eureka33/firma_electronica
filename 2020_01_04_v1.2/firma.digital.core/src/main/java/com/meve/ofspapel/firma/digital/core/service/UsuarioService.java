package com.meve.ofspapel.firma.digital.core.service;

import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import com.meve.ofspapel.firma.digital.core.mappers.UsuarioDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UsuarioService {
     
    @Autowired UsuarioDAO usuarioDAO;
    
    /**
     * Obtiene o registra un usuario
     * 
     * @param clave         clave o identificador único del usuario
     * @param nombre        nombre o descripción del usuario
     * 
     * @return  Una instancia de Usuario registrado en base de datos
     */
    public Usuario obtenerUsuario( String clave, String nombre) {
        Usuario usuario = usuarioDAO.find( clave);
        
        if ( usuario == null) {
            usuario = new Usuario( null);
            usuario.setClave( clave);
            usuario.setNombre( nombre);
            
            usuarioDAO.insert( usuario);
        }
        
        return usuario;
    }
    
}
