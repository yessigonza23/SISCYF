package ec.gob.mdg.controller.entidades.calificacion;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.MedicionVolumetrica;
import ec.gob.mdg.control.ejb.modelo.OtrosInstrumentosMedicion;
import ec.gob.mdg.control.ejb.modelo.Pesaje;
import ec.gob.mdg.control.ejb.modelo.Sitios;
import ec.gob.mdg.control.ejb.modelo.SitiosActividades;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.service.IMedicionVolumetricaService;
import ec.gob.mdg.control.ejb.service.IOtrosInstrumentosMedicionService;
import ec.gob.mdg.control.ejb.service.IPesajeService;
import ec.gob.mdg.control.ejb.service.ISitiosActividadesService;
import ec.gob.mdg.control.ejb.service.ISitiosService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.utils.UtilsArchivos;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalRenSitiosBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ISitiosService serviceSitios;

	@Inject
	private ISitiosActividadesService serviceSitiosActividades;

	@Inject
	private IPesajeService servicePesaje;

	@Inject
	private IMedicionVolumetricaService serviceMedicionVolumetrica;

	@Inject
	private IOtrosInstrumentosMedicionService serviceOtrosInstrumentosMedicion;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<Sitios> listaSitios = new ArrayList<>();
	private List<SitiosActividades> listaSitiosActividades = new ArrayList<>();
	private List<Pesaje> listaPesaje = new ArrayList<>();
	private List<MedicionVolumetrica> listaMedicionVolumetrica = new ArrayList<>();
	private List<OtrosInstrumentosMedicion> listaOtrosInstrumentosMedicion = new ArrayList<>();
	
	private CalificacionesRenovaciones calren = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private Sitios sitios = new Sitios();
	private SitiosActividades sitiosActividades = new SitiosActividades();
	private Pesaje pesaje = new Pesaje();
	private MedicionVolumetrica medicionVolumetrica = new MedicionVolumetrica();
	private OtrosInstrumentosMedicion otrosInstrumentosMedicion = new OtrosInstrumentosMedicion();

	String empresaS;
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
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public Empresa cargarDatos() {
		calrenS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
		calrenId = Integer.parseInt(calrenS);
		if (calrenId != null) {			
			calren = serviceCalRen.calrenPorId(calrenId);
			System.out.println("calren Sitios" + calren);
			empresa = serviceEmpresa.listarEmpresaPorId(calren.getEmpresa().getId());
			cargarListaSitios(calren);
		} else {
			empresa = null;
		}
		return empresa;
	}

	////////////////////////////////////// SITIOS
	public void cargarListaSitios(CalificacionesRenovaciones calren) {
		if (calren != null) {
			listaSitios = serviceSitios.listarSitiosCalren(calren);
		} else {
			listaSitios = null;
		}
	}

	public void mostrarData(Sitios i) {
		this.sitios = i;
	}
	///////////////////// Selecciona Sitio para Pesaje, medicion volumétrica y otros
	///////////////////// instrumentos

	Boolean render_pe = false;
	Boolean render_mv = false;
	Boolean render_oi = false;

	public void onRowSelectSitio(SelectEvent event) {
		System.out.println("entra a seleccionar ");
		if (((Sitios) event.getObject()).getId() != null) {
			cargarListaPesaje(((Sitios) event.getObject()).getId());
			cargarListaMedicionVolumetrica(((Sitios) event.getObject()).getId());
			cargarListaOtrosInstrumentos(((Sitios) event.getObject()).getId());
			if (listaPesaje != null) {
				render_pe = true;
			}
			if (listaMedicionVolumetrica != null) {
				render_mv = true;
			}
			if (listaOtrosInstrumentosMedicion != null) {
				render_oi = true;
			}
		}
	}

	public void onRowUnselectSitio(UnselectEvent event) {
		if (((Sitios) event.getObject()).getId() != null) {
			cargarListaPesaje(((Sitios) event.getObject()).getId());
			cargarListaMedicionVolumetrica(((Sitios) event.getObject()).getId());
			cargarListaOtrosInstrumentos(((Sitios) event.getObject()).getId());
		}
	}

	public void cargarListaPesaje(Integer id_sitios) {
	
		if (id_sitios != null) {
			listaPesaje = servicePesaje.listaPesajeIdSitio(id_sitios);
			System.out.println("carga pesaje " + listaPesaje);
		}
	}

	public void cargarListaMedicionVolumetrica(Integer id_sitios) {
		
		if (id_sitios != null) {
			listaMedicionVolumetrica = serviceMedicionVolumetrica.listaMedicionVolumetricaIdSitio(id_sitios);
			System.out.println("carga medición " + listaMedicionVolumetrica);
		}
	}

	public void cargarListaOtrosInstrumentos(Integer id_sitios) {		
		if (id_sitios != null) {
			listaOtrosInstrumentosMedicion = serviceOtrosInstrumentosMedicion
					.listaOtrosInstrumentosMedicionIdSitio(id_sitios);
		}
	}
	
	public static void verPDF(String nombre_ruta) throws IOException {
		// System.out.println("nombre ruta " + nombre_ruta);
		if (nombre_ruta != null) {
			UtilsArchivos.verPDF(nombre_ruta);
		}

	}
	
	/// Ir a Calificaciones
	public void irCalRen() {
		empresaS = String.valueOf(calren.getEmpresa().getId());
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", empresaS);
		Utilitario.irAPagina("/pg/cal/calrenconsultacal");
	}


}
