package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
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

	private List<BandejaEntrada> listaBandejaEntrada = new ArrayList<>();
	private List<Usuario> listaUsuarios = new ArrayList<>();

	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();
	private BanCatalogoEstados banCatalogoEstados = new BanCatalogoEstados();

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	String siglasTramite;
	String siglasEstado;
	String empresaS;
	Date fecha_inicio;
	Date fecha_fin;
	String renderAsigna;
	Boolean render;

	@PostConstruct
	public void init() {

		try {
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

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		if (renderAsigna != null) {
			if (renderAsigna.equals("T")) {
				render = true;
			}
		}
		listaBandejaEntrada = serviceBandejaEntrada.listarPorEstado(siglasTramite, siglasEstado, fecha_inicio,
				fecha_fin, usuario);
	}

	public void listarUsuarios() {
		try {
			listaUsuarios = serviceUsuario.listar();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// Regresar a bandeja de tramites
	public void regresarBandeja() {
		Utilitario.irAPagina("/pg/ban/bandejaentrada");
	}

	//// Regresar a bandeja de estados
	public void regresarBandejaEstados() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("tramite", siglasTramite);
		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificacion");
	}

}
