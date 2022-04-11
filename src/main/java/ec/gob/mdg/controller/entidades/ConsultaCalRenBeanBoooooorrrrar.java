package ec.gob.mdg.controller.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

@Named
@ViewScoped
public class ConsultaCalRenBeanBoooooorrrrar implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();

	String empresaS;
	Integer empresaId;

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	public Empresa getEmpresa() {
		return empresa;
	}

	///////// LISTAR CALIFICACIONES RENOVACIONES
	public List<CalificacionesRenovaciones> getListaCalRenovaciones() { 
		empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
		
		System.out.println("getlistacalren empresasS "+ empresaS);
		if (empresaS !=null) {
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
			empresaId = Integer.parseInt(empresaS);
			empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
			listaCalRenovaciones = serviceCalRen.listarCalRenPorEmpresa(empresa);
		}else {
			listaCalRenovaciones = null;
		}
		return listaCalRenovaciones;
	}

	//////// CALIFICACIONES RENOVACIONES

	private String tipoDialog;

	public void operar(String accion) {
		System.out.println("entra en Acción: " + accion);
		try {
			if (accion.equalsIgnoreCase("M")) {
				mostrarData(calificacionesRenovaciones);
				calificacionesRenovaciones.setObservacion(calificacionesRenovaciones.getObservacion());
				this.serviceCalRen.modificar(this.calificacionesRenovaciones);
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void mostrarData(CalificacionesRenovaciones i) {
		System.out.println("entra a mostrar: " + i.getId());
		if (i!=null) {
			this.calificacionesRenovaciones = i;
			this.tipoDialog = "Modificar";
		}
	
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

	////////

	public List<Empresa> getListaEmpresas() throws Exception {
		return serviceEmpresa.listar();
	}

	public void setListaCalRenovaciones(List<CalificacionesRenovaciones> listaCalRenovaciones) {
		this.listaCalRenovaciones = listaCalRenovaciones;
	}

	public CalificacionesRenovaciones getCalificacionesRenovaciones() {
		return calificacionesRenovaciones;
	}

	public void setCalificacionesRenovaciones(CalificacionesRenovaciones calificacionesRenovaciones) {
		this.calificacionesRenovaciones = calificacionesRenovaciones;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}	

	public String getTipoDialog() {
		return tipoDialog;
	}

	public void setTipoDialog(String tipoDialog) {
		this.tipoDialog = tipoDialog;
	}

}
