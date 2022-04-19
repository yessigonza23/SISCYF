package ec.gob.mdg.controller.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacion;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.CalrenTipoSustancia;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.Presentacion;
import ec.gob.mdg.control.ejb.modelo.TipoCambioSustancias;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasService;
import ec.gob.mdg.control.ejb.service.ICalrenTipoSustanciasService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.service.IPresentacionService;
import ec.gob.mdg.control.ejb.service.ITipoCambioSustanciasService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaEntidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenSustanciasService serviceSustancias;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalrenActividades;

	@Inject
	private ICalrenTipoSustanciasService serviceCalrenTipoSustancia;

	@Inject
	private IPresentacionService servicePresentacion;

	@Inject
	private ITipoCambioSustanciasService serviceTipoCambio;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalrenSustancias> listaSustancias = new ArrayList<>();
	private List<CalrenActividadesCalificacion> listaCalrenActividadesCalificacion = new ArrayList<>();
	private List<CalrenTipoSustancia> listaCalrenTipoSustancia = new ArrayList<>();
	private List<Presentacion> listaPresentacion = new ArrayList<>();
	private List<TipoCambioSustancias> listaTipoCambio = new ArrayList<>();

	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private CalrenSustancias calrenSustancias = new CalrenSustancias();
	private CalrenActividadesCalificacion calrenActividadesCalificacion = new CalrenActividadesCalificacion();
	private CalrenTipoSustancia calrenTipoSustancia = new CalrenTipoSustancia();

	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;
	Boolean render_sus = false;

	String empresaS;
	Integer empresaId;

	@PostConstruct
	public void init() throws Exception {
		cargarDatos();
		cargarListaPresentacion();
		cargarListaTipoCambio();
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public Empresa cargarDatos() {
		if (empresa != null) {			
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
			empresaId = Integer.parseInt(empresaS);
			render_n = false;
			render_o = false;
			render_j = false;
			render_p = false;
			render = false;
			if (empresaId != null) {
				render = true;
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
				cargarListaCalRen(empresa);
				if (empresa.getTipo_empresa().equals("n")) {
					render_n = true;
				} else if (empresa.getTipo_empresa().equals("j")) {
					render_j = true;
				} else if (empresa.getTipo_empresa().equals("o")) {
					render_o = true;
				} else if (empresa.getTipo_empresa().equals("p")) {
					render_p = true;
				}
			} else {
				render = false;
				render_n = false;
				render_o = false;
				render_j = false;
				render_p = false;
				empresa = null;
			}
		} else {
			empresa = null;
		}
		return empresa;
	}

	//////// CALIFICACIONES RENOVACIONES -
	public List<CalificacionesRenovaciones> cargarListaCalRen(Empresa empr) {
		if (empr != null) {
			
			this.listaCalRenovaciones = serviceCalRen.listarCalRenPorEmpresa(empr);
			if (listaCalRenovaciones != null && !listaCalRenovaciones.isEmpty()) {
				calificacionesRenovaciones = listaCalRenovaciones.get(0);
				listaSustancias = null;
				if (calificacionesRenovaciones != null) {
					cargarListaActividades(calificacionesRenovaciones.getId());
					cargarListaTipoSustancia(calificacionesRenovaciones.getId());
				}
			}
		} else {
			listaCalRenovaciones = null;
			calificacionesRenovaciones = null;
			listaSustancias = null;
			listaCalrenActividadesCalificacion = null;
			listaCalrenTipoSustancia = null;
		}
		return listaCalRenovaciones;
	}

	//// Grabar observaciones para informe de calificacion
	public Integer operar(CalificacionesRenovaciones calren) {		
		try {
			if (calren != null) {
				System.out.println("entra para modificar calren");
				calren.setObservacion(calificacionesRenovaciones.getObservacion());
				this.serviceCalRen.modificar(calren);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}

	//// Selecciona Calificaciones renovaciones

	public void onRowSelect(SelectEvent event) {
		listaSustancias = null;
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		FacesMessage msg = new FacesMessage("CalificacionesRenovaciones",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		calrenTipoSustancia = null;
		FacesMessage msg = new FacesMessage("CalificacionesRenovaciones",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Cancelar",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	////////////////////////////////////// ACTIVIDADES
	public void cargarListaActividades(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenActividadesCalificacion = serviceCalrenActividades.listaCalrenActividades(id_calren);
		} else {
			listaCalrenActividadesCalificacion = null;
		}
	}

	//////////////////////////////////////// TIPO DE SUSTANCIA
	public void cargarListaTipoSustancia(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenTipoSustancia = serviceCalrenTipoSustancia.listaCalrenTipoSustancias(id_calren);
			if (listaCalrenTipoSustancia != null && !listaCalrenTipoSustancia.isEmpty()) {
				calrenTipoSustancia = listaCalrenTipoSustancia.get(0);
				if (calrenTipoSustancia != null) {
					cargarListaSustancias(calificacionesRenovaciones.getId(),
							calrenTipoSustancia.getTipoSustancia().getId());
				}
			}
		} else {
			listaCalrenTipoSustancia = null;
		}
	}

	public void onRowSelectTipo(SelectEvent event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
		FacesMessage msg = new FacesMessage("CalrenTipoSustancia",
				String.valueOf(((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getNombre()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselectTipo(UnselectEvent event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
		FacesMessage msg = new FacesMessage("CalrenTipoSustancia",
				String.valueOf(((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getNombre()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	///////////////////////////////////// SUSTANCIAS
	public void cargarListaSustancias(Integer id_calren, Integer id_tiposus) {
		if (id_calren != null) {
			this.listaSustancias = serviceSustancias.listarSustanciasEmpCalren(id_calren, id_tiposus);
		} else {
			listaSustancias = null;
		}
	}

	//// Grabar razon y estado
	public Integer operarSustancias(CalrenSustancias calrenSus) {
		
		try {
			if (calrenSus != null) {
				calrenSus.setEstado(calrenSustancias.getEstado());
				calrenSus.setTipoCambioSustancias(calrenSustancias.getTipoCambioSustancias());
				calrenSus.setCupo_asignado(calrenSustancias.getCupo_asignado());
				calrenSus.setPresentacion(calrenSustancias.getPresentacion());
				this.serviceSustancias.modificar(calrenSus);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}


	//////// LISTAR PRESENTACION
	public void cargarListaPresentacion() throws Exception {
		this.listaPresentacion = servicePresentacion.listar();
	}

	//////// LISTAR TIPO CAMBIOS
	public void cargarListaTipoCambio() throws Exception {
		this.listaTipoCambio = serviceTipoCambio.listar();
	}

}
