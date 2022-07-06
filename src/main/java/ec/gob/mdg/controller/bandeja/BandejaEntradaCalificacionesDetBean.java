package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
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

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Correo;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.service.ICorreoService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.dinardap.servicios.ServiciosWeb;
import ec.gob.mdg.utils.GenerarJson;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaCalificacionesDetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IUsuarioService serviceUsuario;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	@Inject
	private IBanCatalogoEstadosService serviceBanCatalogoEstados;

	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;

	@Inject
	private ICorreoService serviceCorreo;

	private List<BandejaEntrada> listaBandejaEntrada = new ArrayList<>();
	private List<Usuario> listaUsuarios = new ArrayList<>();

	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();
	private BanCatalogoEstados banCatalogoEstados = new BanCatalogoEstados();
	private BanCatalogoEstados banCatalogoEstadosSiglas = new BanCatalogoEstados();
	private Usuario us = new Usuario();
	BandejaEntrada bandeja = new BandejaEntrada();// nuevo registro
	private Correo correo = new Correo();

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	String siglasTramite;
	String siglasEstado;
	String empresaS;
	Date fecha_inicio;
	Date fecha_fin;
	String renderAsigna;
	String detalle;
	Boolean render;

	@PostConstruct
	public void init() {
		try {
			correo = serviceCorreo.obtenerDatosCorreo();
			listarUsuarios();
			siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
			siglasEstado = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("est_siglas");
			renderAsigna = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash()
					.get("renderAsigna");
			banTipoTramite = serviceBanTipoTramite.muestraPorSiglas(siglasTramite);
			banCatalogoEstados = serviceBanCatalogoEstados.muestraPorSiglas(siglasEstado);
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
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
		if (renderAsigna != null) {
			if (renderAsigna.equals("T")) {
				render = true;
			}
		}
		if (siglasEstado != null && siglasTramite != null && usuario != null && fecha_inicio != null
				&& fecha_fin != null) {
			listaBandejaEntrada = serviceBandejaEntrada.listarPorEstado(siglasTramite, siglasEstado, fecha_inicio,
					fecha_fin, usuario);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sin datos", "Error, "));
		}
	}

	public void asignarUsuario(BandejaEntrada bandejaEntrada) throws Exception {
		if (bandejaEntrada != null) {
			banCatalogoEstadosSiglas = serviceBanCatalogoEstados.muestraPorSiglas("R");
			bandejaEntrada.setVer(false);
			serviceBandejaEntrada.modificar(bandejaEntrada);

			bandeja.setBanCatalogoEstados(banCatalogoEstadosSiglas);
			bandeja.setBanTipoTramite(banTipoTramite);
			bandeja.setEmpresa(bandejaEntrada.getEmpresa());
			bandeja.setUsuario(bandeja.getUsuario());
			bandeja.setNum_tramite(bandejaEntrada.getNum_tramite());
			bandeja.setObservacion("La solicitud " + bandeja.getNum_tramite()
					+ " para acceder al trámite Calificación para el manejo de sustancias catalogadas sujetas a fiscalización ha sido asignada a un técnico del área de Control de SCSF " + bandeja.getUsuario().getNombre() );
			bandeja.setFecha(ec.gob.mdg.utils.UtilsDate.fechaActual());
			bandeja.setVer(true);
			serviceBandejaEntrada.registrar(bandeja);

			detalle = "La solicitud " + bandeja.getNum_tramite()
					+ " para acceder al trámite Calificación para el manejo\r\n"
					+ " de sustancias catalogadas sujetas a fiscalización \r\n"
					+ " se encuentra asignada al técnico del área de Control de SCSF: " + bandeja.getUsuario().getNombre()+"." 
					+ "</div>" + "<div>" + "<br/>" + "<div>" + "Atentamente"
					+ "</div>" + "<div>" + correo.getMail_nombre_institucion() + "</div>";
			renderAsigna = "F";
			cargarDatos();
			enviarCorreo(bandeja, detalle);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sin datos", "Error, "));
		}
	}

	public void enviarCorreo(BandejaEntrada bandejaEntrada, String detalle) {
		if (bandejaEntrada != null && detalle != null) {
			Map<String, Object> parametros = new HashMap<>();
			parametros.put("institution", correo.getMail_nombre_institucion());
			parametros.put("system", "DE CONTROL DE SUSTANCIAS - CALIFICACIÓN DE SUSTANCIAS");
			parametros.put("from", correo.getMailEmisor());
			parametros.put("to", bandejaEntrada.getEmpresa().getCorreo_electronico());
			parametros.put("subject",
					banTipoTramite.getDescripcion_corta() + " - " + correo.getMail_nombre_institucion());
			parametros.put("message", Base64.getEncoder().encodeToString(detalle.getBytes(StandardCharsets.UTF_8)));
			parametros.put("cco", bandejaEntrada.getUsuario().getCorreo_electronico());
			parametros.put("includeTemplate", "true");
			String json = GenerarJson.generarJson(parametros);
			ServiciosWeb.enviarCorreo(json);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sin datos", "Error, "));
		}
	}

	public void listarUsuarios() {
		try {
			listaUsuarios = serviceUsuario.listaUsuariosInternosPorUsuario(usuario);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void mostrar(BandejaEntrada i) {
		bandejaEntrada = i;
	}

	/// Regresar a bandeja de tramites
	public void regresarBandeja() {
		Utilitario.irAPagina("/pg/ban/bandejaentrada");
	}

	//// Regresar a bandeja de estados
	public void regresarBandejaEstados() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("tramite", siglasTramite);
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);
		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificacion");
	}

}
