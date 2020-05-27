package mx.com.neogen.commons.interfaces;

import mx.com.neogen.commons.bean.IdentificadorModulo;
import mx.com.neogen.commons.bean.Propiedades;
import mx.com.neogen.commons.bean.Result;
import mx.com.neogen.commons.logs.Proceso;

public interface IControllableModule {
	
	
	IdentificadorModulo getModuleID();
	
	
	Proceso init( Propiedades parametrosContexto);
	
	
	Result stop();
	
	
	Result runPerformanceTest();
	
}
