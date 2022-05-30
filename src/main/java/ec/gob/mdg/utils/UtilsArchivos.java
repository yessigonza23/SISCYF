package ec.gob.mdg.utils;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;




public class UtilsArchivos {

	private static final Properties p = System.getProperties();
	public static final String sep = p.getProperty("file.separator");

	private static final String rutaRaiz = (p.getProperty("os.name").compareToIgnoreCase("linux") == 0 ? "/opt"
			: p.getProperty("user.home")) + sep + "comprobanteselectronicos" + sep;

	private static final String sistemaOperativo = (p.getProperty("os.name").compareToIgnoreCase("linux") == 0 ? "UTF-8"
			: "Cp1252");

	// devuelve la ruta y si no existe la crea
	public static String crearRuta(String ruta) {
		File directorio = new File(ruta);
		if (!directorio.exists()) {
			directorio.mkdirs();
		}
		return ruta;
	}

	// devuelve true si elimina el archivo
	public static boolean eliminarArchivo(String archivo) {
		File directorio = new File(archivo);
		return directorio.delete();
	}

	public static String getRutaRaiz() {
		return crearRuta(rutaRaiz);
	}

	public static String getRutaCargaMasiva() {
		return crearRuta(getRutaRaiz() + "cargamasiva" + sep);
	}

	public static String getRutaCertificados() {
		return crearRuta(getRutaRaiz() + "certificados" + sep);
	}

	public static String getRutaFirmados() {
		return crearRuta(getRutaRaiz() + "firmados" + sep);
	}

	public static String getRutaAutorizados() {
		return crearRuta(getRutaRaiz() + "autorizados" + sep);
	}

	public static String getRutaGenerados() {
//		System.out.println("generados UtilArchivos ");
		return crearRuta(getRutaRaiz() + "generados" + sep);
	}

	public static String getRutaLogo() {
		return crearRuta(getRutaRaiz() + "logos" + sep);
	}

	public static String getRutaAdjuntos() {
		return crearRuta(getRutaRaiz() + "adjuntos" + sep);
	}

	public static String getRutaReportes() {
		return crearRuta(getRutaRaiz() + "reportes" + sep);
	}

	public static String getRutaReportesJasper() {
		return crearRuta(getRutaRaiz() + "reportesjasper" + sep);
	}
	// metodo para cargar archivos de cualquier tipo a una ubicacion determinada.
	// valido

//	public static String upload(UploadedFile file, String nombreArchivo) throws IOException {
//		String rutaAdjuntos = "";
//		try {
//			rutaAdjuntos = UtilsArchivos.getRutaCargaMasiva();
//
//			InputStream in = (InputStream) file.getInputstream();
//			File f = new File(rutaAdjuntos + nombreArchivo);
//			f.createNewFile();
//			FileOutputStream out1 = new FileOutputStream(f);
//			byte[] buffer = new byte[1024];
//			int length;
//			while ((length = in.read(buffer)) > 0) {
//				out1.write(buffer, 0, length);
//			}
//
//			String path = rutaAdjuntos + nombreArchivo;
//			String pathS = rutaAdjuntos + "S" + nombreArchivo;
//
//			String line = "";
//			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "Cp1252"));
//			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(pathS), sistemaOperativo));
//			try {
//				while ((line = br.readLine()) != null) {
//					out.write(line);
//					out.write("\n");
//				}
//			} finally {
//				br.close();
//				out.close();
//			}
//			out1.close();
//			in.close();
//
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto",
//					"El archivo " + file.getFileName() + " se guardo satisfactoriamente"));
//			return rutaAdjuntos;
//		} catch (Exception e) {
//			e.printStackTrace(System.out);
//		}
//		return rutaAdjuntos;
//
//	}

//	public static String uploadFirma(UploadedFile file, String nombreArchivo) throws IOException {
//		String rutaAdjuntos = "";
//
//		try {
//			rutaAdjuntos = UtilsArchivos.getRutaCertificados();
//
//			InputStream in = (InputStream) file.getInputstream();
//
//			File f = new File(rutaAdjuntos + nombreArchivo);
//
//			f.createNewFile();
//
//			FileOutputStream out = new FileOutputStream(f);
//
//			byte[] buffer = new byte[1024];
//
//			int length;
//			while ((length = in.read(buffer)) > 0) {
//				out.write(buffer, 0, length);
//			}
//
//			out.close();
//			in.close();
//
//			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Correcto",
//					"El certificado: " + file.getFileName() + " se carga satisfactoriamente"));
//			return rutaAdjuntos;
//		} catch (Exception e) {
//			e.printStackTrace(System.out);
//		}
//		return rutaAdjuntos;
//
//	}

	@SuppressWarnings("resource")
	public static void verXls(String archivo) throws IOException {
		File ficheroXLS = new File(UtilsArchivos.getRutaAdjuntos() + archivo);

		FacesContext ctx = FacesContext.getCurrentInstance();
		FileInputStream fis = new FileInputStream(ficheroXLS);
		byte bytes[] = new byte[1000];
		int read = 0;
		if (!ctx.getResponseComplete()) {
			String nombre = archivo;
//			String contentType = "application/pdf";
			String contentType = "application/vnd.ms-excel";
			HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
			response.setContentType(contentType);
			response.setHeader("Content-Disposition",
					(new StringBuilder()).append("attachment;filename=\"").append(nombre).append("\"").toString());
			ServletOutputStream out = response.getOutputStream();
			while ((read = fis.read(bytes)) != -1)
				out.write(bytes, 0, read);
			out.flush();
			out.close();
			// System.out.println("Descargado");
			ctx.responseComplete();
		}
	}

	@SuppressWarnings({ "resource", "unused" })
	public static void verPDF(String nombre_ruta) throws IOException {
		 FacesContext ctx;
			ServletContext request;
	        File archPDF;
	        FileInputStream fisArch;
	        byte[] bytes;
	        int leer;
	        String nombreArchivo;

	        ctx = FacesContext.getCurrentInstance();
	        request = (ServletContext)ctx.getExternalContext().getContext();
	        archPDF = new File(nombre_ruta);
	        fisArch = new FileInputStream(archPDF);
	        bytes = new byte[1000];
	        leer = 0;

	        if (!ctx.getResponseComplete()) {

	            nombreArchivo = archPDF.getName();
	            String contentType = "application/pdf";
	            HttpServletResponse response =
	                (HttpServletResponse)ctx.getExternalContext().getResponse();

	            response.setContentType(contentType);
	            response.setHeader("Content-Disposition",
	                               "attachment;filename=\"" + nombreArchivo );
	            ServletOutputStream out = response.getOutputStream();

	            while ((leer = fisArch.read(bytes)) != -1) {
	                out.write(bytes, 0, leer);
	            }

	            out.flush();
	            out.close();
	            System.out.println("\nLayout descargado...\n");
	            ctx.responseComplete();
	        }
	}

}
