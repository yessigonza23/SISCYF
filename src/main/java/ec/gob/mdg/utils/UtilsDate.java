package ec.gob.mdg.utils;


import static java.time.temporal.ChronoUnit.DAYS;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class UtilsDate {
	
	private static final SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
	
	public static Timestamp fechaActual() {
		return new Timestamp(new Date().getTime());
	}
	public static int compareTo(Date fecha, Date fecha1) {
		return fechaFormatoDate(fecha).compareTo(fechaFormatoDate(fecha1));
	}
	

	public static Date date(Timestamp fecha) {
		return new Date(fecha.getTime());
	}
	

	public static Date dateCompleto(Date fecha) {
		return new Date(fecha.getTime() + 86399999);
	}
	

	public static String dateFin(Date fecha) {
		return fechaFormatoString(fecha) + " 23:59:59.999";
	}
	

	public static String dateInicio(Date fecha) {
		return fechaFormatoString(fecha) + " 00:00:00.000";
	}
	

	public static Date fechaFormatoDate(Date fecha) {
		return fechaFormatoDate(fechaFormatoString(fecha));
	}
	

	public static Date fechaFormatoDate(Date fecha, String formato) {
		try {
			return new SimpleDateFormat(formato).parse(fechaFormatoString(fecha, formato));
		} catch (ParseException e) {
			return null;
		}
	}
	

	public static Date fechaFormatoDate(String fecha) {
		try {
			return formatoFecha.parse(fecha);
		} catch (ParseException e) {
			return new Date();
		}
	}
	

	public static Date fechaFormatoDate(String fecha, String formato) {
		try {
			return new SimpleDateFormat(formato).parse(fecha);
		} catch (ParseException e) {
			return null;
		}
	}
	

	public static String fechaFormatoString(Date fecha) {
		return formatoFecha.format(fecha);
	}
	

	public static String fechaFormatoString(Date fecha, String formato) {
		try {
			return new SimpleDateFormat(formato).format(fecha);
		} catch (Exception e) {
			return null;
		}
	}
	

	public static String fechaFormatoString(String fecha) {
		return fechaFormatoString(fechaFormatoDate(fecha));
	}
	

	public static String fechaFormatoString(Timestamp fecha) {
		return formatoFecha.format(fecha);
	}
	

	public static Timestamp timestamp() {
		return new Timestamp(new Date().getTime());
	}
	


	public static Date dateActual() {
		return new Date();
	}

	
	
	public static Timestamp timestamp(Date fecha) {
		try {
			return new Timestamp(fecha.getTime());
		} catch (Exception e) {
			return null;
		}
	}
	

	public static Timestamp timestamp(String fecha) {
		return new Timestamp(fechaFormatoDate(fecha).getTime());
	}
	

	public static Timestamp timestampCompleto(Date fecha) {
		Calendar c = Calendar.getInstance();
		Calendar c1 = Calendar.getInstance();
		c.setTime(fecha);
		c.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, c1.get(Calendar.SECOND));
		return new Timestamp(c.getTime().getTime());
	}
	

	public static int getNumeroDia(String fecha, String formato) {
		Date date = fechaFormatoDate(fecha, formato);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	

	public static int getNumeroMes(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.get(Calendar.MONTH);
	}
	

	public static int getNumeroMes(String fecha, String formato) {
		Date date = fechaFormatoDate(fecha, formato);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.get(Calendar.MONTH);
	}
	

	public static int getNumeroAnio(Date fecha) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(fecha);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.get(Calendar.YEAR);
	}
	

	public static int getNumeroAnio(String fecha, String formato) {
		Date date = fechaFormatoDate(fecha, formato);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendar.get(Calendar.YEAR);
	}
	

	public static Date getPrimerDiaDelMes(Date fecha) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH),
				calendario.getActualMinimum(Calendar.DAY_OF_MONTH));
		return calendario.getTime();
	}
	

	public static Date getUltimoDiaDelMes(Date fecha) {
		Calendar calendario = Calendar.getInstance();
		calendario.setTime(fecha);
		calendario.set(calendario.get(Calendar.YEAR), calendario.get(Calendar.MONTH),
				calendario.getActualMaximum(Calendar.DAY_OF_MONTH));
		return calendario.getTime();
	}
	

	public static Date sumarDias(Date fechaInicio, int dias) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(fechaInicio);
		calendar.add(Calendar.DATE, dias);
		return calendar.getTime();
	}
	

	public static void diferenciaSegundos(String nombre, Timestamp fechaInicio, Timestamp fechaFin) {
		System.out.println("************** " + nombre + " ***************");
		System.out.println("Inicio: " + fechaInicio);
		System.out.println("Fin: " + fechaFin);
		System.out.println("Tiempo del metodo en ms: " + (fechaFin.getTime() - fechaInicio.getTime()) + " ; ss: "
				+ (double) (fechaFin.getTime() - fechaInicio.getTime()) / 1000);
		System.out.println("*************************************");
	}
	

	public static Date fechaMayor(Date fecha1, Date fecha2) {
		if (fecha1.compareTo(fecha2) == 0)
			return fecha1;
		else if (fecha1.compareTo(fecha2) > 0)
			return fecha1;
		else
			return fecha2;
	}
	

	public static boolean fecha1EsMayorFecha2(Date fecha1, Date fecha2) {
		if (fecha1.compareTo(fecha2) == 0)
			return false;
		else if (fecha1.compareTo(fecha2) > 0)
			return true;
		else
			return false;
	}
	

	public static Date DeStringADate(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		String strFecha = fecha;
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(strFecha);
			//System.out.println(fechaDate.toString());
			return fechaDate;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return fechaDate;
		}
	}
	
	public static Date DeStringADateDinardap(String fecha) {
		SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
		String strFecha = fecha;
		Date fechaDate = null;
		try {
			fechaDate = formato.parse(strFecha);
			//System.out.println(fechaDate.toString());
			return fechaDate;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return fechaDate;
		}
	}
	
	public static LocalDate DeStringADateDinardap1(String fecha) {
		LocalDate localDate = null;
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			localDate = LocalDate.parse(fecha, formatter);
			return localDate;
		} catch (Exception ex) {
			ex.printStackTrace();
			return localDate;
		}
	}

	public static String DeDateAString(Date fecha) {
		DateFormat fechaHora = new SimpleDateFormat("dd-MMM-yyyy");
		return fechaHora.format(fecha);
	}
	

	public static int diferenciaEntreFechas(Date a, Date b) {
		int tempDifference = 0;
		int difference = 0;
		Calendar earlier = Calendar.getInstance();
		Calendar later = Calendar.getInstance();

		if (a.compareTo(b) < 0) {
			earlier.setTime(a);
			later.setTime(b);
		} else {
			earlier.setTime(b);
			later.setTime(a);
		}

		while (earlier.get(Calendar.YEAR) != later.get(Calendar.YEAR)) {
			tempDifference = 365 * (later.get(Calendar.YEAR) - earlier.get(Calendar.YEAR));
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		if (earlier.get(Calendar.DAY_OF_YEAR) != later.get(Calendar.DAY_OF_YEAR)) {
			tempDifference = later.get(Calendar.DAY_OF_YEAR) - earlier.get(Calendar.DAY_OF_YEAR);
			difference += tempDifference;

			earlier.add(Calendar.DAY_OF_YEAR, tempDifference);
		}

		return difference;
	}

	public static String convetirDateConFormato(Date fechaSinFormato) {
		return new SimpleDateFormat("yyyy-MM-dd").format(fechaSinFormato);
	}
	

	
	
	//utilitario de operaciones con campos fechas
	
    public static int edad(Date d) {

        int resultado = 0;
        if (d == null) { return resultado; }
        new SimpleDateFormat("dd/MM/yyyy");
        Date aux = new Date();
        if (aux.before(d)) { return resultado; }
        Calendar nacimiento = GregorianCalendar.getInstance();
        nacimiento.setTime(d);
        Calendar hoy = GregorianCalendar.getInstance();
        hoy.setTime(aux);
        resultado = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        Calendar mesDiaNacimiento = calendarConCamposEnCero();
        mesDiaNacimiento.set(Calendar.MONTH, nacimiento.get(Calendar.MONTH));
        mesDiaNacimiento.set(Calendar.DAY_OF_MONTH, nacimiento.get(Calendar.DAY_OF_MONTH));
        Calendar mesDiaActual = calendarConCamposEnCero();
        mesDiaActual.set(Calendar.MONTH, hoy.get(Calendar.MONTH));
        mesDiaActual.set(Calendar.DAY_OF_MONTH, hoy.get(Calendar.DAY_OF_MONTH));
        if (mesDiaNacimiento.after(mesDiaActual)) {
            resultado--;
        }
        return resultado;
    }

    public static int edadCalculadaACiertaFecha(Date fechaNacimiento, Date fechaFinal) {

        int resultado = 0;
        if (fechaNacimiento == null) { return resultado; }
        new SimpleDateFormat("dd/MM/yyyy");
        // ////System.out.println("Fecha: " + df.format(fechaNacimiento));
        Date aux = fechaFinal;
        // ////System.out.println("Fecha de Deportacion: " + df.format(aux));
        if (aux.before(fechaNacimiento)) { return resultado; }
        Calendar nacimiento = GregorianCalendar.getInstance();
        nacimiento.setTime(fechaNacimiento);
        Calendar fechaDeportacionC = GregorianCalendar.getInstance();
        fechaDeportacionC.setTime(aux);
        resultado = fechaDeportacionC.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR);
        // ////System.out.println("Diferencia parcial en años: " + resultado);
        Calendar mesDiaNacimiento = UtilsDate.calendarConCamposEnCero();
        mesDiaNacimiento.set(Calendar.MONTH, nacimiento.get(Calendar.MONTH));
        mesDiaNacimiento.set(Calendar.DAY_OF_MONTH, nacimiento.get(Calendar.DAY_OF_MONTH));
        // ////System.out.println("Para el calculo con aproximacion mes/dia: ");
        // ////System.out.println("mes/dia de nac: " +
        // df.format(mesDiaNacimiento.getTime()));
        Calendar mesDiaActual = calendarConCamposEnCero();
        mesDiaActual.set(Calendar.MONTH, fechaDeportacionC.get(Calendar.MONTH));
        mesDiaActual.set(Calendar.DAY_OF_MONTH, fechaDeportacionC.get(Calendar.DAY_OF_MONTH));
        // ////System.out.println("mes/dia de hoy: " +
        // df.format(mesDiaActual.getTime()));
        if (mesDiaNacimiento.after(mesDiaActual)) {
            resultado--;
        }
        // ////System.out.println("Resultado: " + resultado);
        return resultado;
    }

    private static Calendar calendarConCamposEnCero() {

        Calendar resultado = GregorianCalendar.getInstance();
        resultado.set(Calendar.YEAR, 0);
        resultado.set(Calendar.MONTH, 0);
        resultado.set(Calendar.DAY_OF_MONTH, 0);
        resultado.set(Calendar.HOUR, 0);
        resultado.set(Calendar.MINUTE, 0);
        resultado.set(Calendar.SECOND, 0);
        resultado.set(Calendar.MILLISECOND, 0);
        return resultado;
    }

    public static boolean fechaIgual(Date f1, Date f2) {
        if (f1 == null || f2 == null) { return false; }
        return f1.equals(f2);
    }

    public static boolean fechaAntes(Date f1, Date f2) {
        if (f1 == null || f2 == null) { return false; }
        return f1.before(f2);
    }

    public static boolean fechaDespues(Date f1,Date f2) {
        if (f1 == null || f2 == null) { return false; }
        return f1.after(f2);
    }

    public static boolean fechaIgualDespues(Date f1,Date f2) {
        if (f1 == null || f2 == null) { return false; }
        return !f1.before(f2);
    }

    public static boolean fechaIgualAntes(Date f1, Date f2) {
        if (f1 == null || f2 == null) { return false; }
        return !f1.after(f2);
    }

    public static int diasEntreFechas(Date di, Date df) {
        int resultado = 0;
        if (di == null || df == null) { return resultado; }
        Calendar fi = new GregorianCalendar();
        fi.setTime(di);
        fi.set(Calendar.HOUR_OF_DAY, 23);
        fi.set(Calendar.MINUTE, 59);
        fi.set(Calendar.SECOND, 00);
        Calendar ff = new GregorianCalendar();
        ff.setTime(df);
        ff.set(Calendar.HOUR_OF_DAY, 23);
        ff.set(Calendar.MINUTE, 59);
        fi.set(Calendar.SECOND, 00);
        Long long1 = Long.valueOf((ff.getTime().getTime() - fi.getTime().getTime()) / (1000 * 60 * 60 * 24));
        int dias = long1.intValue();
        resultado = dias;
        return resultado + 1;
    }

	public static long diasEntreFechas1(LocalDate di, LocalDate df) {
        int resultado = 0;
        if (di == null || df == null) { return resultado; }
		return DAYS.between(di, df);
	}


    public static String fechaFormateada(Date fecha) {
        DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy");
        return dformat.format(fecha);
    }

    public static Date fechaFormateadaDate(Date fechaIngreso) {

        Date fechaFormateada = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (fechaIngreso != null) {
            String fecha = sdf.format(fechaIngreso);
            try {
                fechaFormateada = sdf.parse(fecha);
            }
            catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return fechaFormateada;
    }

    public static String fechaHoraFormateada(Date fecha) {
        DateFormat dformat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return dformat.format(fecha);
    }

    public static Date fechaSoloComoDia(Date fecha) {

        if(fecha == null) {
            return null;
        }
        
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(fecha);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        
        return c.getTime();
    }

	
	
	
	
	
	
	
}