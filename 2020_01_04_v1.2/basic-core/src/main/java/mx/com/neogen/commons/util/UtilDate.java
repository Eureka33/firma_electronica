package mx.com.neogen.commons.util;

import static java.util.Calendar.HOUR_OF_DAY;
import static java.util.Calendar.MILLISECOND;
import static java.util.Calendar.MINUTE;
import static java.util.Calendar.SECOND;
import static java.util.Calendar.getInstance;

import java.util.Calendar;
import java.util.Date;

/**
 * 	Esta clase contiene operaciones típicas para objetos de tipo fecha.
 * No se mantiene estado de cada modificacion.
 * 
 *  Si necesitas una clase de manipulación de fechas que mantenga el estado 
 * de cada operación, puedes utilizar DateUtilInst
 *  
 * @author Marco Antonio García García		g2marco@yahoo.com.mx
 * 
 */
public final class UtilDate {

	private UtilDate() {
		super();
	}

	/**
	 * 	Devuelve la fecha / hora actual en milisegundos. Este metodo en una
	 * simplificacion de la llamada: Calendar.getInstance.getTimeInMillis();
	 *  
	 *  @return La fecha actual en milisegundos (@see {@link Date})
	 */
	public static long getCurrentTime() {
		return getInstance().getTimeInMillis();
	}
	
	/**
	 * 	Devuelve un objeto fecha que representa el inicio del día para la 
	 * fecha indicada.
	 *  
	 * @return Un objeto Date apuntando al inicio del día pasado como parametro
	 */
	public static Date getFechaHoraIniDia( Date fecha) {
		Calendar calendar = getInstance();
		calendar.setTime( fecha);
		
		setStartOfDay( calendar);

		return calendar.getTime();

	}

	/**
	 * 	Actualiza un calendario para apuntar al inicio del día.
	 * 
	 * @param calendar		- Calendar que será modificado.
	 */
	public static void setStartOfDay( Calendar calendar) {
		calendar.set( HOUR_OF_DAY, calendar.getActualMinimum( HOUR_OF_DAY));
		calendar.set( MINUTE,      calendar.getActualMinimum( MINUTE));
		calendar.set( SECOND,      calendar.getActualMinimum( SECOND));
		calendar.set( MILLISECOND, calendar.getActualMinimum( MILLISECOND));
	}

	/**
	 * 	Obtiene un objeto fecha que representa el final del día indicado.
	 * 
	 * @param fecha
	 * 		Objeto Date con la fecha de interes.
	 * @return	
	 * 		El nuevo objeto Date creado.
	 */

	public static Date getFechaFinDia( Date fecha) {
		Calendar calendar = getInstance();
		calendar.setTime( fecha);
		
		setEndOfDay( calendar);

		return calendar.getTime();

	}

