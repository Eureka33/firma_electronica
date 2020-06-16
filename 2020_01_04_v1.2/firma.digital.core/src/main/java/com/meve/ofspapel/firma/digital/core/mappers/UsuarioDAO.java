package com.meve.ofspapel.firma.digital.core.mappers;

import com.meve.ofspapel.firma.digital.core.entidades.Usuario;
import org.apache.ibatis.annotations.Param;

public interface UsuarioDAO {
    
    int insert( @Param( "item") Usuario usuario);
    
    Usuario find( @Param( "clave") String clave);
    
}
