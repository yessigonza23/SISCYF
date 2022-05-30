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
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacionClientes;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividades;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividadesPresentacion;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionClientesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesPresentacionService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class ConsultaCalRenFormulariosComDisCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalRenActCal;

	@Inject
	private ICalrenSustanciasActividadesService serviceCalRenSusAct;

	@Inject
	private ICalrenActividadesCalificacionClientesService serviceCalRenActCalClientes;

	@Inject
	private ICalrenSustanciasActividadesPresentacionService serviceCalrenSusActPresentacion;

	private List<CalrenSustancias> listaCalRenSustancias = new ArrayList<>();
	private List<CalrenSustanciasActividades> listaCalRenSustanciasAct = new ArrayList<>();
	private List<CalrenActividadesCalificacionClientes> listaCalRenActCalClientes = new ArrayList<>();
	private List<CalrenSustanciasActividadesPresentacion> listaCalRenSusActPresentacion = new ArrayList<>();

	private Empresa empresa = new Empresa();
	private CalificacionesRenovaciones calRen = new CalificacionesRenovaciones();
	private CalrenSustanciasActividades calrenSustanciasActividades = new CalrenSustanciasActividades();
	private CalrenActividadesCalificacion calRenActCal = new CalrenActividadesCalificacion();
	private CalrenActividadesCalificacionClientes calrenActCalClientes = new CalrenActividadesCalificacionClientes();
	private CalrenSustanciasActividadesPresentacion calRenSusActPresentacion = new CalrenSustanciasActividadesPresentacion();

	String calrenactS;
	Integer calrenactId;
	String abreviatura;
	String calrenS;
	Integer calrenId;

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

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		abreviatura = "C/D";
		calrenactS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
		calrenactId = Integer.parseInt(calrenactS);
		if (calrenactId != null) {
			calRenActCal = serviceCalRenActCal.listaCalrenActividadesId(calrenactId);
			cargarClientes(calRenActCal.getId());
			if (calRenActCal != null) {
				calRen = serviceCalRen.calrenPorId(calRenActCal.getCalificacionesRenovaciones().getId());
				
				if (calRen != null) {
					listaCalRenSustanciasAct = serviceCalRenSusAct.listaSustActiPorAbreviatura(calRen.getId(),
							abreviatura);
					empresa = serviceEmpresa.listarEmpresaPorId(calRen.getEmpresa().getId());
					if (listaCalRenSustanciasAct != null && !listaCalRenSustanciasAct.isEmpty()) {
						calrenSustanciasActividades = listaCalRenSustanciasAct.get(0);
						if (calrenSustanciasActividades != null) {
							System.out.println("id calrenact :"+calrenSustanciasActividades.getId());
							cargarPresentaciones(calrenSustanciasActividades.getId());							
						}
					}
				}
			}

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos", "No puede continuar"));
		}
	}

	public void cargarPresentaciones(Integer id_CalRenSusAct) {
		if (id_CalRenSusAct!=null) {
			listaCalRenSusActPresentacion = serviceCalrenSusActPresentacion
					.listaCalrenActividadesPresentacion(id_CalRenSusAct);
		}
		
	}

	public void cargarClientes(Integer id_CalRenSusAct) {
		if (id_CalRenSusAct!=null) {
			listaCalRenActCalClientes = serviceCalRenActCalClientes.listaCalrenActClientesIdCalrenAct(id_CalRenSusAct);			
		}		
	}

	public void onRowSelect(SelectEvent<CalrenSustanciasActividades> event) {
		cargarPresentaciones(((CalrenSustanciasActividades) event.getObject()).getId());
	}

	public void onRowUnselect(UnselectEvent<CalrenSustanciasActividades> event) {
		cargarPresentaciones(((CalrenSustanciasActividades) event.getObject()).getId());
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
