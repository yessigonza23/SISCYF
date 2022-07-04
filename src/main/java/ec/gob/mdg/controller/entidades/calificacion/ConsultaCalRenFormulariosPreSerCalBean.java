package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacion;
import ec.gob.mdg.control.ejb.modelo.CalrenIntervenciones;
import ec.gob.mdg.control.ejb.modelo.CalrenIntervencionesComponentes;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividades;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenIntervencionesComponentesService;
import ec.gob.mdg.control.ejb.service.ICalrenIntervencionesService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class ConsultaCalRenFormulariosPreSerCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenSustanciasActividadesService serviceCalRenSusAct;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalRenActCal;

	@Inject
	private ICalrenIntervencionesService serviceCalRenIntervenciones;

	@Inject
	private ICalrenIntervencionesComponentesService serviceCalRenIntComponentes;

	private List<CalrenSustancias> listaCalRenSustancias = new ArrayList<>();
	private List<CalrenSustanciasActividades> listaCalRenSustanciasAct = new ArrayList<>();
	private List<CalrenIntervenciones> listaCalrenIntervenciones = new ArrayList<>();
	private List<CalrenIntervencionesComponentes> listaCalrenIntComponentes = new ArrayList<>();

	private Empresa empresa = new Empresa();
	private CalificacionesRenovaciones calRen = new CalificacionesRenovaciones();
	private CalrenSustanciasActividades calrenSustanciasActividades = new CalrenSustanciasActividades();
	private CalrenActividadesCalificacion calRenActCal = new CalrenActividadesCalificacion();
	private CalrenIntervenciones calrenIntervenciones = new CalrenIntervenciones();

	String calrenactS;
	Integer calrenactId;
	String abreviatura;
	String actividad;
	String calrenS;
	Integer calrenId;
	Double total_porcentaje = 0.0;;

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
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
	}


	public void cargarDatos() {
		abreviatura = "PS";
		calrenactS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
		calrenactId = Integer.parseInt(calrenactS);

		if (calrenactId != null) {
			calRenActCal = serviceCalRenActCal.listaCalrenActividadesId(calrenactId);

			if (calRenActCal != null) {
				calRen = serviceCalRen.calrenPorId(calRenActCal.getCalificacionesRenovaciones().getId());

				if (calRen != null) {
					listaCalRenSustanciasAct = serviceCalRenSusAct.listaSustActiPorAbreviatura(calRen.getId(),
							abreviatura);
					empresa = serviceEmpresa.listarEmpresaPorId(calRen.getEmpresa().getId());

					if (listaCalRenSustanciasAct != null && !listaCalRenSustanciasAct.isEmpty()) {
						calrenSustanciasActividades = listaCalRenSustanciasAct.get(0);
						if (calrenSustanciasActividades != null) {
							cargarListaIntervenciones(calRen.getId());
							if (listaCalrenIntervenciones != null && !listaCalrenIntervenciones.isEmpty()) {
								calrenIntervenciones = listaCalrenIntervenciones.get(0);
								if (calrenIntervenciones != null) {
									cargarListaIntComponentes(calrenIntervenciones.getId());
								}
							}

						}
					}
				}
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos", "No puede continuar"));
		}
	}

	public void cargarListaIntervenciones(Integer id_calren) {
		String tipo="P";
		if (id_calren != null) {
			listaCalrenIntervenciones = serviceCalRenIntervenciones.listaPorIdCalren(id_calren,tipo);
			
		}
	}

	public void cargarListaIntComponentes(Integer id_intervenciones) {
		total_porcentaje = 0.0;
		if (id_intervenciones != null) {
			listaCalrenIntComponentes = serviceCalRenIntComponentes.listarIntervencionesComponentes(id_intervenciones);
			for (CalrenIntervencionesComponentes c : listaCalrenIntComponentes) {
				total_porcentaje = total_porcentaje + c.getPorcentaje();
			}
		}
	}

	public void onRowSelect(SelectEvent<CalrenIntervenciones> event) {
		cargarListaIntComponentes(((CalrenIntervenciones) event.getObject()).getId());
	}

	public void onRowUnselect(UnselectEvent<CalrenIntervenciones> event) {
		cargarListaIntComponentes(((CalrenIntervenciones) event.getObject()).getId());
	}

	/// Ir a Formularios actividades
	public void irFormularios() {
		if (calRen.getId() != null) {
			calrenS = String.valueOf(calRen.getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("calren", calrenS);
			Utilitario.irAPagina("/pg/cal/calrenformulariosactcal");
		}
	}
}
