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
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalRenEnt1Bean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalificacionesRenovaciones> listaCalRenovaciones1 = new ArrayList<>();

	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private CalificacionesRenovaciones calren = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();

	String empresaS;
	Integer empresaId;

	@PostConstruct
	public void init() {
		try {
			cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		if (empresa != null) {
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
			empresaId = Integer.parseInt(empresaS);
			if (empresaId != null) {
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
				cargarListaCalRen(empresa);
			}
		}
	}

	//////// CALIFICACIONES RENOVACIONES -
	public List<CalificacionesRenovaciones> cargarListaCalRen(Empresa empr) {
		if (empr != null) {
			this.listaCalRenovaciones = serviceCalRen.listarCalRenPorEmpresa(empr);
			if (listaCalRenovaciones != null && !listaCalRenovaciones.isEmpty()) {
				calificacionesRenovaciones = listaCalRenovaciones.get(0);
				if (calificacionesRenovaciones != null) {
					cargarListaCalRen(calificacionesRenovaciones.getId());
				}
			}
		} else {
			listaCalRenovaciones = null;
			calificacionesRenovaciones = null;
		}
		return listaCalRenovaciones;
	}

	//// Grabar observaciones para informe de calificacion
	public Integer operar(CalificacionesRenovaciones calren) {
		try {
			if (calren != null && calren.getAprobado().equals("N")) {
				calren.setObservacion(calificacionesRenovaciones.getObservacion());
				this.serviceCalRen.modificar(calren);
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"Se ha grabado la observación", "Actualización Realizada"));
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No hay datos o No puede realizar cambios", "Calificación/Renovación Aprobada"));
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}

	////////////////////////////////////// CALIFICACIONES RENOVACIONES
	public void cargarListaCalRen(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalRenovaciones1 = serviceCalRen.listarCalRenPorId(id_calren);
		} else {
			listaCalRenovaciones1 = null;
		}
	}
	
////Selecciona Calificaciones renovaciones
	public void onRowSelect(SelectEvent event) {		
		cargarListaCalRen(((CalificacionesRenovaciones) event.getObject()).getId());		
	}

	public void onRowUnselect(UnselectEvent event) {
		cargarListaCalRen(((CalificacionesRenovaciones) event.getObject()).getId());		
	}
}
