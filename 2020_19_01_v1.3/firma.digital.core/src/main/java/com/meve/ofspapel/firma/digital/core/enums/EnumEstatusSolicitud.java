package com.meve.ofspapel.firma.digital.core.enums;

public enum EnumEstatusSolicitud {
    REGISTRADA( 10),
    NOTIFICADA( 11),
    VISITADA(   12),
    ATENDIDA(   13),
    CANCELADA(  14);
    
    private final int estatus;

    EnumEstatusSolicitud( int estatus) {
        this.estatus = estatus;
    }
    
    public int getValue() {
        return estatus;    
    }
    
    public static EnumEstatusSolicitud valueOf( int estatus) {
        for( EnumEstatusSolicitud item : values()) {
            if( item.getValue() == estatus) {
                return item;
            }
        }
        
        throw new IllegalArgumentException( "Valor de estatus no definido: [" + estatus + "]");
    }
    
}