	/**
	 * 	Modifica un objeto calendario para apuntar al final de ese día.
	 * 
	 * @param calendar	Objeto Calendar que seŕa modificado.
	 */
	public static void setFechaFinDia( Calendar calendar) {
		calendar.set( Calendar.HOUR_OF_DAY,
			calendar.getActualMaximum( Calendar.HOUR_OF_DAY)
		);
    	calendar.set( Calendar.MINUTE,
    		calendar.getActualMaximum( Calendar.MINUTE)
    	);
    	calendar.set( Calendar.SECOND,
    		calendar.getActualMaximum( Calendar.SECOND)
    	);
    	calendar.set( Calendar.MILLISECOND,
    		calendar.getActualMaximum( Calendar.MILLISECOND)
    	);
 
    	return;
	}

	
	/**
	 * 	Modifica un objeto calendario para apuntar al final de ese día.
	 * 
	 * @param calendar	Objeto Calendar que seŕa modificado.
	 */
	public static void setEndOfDay( Calendar calendar) {
		calendar.set( Calendar.HOUR_OF_DAY,	calendar.getActualMaximum( Calendar.HOUR_OF_DAY));
    	calendar.set( Calendar.MINUTE,      calendar.getActualMaximum( Calendar.MINUTE));
    	calendar.set( Calendar.SECOND,      calendar.getActualMaximum( Calendar.SECOND));
    	calendar.set( Calendar.MILLISECOND, calendar.getActualMaximum( Calendar.MILLISECOND));
	}

	
	/*
	public void setFechaHoraActual() {
		calendar = getInstance();
		calendar.set(SECOND, 0);
		calendar.set(MILLISECOND, 0);

	}

	public void setTimeStampActual() {
		calendar = getInstance();
	}

	public void setFechaHora(int anio, int mes, int dia, int hora, int minutos) {
		calendar = getInstance();
		calendar.set(anio, mes - 1, dia, hora, minutos);
		calendar.set(SECOND, 0);
		calendar.set(MILLISECOND, 0);

	}

	public void setFecha(Date fecha) {
		calendar = getInstance();
		calendar.setTime(fecha);

	}

	public void setHoraMinutos(String HHmmStr) {

		setInicioDia();

		long nuevaFecha = calendar.getTimeInMillis()
				+ FechaFormatter.getHorainMillis(HHmmStr);

		calendar.setTimeInMillis(nuevaFecha);

	}

	public void setFechaHoraSiguienteCuarto() {
		// Obtiene la fecha y hora del siguiente cuarto de hora

		int periodo = getMinutos() / 15;
		int nextCuarto = 0;

		switch (periodo) {
		case 0:
			nextCuarto = 15;
			break;

		case 1:
			nextCuarto = 30;
			break;

		case 2:
			nextCuarto = 45;
			break;

		case 3:
			nextCuarto = 0;

			if (getHora() == calendar.getActualMaximum(HOUR_OF_DAY)) {
				calendar.roll(HOUR_OF_DAY, true);

				if (getDia() == calendar.getActualMaximum(DAY_OF_MONTH)) {
					calendar.roll(DAY_OF_MONTH, true);

					if ((getMes() - 1) == calendar.getActualMaximum(MONTH)) {
						calendar.roll(MONTH, true);

						calendar.roll(YEAR, true);

					} else {
						calendar.roll(MONTH, true);
					}

				} else {
					calendar.roll(DAY_OF_MONTH, true);
				}

			} else {
				calendar.roll(HOUR_OF_DAY, true);

			}

			break;

		}

		calendar.set(MINUTE, nextCuarto);

	}

	public void setFechaHoraSiguienteMinuto() {
		// Obtiene la fecha y hora del siguiente minuto
		int minutoActual = getMinutos();
		int nextMinuto = minutoActual + 1;

		if (nextMinuto == 60) {

			nextMinuto = 0;
			if (getHora() == calendar.getActualMaximum(HOUR_OF_DAY)) {
				calendar.roll(HOUR_OF_DAY, true);

				if (getDia() == calendar.getActualMaximum(DAY_OF_MONTH)) {
					calendar.roll(DAY_OF_MONTH, true);

					if ((getMes() - 1) == calendar.getActualMaximum(MONTH)) {
						calendar.roll(MONTH, true);

						calendar.roll(YEAR, true);

					} else {
						calendar.roll(MONTH, true);
					}

				} else {
					calendar.roll(DAY_OF_MONTH, true);
				}

			} else {
				calendar.roll(HOUR_OF_DAY, true);

			}

		}

		calendar.set(MINUTE, nextMinuto);
		calendar.set(SECOND, 0);
		calendar.set(MILLISECOND, 0);

	}

	public int getYear() {
		return calendar.get(YEAR);
	}

	public int getMes() {
		return calendar.get(MONTH) + 1;
	}

	public int getSemanaMes() {
		return calendar.get(WEEK_OF_MONTH);

	}

	public void setSemanaMes(int semana) {
		calendar.set(WEEK_OF_MONTH, semana);

	}

	public int getUltimaSemanaMes() {
		return calendar.getActualMaximum(WEEK_OF_MONTH);

	}

	/**
	 * primera fecha del mes
	 * 
	 * @return la fecha
	 *
	public Date getInicioMes() {
		Date inicio;
		inicio = new Date(new GregorianCalendar(calendar.get(YEAR), calendar
				.get(MONTH), 1, 0, 0, 0).getTimeInMillis());
		return inicio;
	}

	/**
	 * última fecha del mes
	 * @return la fecha
	 
	public Date getFinMes() {
		Date fin;
		fin = new Date(new GregorianCalendar(calendar.get(YEAR), calendar
				.get(MONTH), calendar.getActualMaximum(DAY_OF_MONTH), 23, 59,
				59).getTimeInMillis() + 999);
		return fin;
	}

	public int getDia() {
		return calendar.get(DAY_OF_MONTH);
	}

	public void setDia(int dia) {
		calendar.set(DAY_OF_MONTH, dia);
	}

	/**
	 * Regresa el numero de dia Lunes = 1, Domingo = 7;
	 * 
	 *

	public int getDiaSemana() {
		int dia = calendar.get(DAY_OF_WEEK) - 1;

		if (dia == 0) {
			dia = 7;
		}

		return dia;

	}

	public void setDiaSemana(int diaSemana) {

		int dia = diaSemana + 1;

		if (dia == 8) {
			dia = 1;
		}

		calendar.set(DAY_OF_WEEK, dia);

		return;

	}

	public boolean isUltimoDiaMes() {
		return (getDia() == calendar.getActualMaximum(DAY_OF_MONTH));
	}

	public int getUltimoDiaMes() {
		return calendar.getActualMaximum(DAY_OF_MONTH);
	}

	public int getHora() {
		return calendar.get(HOUR_OF_DAY);
	}

	public int getMinutos() {
		return calendar.get(MINUTE);
	}

	public int getSegundos() {
		return calendar.get(SECOND);
	}

	@Override
	public String toString() {

		return FechaFormatter.formatTimeStamp(calendar.getTime());

	}

	public Date getDate() {

		return calendar.getTime();

	}

	public long getTimeInMilis() {

		return getDate().getTime();

	}

	public void incrementaHora() {
		incrementaHora(1);
	}

	public void incrementaHora(int horas) {
		calendar.add(HOUR_OF_DAY, horas);

	}

	public void incrementaDia() {
		calendar.add(DAY_OF_MONTH, 1);

	}

	public void incrementaMes() {
		calendar.add(MONTH, 1);

	}

	public void incrementaSemana() {
		calendar.add(WEEK_OF_MONTH, 1);

	}
	
	public void decrementaSemana(){
		calendar.add(WEEK_OF_MONTH, -1);
		
	}

	public void setInicioDia() {
		calendar.set(HOUR_OF_DAY, 0);
		calendar.set(MINUTE, 0);
		calendar.set(SECOND, 0);
		calendar.set(MILLISECOND, 0);

		return;

	}

	public void setInicioMes() {
		calendar.set(DAY_OF_MONTH, 1);
		setInicioDia();
	}

	public Date getFechaInicio() {

		setInicioDia();
		return calendar.getTime();

	}

	
	public Date getFechaFin() {
		calendar.set(HOUR_OF_DAY, 23);
		calendar.set(MINUTE, 59);
		calendar.set(SECOND, 59);
		calendar.set(MILLISECOND, 0);

		return calendar.getTime();

	}

*/
}
