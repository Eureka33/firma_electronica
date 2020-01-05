package mx.com.neogen.commons.exception;

import mx.com.neogen.commons.messages.MessageProvider;
import mx.com.neogen.commons.util.UtilReflection;

/**
 * 	This is an exception whose actual message is formed by another entity based 
 * in a message key and a list of replacement parameters.  
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public abstract class GenericException extends RuntimeException implements MessageProvider {

	private static final long serialVersionUID = 7218780994862221036L;
	
	private String   messageKey;
	private Object[] parameters;
	
	public GenericException() {
		super();
	}	
	public GenericException( Throwable cause) {
		super( cause);
	}

	
	@Override
	public String toString() {
		return UtilReflection.toString( this);
	}
	
	
	public final void setMessage( String messageKey, Object[] parameters) {
		this.messageKey = messageKey;
		this.parameters = parameters;
	}
	
	
	public String getMessageKey() {
		return messageKey;
	}
	

	public Object[] getMessageParams() {
		return parameters;
	}
	
}
