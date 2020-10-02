package mx.com.neogen.commons.bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mx.com.neogen.commons.util.UtilText;

/**
 *  <p> This class represents the outcome of a whole process </p>
 *  <p> Caracteristics: </p>
 *  
 *  <ul>
 *  	<li>Simple creation without using "new"</li>
 *  	<li>A single instance can be reused by all methods involved in 
 *  		task completion
 *  	</li>
 *  	<li>Any parametized message can be added</li>
 * 	</ul>
 *  
 *  @author Marco Antonio García García	    g2marco@yahoo.com.mx
 */
public final class Result {

	private Map<String, Object>	items;
	private boolean 		  success;
	private List<Mensaje> 	 messages;
	
	
	public Result() {
		super();
		
		this.items 	 = new HashMap<String, Object>();
		this.success = true;
	}
	
	
	
	public boolean isSuccess() {
		return success;
	}
	
	public boolean hasErrors() {
		return !success;
	}
	
	public <T> void setOutcome(	final T outcome) { 
		items.put( "outcome", outcome);
	}
	
	public <T> void addOutcome( final String key, final T outcome) {
		items.put( key, outcome);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOutcome() { 
		return (T) items.get( "outcome");
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getOutcome( 	final String key) {
		return (T) items.get( key);
	}
	
	public void addError(		final String messageKey, final Object ... params) {
		if( messages == null) {	messages = new ArrayList<Mensaje>(); }
		messages.add(  new Mensaje( messageKey, params));
		this.success = false;
	}
	
	public void addError(		final Throwable cause) {
		addError( "error.general.exception", UtilText.stackTraceToString( cause));
	}
	
	
}