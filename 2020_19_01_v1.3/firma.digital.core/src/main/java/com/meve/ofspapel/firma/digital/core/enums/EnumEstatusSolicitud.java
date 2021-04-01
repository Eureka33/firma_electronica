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
    
    public int getEstatus() {
        return estatus;    
    }
    
    public static EnumEstatusSolicitud valueOf( int estatus) {
        for( EnumEstatusSolicitud item : values()) {
            if( estatus == item.getEstatus()) {
                return item;
            }
        }
        
        throw new IllegalArgumentException( "Valor de estatus no definido: [" + estatus + "]");
    }
    
}
