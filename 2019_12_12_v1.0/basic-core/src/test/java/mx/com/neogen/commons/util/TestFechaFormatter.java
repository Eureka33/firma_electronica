package mx.com.neogen.commons.util;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class TestFechaFormatter {
	
	@Test
	public void pruebaFormatoFecha() {
		Assert.assertEquals( "date matches", "08/09/2001", FechaFormatter.formatFecha( new Date( 1000000000000L)));
	}
}
