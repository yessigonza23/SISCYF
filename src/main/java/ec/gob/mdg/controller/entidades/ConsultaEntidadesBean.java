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

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasService;
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

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalrenSustancias> listaSustancias = new ArrayList<>();
	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private CalrenSustancias calrenSustancias= new CalrenSustancias();

	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;

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
		if (empr != null) {
			System.out.println("entra a cargar datos de calren");
			this.listaCalRenovaciones = serviceCalRen.listarCalRenPorEmpresa(empr);
		} else {
			listaCalRenovaciones = null;
		}
		return listaCalRenovaciones;
	}

	//// GRABAR OBSERVACIONES
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
		FacesMessage msg = new FacesMessage("Calificacion/Renovacion: ",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	public void onRowUnselect(UnselectEvent event) {
		FacesMessage msg = new FacesMessage("Calificacion/Renovacion quitar seleccion",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	//////// SUSTANCIAS
	public List<CalificacionesRenovaciones> cargarListaSustancias(Empresa empr, CalificacionesRenovaciones calren) {
		if (empr != null && calren != null) {
			System.out.println("entra a cargar sustancias");
			this.listaSustancias = serviceSustancias.listarSustanciasEmpCalren(empr, calren);
		} else {
			listaCalRenovaciones = null;
		}
		return listaCalRenovaciones;
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
