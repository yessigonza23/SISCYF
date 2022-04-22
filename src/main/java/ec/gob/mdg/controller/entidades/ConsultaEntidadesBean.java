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
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividades;
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
import ec.gob.mdg.control.ejb.service.impl.CalrenSustanciasActividadesServiceImpl;
import ec.gob.mdg.control.ejb.utils.Conversiones;
import ec.gob.mdg.control.funciones.CalculosCalRen;
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

	@Inject
	private CalculosCalRen serviceCalculos;
	
	@Inject
	private CalrenSustanciasActividadesServiceImpl serviceSusActividades;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalrenSustancias> listaSustancias = new ArrayList<>();
	private List<CalrenSustancias> listaSustanciasReqRT = new ArrayList<>();
	private List<CalrenActividadesCalificacion> listaCalrenActividadesCalificacion = new ArrayList<>();
	private List<CalrenTipoSustancia> listaCalrenTipoSustancia = new ArrayList<>();
	private List<Presentacion> listaPresentacion = new ArrayList<>();
	private List<TipoCambioSustancias> listaTipoCambio = new ArrayList<>();
	private List<CalrenSustanciasActividades> listaSustanciasActividades = new ArrayList<>();

	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private CalificacionesRenovaciones calren = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private CalrenSustancias calrenSustancias = new CalrenSustancias();
	private CalrenActividadesCalificacion calrenActividadesCalificacion = new CalrenActividadesCalificacion();
	private CalrenTipoSustancia calrenTipoSustancia = new CalrenTipoSustancia();
	private CalrenSustanciasActividades calrenSustanciasActividades = new CalrenSustanciasActividades();

	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;
	Boolean render_sus = false;
	Integer categoria = 0;
	Double valorKilos;
	String mensajeReqRT;

	String empresaS;
	Integer empresaId;

	@PostConstruct
	public void init() {
		try {
			cargarDatos();
			cargarListaPresentacion();
			cargarListaTipoCambio();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			if (calren != null && calren.getAprobado().equals("N")) {
				calren.setObservacion(calificacionesRenovaciones.getObservacion());
				this.serviceCalRen.modificar(calren);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha grabado la observación", "Actualización Realizada"));
			}else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos o No puede realizar cambios", "Calificación/Renovación Aprobada"));
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
	}

	public void onRowUnselect(UnselectEvent event) {
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		calrenTipoSustancia = null;
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
					requiereRT(calificacionesRenovaciones.getId());
				}
			}
		} else {
			listaCalrenTipoSustancia = null;
		}
	}

	public void onRowSelectTipo(SelectEvent event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getCalificacionesRenovaciones().getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
	}

	public void onRowUnselectTipo(UnselectEvent event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getCalificacionesRenovaciones().getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
	}

	///////////////////////////////////// SUSTANCIAS
	public void cargarListaSustancias(Integer id_calren, Integer id_tiposus) {
		if (id_calren != null) {
			this.listaSustancias = serviceSustancias.listarSustanciasEmpCalren(id_calren, id_tiposus);
			
		} else {
			listaSustancias = null;
		}
	}

	//// Grabar cupo y estado
	public Integer operarSustancias(CalrenSustancias calrenSus) {
		try {
			if (calrenSus != null) {
				calren = serviceCalRen.calrenPorId(calrenSus.getCalificacionesRenovaciones().getId());
				if (calren.getAprobado().equals("N")) {
					calrenSus.setEstado(calrenSustancias.getEstado());
					calrenSus.setTipoCambioSustancias(calrenSustancias.getTipoCambioSustancias());
					calrenSus.setCupo_asignado(calrenSustancias.getCupo_asignado());
					calrenSus.setPresentacion(calrenSustancias.getPresentacion());
					this.serviceSustancias.modificar(calrenSus);				
					actualizaCalren(calrenSus.getCalificacionesRenovaciones().getId());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio Exitoso", "Actualización completa"));
				}else if (calren.getAprobado().equals("S")) {
					calrenSus.setEstado(calrenSustancias.getEstado());
					calrenSus.setTipoCambioSustancias(calrenSustancias.getTipoCambioSustancias());
					this.serviceSustancias.modificar(calrenSus);				
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio de estado y tipo de cambio exitoso", "Actualización Realizada"));
				}
				
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}

	public void actualizaCalren(Integer calren) throws Exception {
		if (calren != null) {
			valorKilos = serviceCalculos.ValorKilosEntidad(calren);
			valorKilos = Conversiones.formatearDecimales(valorKilos, 4);
			categoria = serviceCalculos.ValorCategoria(valorKilos);			
			calificacionesRenovaciones.setCategoria_actual(categoria);
			calificacionesRenovaciones.setCupo_kg_actual(valorKilos);
			this.serviceCalRen.modificar(calificacionesRenovaciones);			
		}
	}

	//////// LISTAR PRESENTACION
	public void cargarListaPresentacion() throws Exception {
		this.listaPresentacion = servicePresentacion.listar();
	}

	//////// LISTAR TIPO CAMBIOS
	public void cargarListaTipoCambio() throws Exception {
		this.listaTipoCambio = serviceTipoCambio.listar();
	}
	
	//LISTAR ACTIVIDADES POR SUSTANCIAS
	public void cargarListaSustanciasActividades(Integer id_calrensustancias) throws Exception {
		this.listaSustanciasActividades = serviceSusActividades.listaActividadesIdCalRenSus(id_calrensustancias);
	}

	//////VALIDACION SI REQUIERE REPRESENTANTE TECNICO
	public void cargarListaSustanciasReqRT(Integer id_calren) {
		if (id_calren != null) {
			this.listaSustanciasReqRT = serviceSustancias.listarSustanciasCalrenReqRT(id_calren);
			
		} else {
			listaSustanciasReqRT = null;
		}
	}
	
	public String requiereRT(Integer id_calren) {
		if (calren!=null) {
			valorKilos = serviceCalculos.ValorKilosEntidad(id_calren);
			valorKilos = Conversiones.formatearDecimales(valorKilos, 4);
			categoria = serviceCalculos.ValorCategoria(valorKilos);	
			this.listaSustanciasReqRT = serviceSustancias.listarSustanciasCalrenReqRT(id_calren);
			if (categoria==1) {
				mensajeReqRT ="No requiere Representante Técnico";
			}else if (categoria>1 && listaSustanciasReqRT.size()>0 ) {
				mensajeReqRT ="Requiere Representante Técnico";
			}else if (categoria>2) {
				mensajeReqRT ="Requiere Representante Técnico";

			}
		}else {
			mensajeReqRT ="Sin informacion de categoria y sustancias";
		}
		return mensajeReqRT;
	}
	
}
