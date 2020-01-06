package mx.eurk.firma.digital.bean;

import org.springframework.context.ApplicationContext;

public class AppContext {
    
    private static ApplicationContext appContext;
    
    public static void setApplicationContext( ApplicationContext ctx) {
        AppContext.appContext = ctx;
    }
    
    public static <T> T getBean( Class clase) {
        return (T) appContext.getBean( clase);
    }
    
}
