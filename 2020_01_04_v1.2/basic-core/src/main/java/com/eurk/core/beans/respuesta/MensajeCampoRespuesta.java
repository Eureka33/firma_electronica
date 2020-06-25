package com.eurk.core.beans.respuesta;

public class MensajeCampoRespuesta extends MensajeRespuesta {

	private static final long serialVersionUID = -6243814053922384866L;
	
	private final String campo;
	
	
	protected MensajeCampoRespuesta( TipoMensajeEnum tipo, String claveCampo, String mensaje) {
		super( tipo, mensaje);
		
		this.campo = claveCampo;
	}
	
	
	public static MensajeCampoRespuesta crearMensajeConLlave( String claveCampo, String llave) {
		return new MensajeCampoRespuesta( TipoMensajeEnum.KEY, claveCampo, llave);
	}
	
	public static MensajeCampoRespuesta crearMensajeConTexto( String claveCampo, String texto) {
		return new MensajeCampoRespuesta( TipoMensajeEnum.TEXT, claveCampo, texto);
	}
	
	public String getCampo() {		return campo;		}

	
	@Override
	public boolean equals(Object object) {
		if ( !super.equals( object)) { return false; }
		
		if ( !(object instanceof MensajeCampoRespuesta)) { return false; }
		
		MensajeCampoRespuesta instancia = (MensajeCampoRespuesta) object;
		
		if( campo == null) { return false;	}
		
		return campo.equals( instancia.campo);
	}

    @Override
    public int hashCode() {
        return (int) ( campo.hashCode() * serialVersionUID);
    }
    
}