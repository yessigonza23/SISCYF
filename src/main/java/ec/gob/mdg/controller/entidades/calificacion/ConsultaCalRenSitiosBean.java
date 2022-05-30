package ec.gob.mdg.controller.entidades.calificacion;

import java.io.IOException;
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
	public void cargarDatos() {
		calrenS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
		calrenId = Integer.parseInt(calrenS);
		if (calrenId != null) {
			calren = serviceCalRen.calrenPorId(calrenId);
			empresa = serviceEmpresa.listarEmpresaPorId(calren.getEmpresa().getId());
		
			cargarListaSitios(calren);
		} else {
			empresa = null;
		}
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

	Boolean render = false;
	Boolean render_pe = false;
	Boolean render_mv = false;
	Boolean render_oi = false;

	public void onRowSelectSitio(SelectEvent<Sitios> event) throws Exception {
		if (((Sitios) event.getObject()).getId() != null) {
			mostrarData(((Sitios) event.getObject()));			
			cargarListaPesaje(((Sitios) event.getObject()).getId());
			cargarListaMedicionVolumetrica(((Sitios) event.getObject()).getId());
			cargarListaOtrosInstrumentos(((Sitios) event.getObject()).getId());
			if (sitios!=null) {
				render = true;
			}
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

	// LISTAR ACTIVIDADES POR SITIOS
	public void cargarListaSitiosActividades(Integer id_sitios) throws Exception {
		if (id_sitios != null) {
			this.listaSitiosActividades = serviceSitiosActividades.listaSitiosActividadesId(id_sitios);
		}

	}

	public void onRowUnselectSitio(UnselectEvent<Sitios> event) {
		if (((Sitios) event.getObject()).getId() != null) {
			mostrarData(((Sitios) event.getObject()));
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

	public void cargarListaPesaje(Integer id_sitios) {
		if (id_sitios != null) {
			listaPesaje = servicePesaje.listaPesajeIdSitio(id_sitios);
		}
	}

	public void cargarListaMedicionVolumetrica(Integer id_sitios) {
		if (id_sitios != null) {
			listaMedicionVolumetrica = serviceMedicionVolumetrica.listaMedicionVolumetricaIdSitio(id_sitios);
		}
	}

	public void cargarListaOtrosInstrumentos(Integer id_sitios) {
		if (id_sitios != null) {
			listaOtrosInstrumentosMedicion = serviceOtrosInstrumentosMedicion
					.listaOtrosInstrumentosMedicionIdSitio(id_sitios);
		}
	}

	public static void verPDF(String nombre_ruta) throws IOException {
		if (nombre_ruta != null) {
			UtilsArchivos.verPDF(nombre_ruta);
		}

	}
	
	//inicializar render
	public void limpiar() {
		render=false;
		render_pe = false;
		render_mv = false;
		render_oi = false;
	}

	/// Ir a Calificaciones
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
