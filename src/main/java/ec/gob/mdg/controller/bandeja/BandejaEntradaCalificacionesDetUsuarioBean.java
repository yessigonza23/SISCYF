package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Correo;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.service.ICorreoService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.dinardap.servicios.ServiciosWeb;
import ec.gob.mdg.utils.GenerarJson;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaCalificacionesDetUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	@Inject
	private IBanCatalogoEstadosService serviceBanCatalogoEstados;

	@Inject
	private ICorreoService serviceCorreo;

	private List<BandejaEntrada> listaBandejaEntrada = new ArrayList<>();
	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	private BanCatalogoEstados banCatalogoEstadosSiglas = new BanCatalogoEstados();
	BandejaEntrada bandeja = new BandejaEntrada();// nuevo registro
	private Correo correo = new Correo();

	String siglasTramite;
	String empresaS;
	String observacion;
	String detalle;
	String estado;

	Date fecha_fin;
	Date fecha_inicio;

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	@PostConstruct
	public void init() {
		correo = serviceCorreo.obtenerDatosCorreo();
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
		banTipoTramite = serviceBanTipoTramite.muestraPorSiglas(siglasTramite);
		try {
			fecha_inicio = formato.parse(
					(String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaInicio"));
			fecha_fin = formato
					.parse((String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaFin"));
			cargarDatos();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void cargarDatos() {

		if (fecha_inicio != null && fecha_fin != null) {
			listaBandejaEntrada = serviceBandejaEntrada.listarPorTipoTramiteUsuario(usuario, siglasTramite,
					fecha_inicio, fecha_fin);

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sin datos", "Error"));
		}
	}

	public void onRowSelect(SelectEvent<BandejaEntrada> event) throws Exception {
		estado = ((BandejaEntrada) event.getObject()).getBanCatalogoEstados().getSiglas();
		if (estado.equals("R") || estado.equals("T")) {
			if (estado.equals("R")) {
				cambiarAEstadoTramite(((BandejaEntrada) event.getObject()));
			}
			empresaS = String.valueOf(((BandejaEntrada) event.getObject()).getEmpresa().getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", empresaS);
			if (((BandejaEntrada) event.getObject()).getBanTipoTramite().getSiglas().equals("C")) {
				Utilitario.irAPagina("/pg/cal/entprincipalcal");
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "El trámite aun no ha sido procesado ", "Aviso, "));
		}
	}

	public void onRowUnselect(UnselectEvent<BandejaEntrada> event) throws Exception {
		estado = ((BandejaEntrada) event.getObject()).getBanCatalogoEstados().getSiglas();
		if (estado.equals("R") || estado.equals("T")) {
			if (estado.equals("R")) {
				cambiarAEstadoTramite(((BandejaEntrada) event.getObject()));
			}
			empresaS = String.valueOf(((BandejaEntrada) event.getObject()).getEmpresa().getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", empresaS);
			if (((BandejaEntrada) event.getObject()).getBanTipoTramite().getSiglas().equals("C")) {
				Utilitario.irAPagina("/pg/cal/entprincipalcal");
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "El trámite aun no ha sido procesado ", "Aviso, "));
		}
	}

	public void cambiarAEstadoTramite(BandejaEntrada b) throws Exception {
		if (b != null) {
			estado = "T";
			observacion = "La solicitud " + b.getNum_tramite() + " para acceder al trámite Calificación para el manejo"
					+ " de sustancias catalogadas sujetas a fiscalización "
					+ " se encuentra en trámite por el técnico del área de Control de SCSF: "
					+ b.getUsuario().getNombre();

			detalle = "La solicitud " + b.getNum_tramite() + ", para acceder al trámite Calificación para el manejo\r\n"
					+ " de sustancias catalogadas sujetas a fiscalización \r\n"
					+ " se encuentra en trámite por el técnico del área de Control de SCSF" + b.getUsuario().getNombre()
					+ "<div>" + "</div>" + "<br/>" + "<div>" + "Atentamente" + "</div>" + "<div>"
					+ correo.getMail_nombre_institucion() + "</div>";
			cambiarEstado(b, estado, observacion);
			enviarCorreo(b, detalle);
		}
	}

	public void devolverTramite(BandejaEntrada b, String observacion) throws Exception {
		System.out.println(observacion + " Observación");
		System.out.println(b + " Bandeja de entrada");
//		if (b != null && observacion!=null) {
//			estado = "D";
//			observacion = "Existen observaciones a la solicitud: " + b.getNum_tramite()
//					+ " para acceder al trámite Calificación para el manejo de sustancias catalogadas sujetas a fiscalización. "
//					+ "Realizar las correcciones solicitadas y volver a enviar el trámite. "
//					+ "Las observaciones son las siguientes: " + observacion;
//
//			detalle = "Existen observaciones a la solicitud: " + b.getNum_tramite()
//					+ " para acceder al trámite Calificación para el manejo de sustancias catalogadas sujetas a fiscalización.\r\n"
//					+ "Realizar las correcciones solicitadas y volver a enviar el trámite.\r\n"
//					+ "Las observaciones son las siguientes: " + observacion + "<div>" + "</div>" + "<br/>" + "<div>"
//					+ "Atentamente" + "</div>" + "<div>" + correo.getMail_nombre_institucion() + "</div>";
//			cambiarEstado(b, estado, observacion);
//			enviarCorreo(b, detalle);
//		}
	}

	public void cambiarEstado(BandejaEntrada b, String estado, String observacion) throws Exception {
		b.setVer(false);
		serviceBandejaEntrada.modificar(b);

		banCatalogoEstadosSiglas = serviceBanCatalogoEstados.muestraPorSiglas(estado);

		bandeja.setBanCatalogoEstados(banCatalogoEstadosSiglas);
		bandeja.setBanTipoTramite(b.getBanTipoTramite());
		bandeja.setEmpresa(b.getEmpresa());
		if (estado.equals("D")) {
			bandeja.setUsuario(null);
		} else {
			bandeja.setUsuario(b.getUsuario());
		}

		bandeja.setNum_tramite(b.getNum_tramite());
		bandeja.setObservacion(observacion);
		bandeja.setFecha(ec.gob.mdg.utils.UtilsDate.fechaActual());
		bandeja.setVer(true);
		serviceBandejaEntrada.registrar(bandeja);

		cargarDatos();
	}

	public void enviarCorreo(BandejaEntrada bandejaEntrada, String detalle) {

		Map<String, Object> parametros = new HashMap<>();
		parametros.put("institution", correo.getMail_nombre_institucion());
		parametros.put("system", "DE CONTROL DE SUSTANCIAS - CALIFICACIÓN DE SUSTANCIAS");
		parametros.put("from", correo.getMailEmisor());
		parametros.put("to", bandejaEntrada.getEmpresa().getCorreo_electronico());
		parametros.put("subject", banTipoTramite.getDescripcion_corta() + " - " + correo.getMail_nombre_institucion());
		parametros.put("message", Base64.getEncoder().encodeToString(detalle.getBytes(StandardCharsets.UTF_8)));
		parametros.put("cco", bandejaEntrada.getUsuario().getCorreo_electronico());
		parametros.put("includeTemplate", "true");

		String json = GenerarJson.generarJson(parametros);
		ServiciosWeb.enviarCorreo(json);
	}

	//// Regresar a bandeja de estados
	public void regresarBandejaEntrada() {
		Utilitario.irAPagina("/pg/ban/bandejaentradausuario");
	}

	public void mostrarData(BandejaEntrada i) {
		bandejaEntrada = i;
	}
}
