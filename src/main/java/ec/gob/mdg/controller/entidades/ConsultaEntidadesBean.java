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
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasService;
import ec.gob.mdg.control.ejb.service.ICalrenTipoSustanciasService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
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

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalrenSustancias> listaSustancias = new ArrayList<>();
	private List<CalrenActividadesCalificacion> listaCalrenActividadesCalificacion = new ArrayList<>();
	private List<CalrenTipoSustancia> listaCalrenTipoSustancia = new ArrayList<>();
	
	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private CalrenSustancias calrenSustancias= new CalrenSustancias();
	private CalrenActividadesCalificacion calrenActividadesCalificacion = new CalrenActividadesCalificacion();
	private CalrenTipoSustancia calrenTipoSustancia = new CalrenTipoSustancia();

	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;
	Boolean render_sus=false;

	String empresaS;
	Integer empresaId;

	@PostConstruct
	public void init() {
		cargarDatos();
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public Empresa cargarDatos() {
		if (empresa != null) {
			System.out.println("entra a cargar datos de empresa");
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
		if (empr != null ) {
			System.out.println("entra a cargar datos de calren");
			this.listaCalRenovaciones = serviceCalRen.listarCalRenPorEmpresa(empr);
			if (listaCalRenovaciones!=null && !listaCalRenovaciones.isEmpty()) {
				calificacionesRenovaciones=listaCalRenovaciones.get(0) ;//   listaCalRenovaciones.indexOf(0);;
				System.out.println("calificaciones primera posicion "+calificacionesRenovaciones.getId());
				
				if (calificacionesRenovaciones!=null) {					
					cargarListaActividades(calificacionesRenovaciones.getId());
					cargarListaTipoSustancia(calificacionesRenovaciones.getId());
					cargarListaSustancias(calificacionesRenovaciones.getId());
				}				
			}			
		} else {
			listaCalRenovaciones = null;
			calificacionesRenovaciones=null;
			listaSustancias=null;
			listaCalrenActividadesCalificacion=null;
			listaCalrenTipoSustancia=null;
		}
		return listaCalRenovaciones;
	}

	//// Grabar observaciones para informe de calificacion
	public Integer operar(CalificacionesRenovaciones calren) {
		System.out.println("entra a Accion : " + calificacionesRenovaciones.getObservacion() + "-" + calren.getId()
				+ "-" + calren.getAprobado() + "-" + calren.getObservacion());
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

	//////////////// SELECCIONAR LA CALIFICACION RENOVACIÓN

	public void onRowSelect(SelectEvent event) {		
		System.out.println("on row" );
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaSustancias(((CalificacionesRenovaciones) event.getObject()).getId());
		FacesMessage msg = new FacesMessage("CalificacionesRenovaciones", String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		System.out.println("deseon row" );
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaSustancias(((CalificacionesRenovaciones) event.getObject()).getId());
		FacesMessage msg = new FacesMessage("CalificacionesRenovaciones", String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
        FacesContext.getCurrentInstance().addMessage(null, msg);
	}
	
	 public void onRowCancel(RowEditEvent event) {
	        FacesMessage msg = new FacesMessage("Cancelar", String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
	        FacesContext.getCurrentInstance().addMessage(null, msg);
	    }
	
	/////Calificaciones por actividad
	public void cargarListaActividades(Integer id_calren) {
		System.out.println("antes del if actividades " + id_calren );
		if (id_calren != null) {
			this.listaCalrenActividadesCalificacion = serviceCalrenActividades.listaCalrenActividades(id_calren);
		} else {
			System.out.println("entra al else" );
			listaCalrenActividadesCalificacion = null;
		}
	}
	
	/////Calificaciones por tipo de sustancias
	public void cargarListaTipoSustancia(Integer id_calren) {
		System.out.println("antes del iftipo de sustancia " + id_calren );
		if (id_calren != null) {
			this.listaCalrenTipoSustancia = serviceCalrenTipoSustancia.listaCalrenTipoSustancias(id_calren);
		} else {
			System.out.println("entra al else" );
			listaCalrenTipoSustancia = null;
		}
	}
	
	//////// SUSTANCIAS
	public void cargarListaSustancias(Integer id_calren) {
		System.out.println("antes del carga lista sustancias " + id_calren );
		if (id_calren != null) {
			this.listaSustancias = serviceSustancias.listarSustanciasEmpCalren(id_calren);
		} else {
			System.out.println("entra al else" );
			listaSustancias = null;
		}
	}

    ////GRABAR RAZON Y ESTADO
	public Integer operarSustancias(CalrenSustancias calrenSus) {
		System.out.println("entra a Accion Sustancias : "); 
		try {
			if (calrenSus != null) {
				System.out.println("entra para modificar calren");
				calrenSus.setEstado(calrenSustancias.getEstado());
				calrenSus.setTipo_cambio(calrenSustancias.getTipo_cambio());
				calrenSus.setCupo_asignado(calrenSustancias.getCupo_asignado());
				this.serviceSustancias.modificar(calrenSus);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}

	////////

}
