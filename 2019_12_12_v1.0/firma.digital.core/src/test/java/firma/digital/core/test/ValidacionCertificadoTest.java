package firma.digital.core.test;

import java.io.FileInputStream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.meve.ofsp.certificado.ICertificadoDigitalService;
import com.meve.ofsp.certificado.IValidacionCertificadoService;
import com.meve.ofsp.certificado.StatusCertificadoEnum;
import com.meve.ofsp.certificado.TipoCertificadoEnum;

@RunWith( SpringJUnit4ClassRunner.class)
@ContextConfiguration( locations="classpath:core-firma-digital-context.xml")
public class ValidacionCertificadoTest {
	
	@Autowired
	private IValidacionCertificadoService servicioValidacion;
	
	@Autowired
	private ICertificadoDigitalService  certificadoService;
	
	@Test
	public void testValidacionCertificadoFP() throws Exception {

		StatusCertificadoEnum estatus = servicioValidacion.obtenerEstatus( 
			certificadoService.cargar( new FileInputStream( "C:\\tmp\\ope.gob.mx.cer" )),
			TipoCertificadoEnum.FUNCION_PUBLICA
		);
		
		Assert.assertEquals( StatusCertificadoEnum.VALIDO, estatus);
	}
		
	@Test
	public void testValidacionCertificadoFIEL() throws Exception {

		StatusCertificadoEnum estatus = servicioValidacion.obtenerEstatus( 
			certificadoService.cargar( new FileInputStream( "C:\\tmp\\testxyz\\repl660531335.cer" )),
			TipoCertificadoEnum.FIEL
		);
		
		Assert.assertEquals( StatusCertificadoEnum.VALIDO, estatus);
	}
}
