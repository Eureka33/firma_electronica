package mx.com.neogen.commons.bean;

import java.io.Serializable;
import mx.com.neogen.commons.util.UtilReflection;


public class ItemCatalogo implements Serializable {
	
	private static final long serialVersionUID = 2308907526606351860L;

	private String value;
    private String code;
	private String label;
    private Integer status;


	public ItemCatalogo() {
		super();
	}

    public ItemCatalogo( String id, String codigo, String descripcion) {
        super();
        value = id;
        code  = codigo;
        label = descripcion;
    }

    public ItemCatalogo( Integer id, String codigo, String descripcion) {
        this( id.toString(), codigo, descripcion);
    }
    
    
	public String getValue() {				return value;		}
	public void setValue(String value) {	this.value = value;	}

    public String getCode() {               return code;        }    
    public void setCode( String code) {     this.code = code;   }
	
    public String getLabel() {				return label;		}
	public void setLabel(String label) {	this.label = label;	}
    
    public Integer getStatus() {			return status;      }
	public void setStatus(Integer value) {	this.status = value;}

	@Override
	public boolean equals(Object item) {
		if( item == null) { return false; }
		if( this == item) { return true;  }
		if( !(item instanceof ItemCatalogo)) { return false; }
		
		final ItemCatalogo other = (ItemCatalogo) item;
		
		return value.equals( other.value);
	}

    @Override
    public int hashCode() {
        return (int) (value.hashCode() * serialVersionUID);
    }
	
    
	@Override
	public String toString() { return UtilReflection.toString( this); }
	
}