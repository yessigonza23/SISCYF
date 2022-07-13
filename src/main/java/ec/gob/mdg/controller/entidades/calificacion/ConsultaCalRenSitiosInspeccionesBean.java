package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.BanTipoTramiteDetalle;
import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.Sitios;
import ec.gob.mdg.control.ejb.modelo.SitiosInspecciones;
import ec.gob.mdg.control.ejb.modelo.SitiosInspeccionesCheck;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteDetalleService;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.service.ISitiosInspeccionesCheckService;
import ec.gob.mdg.control.ejb.service.ISitiosInspeccionesService;
import ec.gob.mdg.control.ejb.service.ISitiosService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalRenSitiosInspeccionesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ISitiosService serviceSitios;

	@Inject
	private ISitiosInspeccionesService serviceSitiosInsp;

	@Inject
	private ISitiosInspeccionesCheckService serviceSitiosInspCheck;

	@Inject
	private ICoordinacionService serviceCoordinacion;

	@Inject
	private IBanTipoTramiteDetalleService serviceBanTipTraDet;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<Sitios> listaSitios = new ArrayList<>();
	private List<SitiosInspecciones> listaSitiosInspecciones = new ArrayList<>();
	private List<SitiosInspeccionesCheck> listaSitiosInspCheck = new ArrayList<>();
	private List<Coordinacion> listaCoordinacion = new ArrayList<>();
	private List<BanTipoTramiteDetalle> listaBanTipoTramiteDetalle = new ArrayList<>();

	private CalificacionesRenovaciones calren = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private Sitios sitios = new Sitios();
	private SitiosInspecciones sitiosInspecciones = new SitiosInspecciones();
	private SitiosInspeccionesCheck sitiosInspeccionesCheck = new SitiosInspeccionesCheck();
	private Coordinacion coordinacion = new Coordinacion();
	private Coordinacion coordinacionUsuario = new Coordinacion();
	private BanTipoTramiteDetalle banTipoTramiteDetalle = new BanTipoTramiteDetalle();

	String sitiosS;
	String calrenS;
	Integer sitiosId;
	private String tipoDialog = null;
	Boolean render = false;

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	@PostConstruct
	public void init() {
		try {
			coordinacionUsuario = serviceCoordinacion.buscaPorId(usuario.getCoordinacion().getId());
			listarCoordinacion();
			this.tipoDialog = "Nueva";
			sitiosInspecciones = new SitiosInspecciones();
			cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("sitios");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() throws Exception {
		sitiosS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("sitios");
		sitiosId = Integer.parseInt(sitiosS);
		if (sitiosId != null) {
			sitios = serviceSitios.sitioPorId(sitiosId);
			calren = serviceCalRen.calrenPorId(sitios.getCalificacionesRenovaciones().getId());
			empresa = serviceEmpresa.listarEmpresaPorId(calren.getEmpresa().getId());
			cargarListaInspecciones(sitios);
			if (listaSitiosInspecciones != null && !listaSitiosInspecciones.isEmpty()) {
				sitiosInspecciones = listaSitiosInspecciones.get(0);
				if (sitiosInspecciones != null) {
					cargarListaCheck(sitiosInspecciones);
				}
			}
		} else {
			empresa = null;
		}
	}

	////////////////////////////////////// SITIOS
	public void cargarListaInspecciones(Sitios sitios) {
		if (sitios != null) {
			listaSitiosInspecciones = serviceSitiosInsp.buscarPorIdSitio(sitios);
		} else {
			listaSitiosInspecciones = null;
		}
	}

	public void limpiarInspecciones() {
		coordinacion = coordinacionUsuario;
		sitiosInspecciones = new SitiosInspecciones();
		sitiosInspecciones.setCoordinacion(coordinacion);
		tipoDialog = "Nueva Inspección";
	}

	public void mostrarInspecciones(SitiosInspecciones i) {

		if (usuario.getRol().equals("C") && i.getEstado() == true) {
			this.sitiosInspecciones = i;
			tipoDialog = "Modificar Inspección";
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "No puede modificar", "Inspección inactivada"));
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				cambiarEstado();
				SitiosInspecciones sitiosIns = new SitiosInspecciones();
				if (coordinacion.getNombre().equals(coordinacionUsuario.getNombre())) {
					sitiosIns.setUsuario(usuario);
					sitiosIns.setCoordinacion(coordinacionUsuario);
				} else {
					sitiosIns.setCoordinacion(coordinacion);
				}
				sitiosIns.setSitios(sitios);
				sitiosIns.setFecha_inspeccion(sitiosInspecciones.getFecha_inspeccion());
				this.serviceSitiosInsp.registrar(sitiosIns);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceSitiosInsp.modificar(sitiosInspecciones);
			}
			this.cargarListaInspecciones(sitios);
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void cambiarEstado() throws Exception {
		if (listaSitiosInspecciones != null && !listaSitiosInspecciones.isEmpty()) {
			for (SitiosInspecciones s : listaSitiosInspecciones) {
				s.setEstado(false);
				serviceSitiosInsp.modificar(s);
			}
		}
	}

	// LISTAR CHECK DE INSPECCIONES
	public void cargarListaCheck(SitiosInspecciones si) throws Exception {

		if (si != null) {
			this.listaSitiosInspCheck = serviceSitiosInspCheck.buscarPorIdSitioIns(si);
			if (listaSitiosInspCheck == null) {
				listaBanTipoTramiteDetalle = serviceBanTipTraDet.listaPorTramite("C");
				if (listaBanTipoTramiteDetalle != null) {
					for (BanTipoTramiteDetalle b : listaBanTipoTramiteDetalle) {
						SitiosInspeccionesCheck check = new SitiosInspeccionesCheck();
						check.setBanTipoTramiteDetalle(b);
						check.setSitiosInspecciones(si);
						listaSitiosInspCheck.add(check);
					}
					for(SitiosInspeccionesCheck s : listaSitiosInspCheck) {
						serviceSitiosInspCheck.registrar(s);
					}
					
					this.listaSitiosInspCheck = serviceSitiosInspCheck.buscarPorIdSitioIns(si);
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"No tiene el listado del check para este trámite", "Error"));
				}
			}
		}
	}

	public void limpiarInspeccionesCheck() {
		sitiosInspeccionesCheck = new SitiosInspeccionesCheck();
		tipoDialog = "Nuevo Check";
	}

	public void mostrarInspeccionesChecK(SitiosInspeccionesCheck i) {
		this.sitiosInspeccionesCheck = i;
		tipoDialog = "Modificar Check";
	}

	//// LISTAR COORDINACIONES
	public void listarCoordinacion() throws Exception {
		listaCoordinacion = serviceCoordinacion.listar();
	}

	/// Ir a Sitios
	public void regresarSitios(Sitios sitios) {
		if (sitios != null) {
			calrenS = String.valueOf(sitios.getCalificacionesRenovaciones().getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("calren", calrenS);
			Utilitario.irAPagina("/pg/cal/calrensitioscal");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos no puede continuar", "Sin datos"));
		}

	}

}
