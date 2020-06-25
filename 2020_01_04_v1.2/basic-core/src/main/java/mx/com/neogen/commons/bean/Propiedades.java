package mx.com.neogen.commons.bean;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Propiedades implements Map<String, Object> {
	
	private Map<String, Object> entradas;

	
	public Propiedades() {
		super();
		this.entradas = new HashMap<>();
	}
	
	public Propiedades( Map<String, Object> propiedades) {
		super();
		this.entradas = new HashMap<>( propiedades);
	}
	
	
	public Object setPropiedad( String key, Object value) {
		return entradas.put( key, value);
	}
	
    /**
     *  Obtiene una propiedad, intenta castearla al tipo de dato solicitado. Genera excepción si la propiedad 
     *  no existe
     * @param <T>       Tipo de dato para casteo
     * @param key       Clave de la propiedad
     * 
     * @return          El valor de la propiedad solicitada
     */
	@SuppressWarnings("unchecked")
	public <T> T getPropiedad( String key) {
        final Object value = entradas.get( key);
        
		if ( !entradas.containsKey( key) || value == null) {
			throw new IllegalArgumentException( "No existe la propiedad solicitada: ["+ key +"]");
		}
		
		return (T) value;
	}
	
    /**
     *  Obtiene una propiedad, intenta castearla al tipo de dato solicitado.
     *  Si no existe, devuelve el valor por default
     *  no existe
     * @param <T>           Tipo de dato para casteo
     * @param key           Clave de la propiedad
     * @param defaultValue  Valor por default
     * 
     * @return              El valor de la propiedad solicitada, o bien el valor por default
     */
	@SuppressWarnings("unchecked")
	public <T> T getPropiedad( String key, Object defaultValue) {
        Object value = entradas.get( key);
        
		if ( !entradas.containsKey( key) || value == null) {
			value = defaultValue;
		}
		
		return (T) value;
	}
    
    
	public Object put( String key, Object value) {
		return entradas.put( key, value);
	}
	
	public Collection<Object> values() {
		return entradas.values();
	}

	
	public void clear() {
		entradas.clear();	
	}
	
	public boolean contiene ( Object key) {
		return entradas.containsKey( key);
	}
	
	public boolean containsKey(Object key) {
		return entradas.containsKey( key);
	}
	
	public boolean containsValue(Object value) {
		return entradas.containsValue( value);
	}
	
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return entradas.entrySet();
	}
	
	public Object get(Object key) {
		return entradas.get( key);
	}
	
	public Set<String> keySet() {
		return entradas.keySet();
	}
	
	public boolean isEmpty() {
		return entradas.isEmpty();
	}
	
	public void putAll(Map<? extends String, ? extends Object> m) {
		entradas.putAll( m);
	}
	
	public Object remove(Object key) {
		return entradas.remove( key);
	}

	public int size() {
		return entradas.size();
	}

    @Override
    public String toString() {
        final StringBuilder strb = new StringBuilder();
        
        for( Entry<String, Object> entry : entradas.entrySet()) {
            strb.append( "\n{").append( entry.getKey()).append( ": ").append( entry.getValue()).append( "}");
        }
        
        return strb.toString();
    }
    
}