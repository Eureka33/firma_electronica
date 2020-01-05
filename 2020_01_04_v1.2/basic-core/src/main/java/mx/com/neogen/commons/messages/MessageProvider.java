package mx.com.neogen.commons.messages;

/**
 * 	Defines methods to be implemented by an entity capable of returning a 
 * message.
 * 
 * @author Marco Antonio García García 		g2marco@yahoo.com.mx
 */
public interface MessageProvider {

	/**
	 * 	Returns the message key, this key can be looked-up from a messages 
	 * resource.
	 *  Related message could have replacement parameters.
	 * 
	 * @return	a string identifying a message 
	 */
	String getMessageKey();
	
	/**
	 * 	Returns an array of replacement values for creating a message.
	 *  
	 * @return An array of replacement values or null if the message needs no
	 * 	parameters.
	 */
	Object[] getMessageParams();
	
}
