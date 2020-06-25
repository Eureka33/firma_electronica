package mx.com.neogen.commons.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import mx.com.neogen.commons.annotation.AutoPrint;
import mx.com.neogen.commons.messages.AppLogger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;


public final class UtilBean {
    
    private static final AppLogger LOG = AppLogger.getLogger( UtilBean.class);
	
	private static final ObjectMapper jsonMapper = new ObjectMapper();

	
	public static String toString( final Object instancia) {
		
		final StringBuilder str = new StringBuilder();
		
		try {
			toString(str, instancia, 0);
		
		} catch ( Exception ex) {
            LOG.error( "Error al serializar objeto", ex);
		}
		
		return str.toString();
	}
	
	
	private static void toString( 	final StringBuilder	 strb, 
									final Object	instancia,
									int	  anidamiento) throws Exception {
		if( instancia == null){
			strb.append( "null\n");
			return;
		}
		
		strb.append( "<").append( instancia.getClass().getName()).
		append("> [\n");
		
		anidamiento++;
		
		final Field[] campos = instancia.getClass().getDeclaredFields();
		generatePrintableFields(campos, instancia, strb, anidamiento);

		final Field[] camposHeredados = 
			instancia.getClass().getSuperclass().getDeclaredFields();
		
		generatePrintableFields(camposHeredados, instancia, strb, anidamiento);
		
		anidamiento--;
		
		UtilText.appendTabs( strb, anidamiento);
		strb.append("]\n");
		
	}
	
	private static void generatePrintableFields(	final Field[] 	campos,
													final Object 	object,
													final StringBuilder strb,
													final int anidamiento)
															throws Exception {
		if ( campos == null || campos.length == 0) {
			return;
		}
		
		for( Field campo : campos) {
			boolean isAccesible = campo.isAccessible();

			campo.setAccessible(true);
		
			final Object instancia = campo.get( object);
			
			if( instancia != null && instancia instanceof Iterable){

				for( Object nextInstancia : (Iterable<?>) instancia) {
					UtilText.appendTabs( strb, anidamiento);
					toString(strb, nextInstancia, anidamiento);
				}
			} else if ( instancia != null && campo.getType().isArray()) {
				if ( instancia instanceof byte[]) {
					generatePrintedField(campo, Arrays.toString((byte[]) instancia), strb, anidamiento);
				}
				
			} else {
				generatePrintedField(campo, instancia, strb, anidamiento);

			}

			campo.setAccessible( isAccesible);
		}
	}
	
	
	/**
	 *  Imprime el valor de un campo en la instancia
	 */
	private static void generatePrintedField(	final Field 		campo,
												final Object 		valor,
												final StringBuilder  strb,
												int 	      anidamiento)
															throws Exception {
		UtilText.appendTabs(strb, anidamiento);
		strb.append( campo.getName()).append(" = ");
		
		for ( Annotation anotacion : campo.getType().getAnnotations()) {
			if ( anotacion.annotationType().equals( AutoPrint.class)) {
				toString( strb, valor, anidamiento);				
				return;
			}
		}
		
		strb.append( valor).append("\n");
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <T> T parseBean( 				final InputStream itemStream, 
												final Class<?>		   clase) {
		try {
			return (T) jsonMapper.readValue( itemStream, clase);
		
		} catch( Exception ex) {
			throw new RuntimeException(ex); 

		} finally {	
			UtilStream.close( itemStream);
		}
		
	}
	
	@SuppressWarnings({ "unchecked" })
	public static <T> T parseBean( final InputStream itemStream, final JavaType	javaType) {
		try {
			return (T) jsonMapper.readValue( itemStream, javaType);
		
		} catch( Exception ex) {
			throw new RuntimeException(ex);
			
		} finally {
			UtilStream.close( itemStream);
		}
	}

	public static String objectToJsonString(Object object) throws IOException {
		final ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public static InputStream parseBeanToInputStream(Object bean) {
			
		InputStream       response = null;   

		try (
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
          	ObjectOutputStream     oos = new ObjectOutputStream(baos);
        ) {
            
		    oos.writeObject(bean);
            oos.flush();
            
		    response = new ByteArrayInputStream(baos.toByteArray());
				
			return response;
		
		} catch( Exception ex) {	
			throw new RuntimeException(ex);
			
		}	
	}
}