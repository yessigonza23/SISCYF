package ec.gob.mdg.controller.entidades.calificacion;

import java.io.IOException;
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

import org.primefaces.PrimeFaces;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacion;
import ec.gob.mdg.control.ejb.modelo.CalrenConductores;
import ec.gob.mdg.control.ejb.modelo.CalrenVehiculos;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.EmpresaRepresentantes;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenConductoresService;
import ec.gob.mdg.control.ejb.service.ICalrenVehiculosService;
import ec.gob.mdg.control.ejb.service.IEmpresaRepresentantesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.utils.UtilsArchivos;
import lombok.Data;

@Data
@Named
@SessionScoped
public class ConsultaCalRenFormulariosTraCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalRenActCal;

	@Inject
	private ICalrenVehiculosService serviceCalRenVehiculos;

	@Inject
	private ICalrenConductoresService serviceCalRenConductores;

	@Inject
	private IEmpresaRepresentantesService serviceEmpresaRepresentantes;

	private List<CalrenVehiculos> listaCalRenVehiculos = new ArrayList<>();
	private List<CalrenConductores> listaCalRenConductores = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantes = new ArrayList<>();

	private Empresa empresa = new Empresa();
	private CalificacionesRenovaciones calRen = new CalificacionesRenovaciones();
	private CalrenActividadesCalificacion calRenActCal = new CalrenActividadesCalificacion();
	private CalrenVehiculos calrenVehiculos = new CalrenVehiculos();
	private CalrenConductores calrenConductores = new CalrenConductores();
	private EmpresaRepresentantes empresaRepresentantes = new EmpresaRepresentantes();

	String calrenS;
	Integer calrenId;
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
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		calrenactS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
		calrenactId = Integer.parseInt(calrenactS);
		if (calrenactId != null) {
			calRenActCal = serviceCalRenActCal.listaCalrenActividadesId(calrenactId);
			if (calRenActCal != null) {				
				calRen = serviceCalRen.calrenPorId(calRenActCal.getCalificacionesRenovaciones().getId());
				if (calRen != null) {
					listaCalRenVehiculos = this.serviceCalRenVehiculos.listarCalrenVehiculosPorIdCalren(calRen.getId());
					listaCalRenConductores = this.serviceCalRenConductores
							.listarCalrenConductoresPorIdCalren(calRen.getId());					
					empresa = serviceEmpresa.listarEmpresaPorId(calRen.getEmpresa().getId());
					listaEmpresaRepresentantes = serviceEmpresaRepresentantes.listarRepLogisticos(empresa);
				} else {
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos", "No puede continuar"));
				}
			}
		}
	}

	public void mostrarData(EmpresaRepresentantes i) {
		this.empresaRepresentantes = i;
	}
	
	public void mostrarDataVehiculos(CalrenVehiculos i) {
		this.calrenVehiculos = i;	
		if (calrenVehiculos.getTipo_sustancia().equals("S")) {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('wdgsus').show();");		
		}else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Unicamente Sales", "No hay datos");
			PrimeFaces.current().dialog().showMessageDynamic(message);		
		}
	}
	
	public void mostrarDataVehiculosOrigen(CalrenVehiculos i) {
		this.calrenVehiculos = i;	
		if (calrenVehiculos.getVehiculos().getOrigen().equals("N")) {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('wdgpo').show();");		
		}else {
			PrimeFaces current = PrimeFaces.current();
			current.executeScript("PF('wdgci').show();");		
		}
	}

	public static void verPDF(String nombre_ruta) throws IOException {
		if (nombre_ruta != null) {
			UtilsArchivos.verPDF(nombre_ruta);
		}
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
