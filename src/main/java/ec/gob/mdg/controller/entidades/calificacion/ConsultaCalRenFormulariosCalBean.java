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

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacion;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalRenFormulariosCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalRenActCal;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalrenActividadesCalificacion> listaCalRenActCal = new ArrayList<>();

	private CalificacionesRenovaciones calren = new CalificacionesRenovaciones();
	private CalrenActividadesCalificacion calRenActCal = new CalrenActividadesCalificacion();
	private Empresa empresa = new Empresa();

	String calrenS;
	Integer calrenId;
	String actividad;
	String calrenactS;
	Integer calrenactId;

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
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		calrenS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
		calrenId = Integer.parseInt(calrenS);
		if (calrenId != null) {
			calren = serviceCalRen.calrenPorId(calrenId);
			empresa = serviceEmpresa.listarEmpresaPorId(calren.getEmpresa().getId());
			listaCalRenActCal = serviceCalRenActCal.listaCalrenActividades(calrenId);
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos", "No puede continuar"));
		}
	}

	public void onRowSelect(SelectEvent event) throws Exception {
	
		if (((CalrenActividadesCalificacion) event.getObject()) != null) {
			actividad = ((CalrenActividadesCalificacion) event.getObject()).getActividadCalificacion().getAbreviatura();
			calrenactS = String.valueOf(
					((CalrenActividadesCalificacion) event.getObject()).getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("calrenact", calrenactS);

			
			if (actividad.equals("I/E")) {
				
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalimpexp");
			} else if (actividad.equals("C/D")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalcomdis");
			} else if (actividad.equals("A")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalalm");
			} else if (actividad.equals("P")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalpro");
			} else if (actividad.equals("PS")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalpreser");
			} else if (actividad.equals("U")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcaluso");
			} else if (actividad.equals("T")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcaltra");
			} else if (actividad.equals("R")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalrec");
			} else if (actividad.equals("RU")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalreu");
			}
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		if (((CalrenActividadesCalificacion) event.getObject()) != null) {
			actividad = ((CalrenActividadesCalificacion) event.getObject()).getActividadCalificacion().getAbreviatura();
			calrenS = String.valueOf(
					((CalrenActividadesCalificacion) event.getObject()).getCalificacionesRenovaciones().getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("calren", calrenS);

			if (actividad.equals("I/E")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalimpexp");
			} else if (actividad.equals("C/D")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalcomdis");
			} else if (actividad.equals("A")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalalm");
			} else if (actividad.equals("P")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalpro");
			} else if (actividad.equals("PS")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalpreser");
			} else if (actividad.equals("U")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcaluso");
			} else if (actividad.equals("T")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcaltra");
			} else if (actividad.equals("R")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalrec");
			} else if (actividad.equals("RU")) {
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcalreu");
			}
		}
	}
	
	/// Ir a Sustancias
		public void cargarSustancias() {
			if (calren != null) {
				
				calrenS=String.valueOf(calren.getId());
				final FacesContext context = FacesContext.getCurrentInstance();
				final Flash flash = context.getExternalContext().getFlash();
				flash.put("calren", calrenS);
				Utilitario.irAPagina("/pg/cal/calrenprincipalcal");
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos no puede continuar", "Sin datos"));
			}
		}

}
