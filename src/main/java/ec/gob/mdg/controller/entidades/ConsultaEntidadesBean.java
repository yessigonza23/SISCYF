package ec.gob.mdg.controller.entidades;

import java.io.IOException;
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
import ec.gob.mdg.control.ejb.modelo.CalrenConductores;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividades;
import ec.gob.mdg.control.ejb.modelo.CalrenTipoSustancia;
import ec.gob.mdg.control.ejb.modelo.CalrenVehiculos;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.MedicionVolumetrica;
import ec.gob.mdg.control.ejb.modelo.OtrosInstrumentosMedicion;
import ec.gob.mdg.control.ejb.modelo.Pesaje;
import ec.gob.mdg.control.ejb.modelo.Presentacion;
import ec.gob.mdg.control.ejb.modelo.Sitios;
import ec.gob.mdg.control.ejb.modelo.SitiosActividades;
import ec.gob.mdg.control.ejb.modelo.TipoCambio;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenConductoresService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasService;
import ec.gob.mdg.control.ejb.service.ICalrenTipoSustanciasService;
import ec.gob.mdg.control.ejb.service.ICalrenVehiculosService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.service.IMedicionVolumetricaService;
import ec.gob.mdg.control.ejb.service.IOtrosInstrumentosMedicionService;
import ec.gob.mdg.control.ejb.service.IPesajeService;
import ec.gob.mdg.control.ejb.service.IPresentacionService;
import ec.gob.mdg.control.ejb.service.ISitiosActividadesService;
import ec.gob.mdg.control.ejb.service.ISitiosService;
import ec.gob.mdg.control.ejb.service.ITipoCambioService;
import ec.gob.mdg.control.ejb.utils.Conversiones;
import ec.gob.mdg.control.funciones.CalculosCalRen;
import ec.gob.mdg.utils.UtilsArchivos;
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

	@Inject
	private IPresentacionService servicePresentacion;

	@Inject
	private ITipoCambioService serviceTipoCambio;

	@Inject
	private CalculosCalRen serviceCalculos;

	@Inject
	private ICalrenSustanciasActividadesService serviceSusActividades;

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

	@Inject
	private ICalrenVehiculosService serviceCalrenVehiculos;

	@Inject
	private ICalrenConductoresService serviceCalrenConductores;

	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private List<CalificacionesRenovaciones> listaCalRenovaciones1 = new ArrayList<>();
	private List<CalrenSustancias> listaSustancias = new ArrayList<>();
	private List<CalrenSustancias> listaSustanciasReqRT = new ArrayList<>();
	private List<CalrenActividadesCalificacion> listaCalrenActividadesCalificacion = new ArrayList<>();
	private List<CalrenTipoSustancia> listaCalrenTipoSustancia = new ArrayList<>();
	private List<Presentacion> listaPresentacion = new ArrayList<>();
	private List<TipoCambio> listaTipoCambio = new ArrayList<>();
	private List<CalrenSustanciasActividades> listaSustanciasActividades = new ArrayList<>();
	private List<Sitios> listaSitios = new ArrayList<>();
	private List<SitiosActividades> listaSitiosActividades = new ArrayList<>();
	private List<Pesaje> listaPesaje = new ArrayList<>();
	private List<MedicionVolumetrica> listaMedicionVolumetrica = new ArrayList<>();
	private List<OtrosInstrumentosMedicion> listaOtrosInstrumentosMedicion = new ArrayList<>();
	private List<CalrenVehiculos> listaCalrenVehiculos = new ArrayList<>();
	private List<CalrenConductores> listaCalrenConductores = new ArrayList<>();

	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	private CalificacionesRenovaciones calren = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private CalrenSustancias calrenSustancias = new CalrenSustancias();
	private CalrenActividadesCalificacion calrenActividadesCalificacion = new CalrenActividadesCalificacion();
	private CalrenTipoSustancia calrenTipoSustancia = new CalrenTipoSustancia();
	private CalrenSustanciasActividades calrenSustanciasActividades = new CalrenSustanciasActividades();
	private Sitios sitios = new Sitios();
	private SitiosActividades sitiosActividades = new SitiosActividades();
	private Pesaje pesaje = new Pesaje();
	private MedicionVolumetrica medicionVolumetrica = new MedicionVolumetrica();
	private OtrosInstrumentosMedicion otrosInstrumentosMedicion = new OtrosInstrumentosMedicion();
	private CalrenVehiculos calrenVehiculos = new CalrenVehiculos();
	private CalrenConductores calrenConductores = new CalrenConductores();

	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;
	Boolean render_sus = false;
	Boolean render_tra = false;
	Integer categoria = 0;
	Double valorKilos;
	String mensajeReqRT;

	String empresaS;
	Integer empresaId;

	@PostConstruct
	public void init() {
		try {
			cargarDatos();
			cargarListaPresentacion();
			cargarListaTipoCambio();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public Empresa cargarDatos() {
		if (empresa != null) {
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
				// CARGA DATOS DE CALIFICACIONES Y RENOVACIONES
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

			this.listaCalRenovaciones = serviceCalRen.listarCalRenPorEmpresa(empr);
			if (listaCalRenovaciones != null && !listaCalRenovaciones.isEmpty()) {
				calificacionesRenovaciones = listaCalRenovaciones.get(0);
				listaSustancias = null;
				if (calificacionesRenovaciones != null) {
					cargarListaCalRen(calificacionesRenovaciones.getId());
					cargarListaActividades(calificacionesRenovaciones.getId());
					cargarListaTipoSustancia(calificacionesRenovaciones.getId());
					cargarListaSitios(calificacionesRenovaciones);
					System.out.println("terminad e cargar listas");

					for (CalrenActividadesCalificacion c : listaCalrenActividadesCalificacion) {
						if (c.getActividadCalificacion().getSi_sustancia() == false) {
							cargarListaVehiculos(calificacionesRenovaciones.getId());
							cargarListaConductores(calificacionesRenovaciones.getId());
							render_tra = true;
						}
					}
				}
			}
		} else {
			listaCalRenovaciones = null;
			calificacionesRenovaciones = null;
			listaSustancias = null;
			listaCalrenActividadesCalificacion = null;
			listaCalrenTipoSustancia = null;
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

	//// Selecciona Calificaciones renovaciones
	public void onRowSelect(SelectEvent event) {
		listaSustancias = null;
		cargarListaCalRen(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaSitios(((CalificacionesRenovaciones) event.getObject()));
		for (CalrenActividadesCalificacion c : listaCalrenActividadesCalificacion) {
			if (c.getActividadCalificacion().getSi_sustancia() == false) {
				cargarListaVehiculos(calificacionesRenovaciones.getId());
				cargarListaConductores(calificacionesRenovaciones.getId());
			}
		}
	}

	public void onRowUnselect(UnselectEvent event) {
		cargarListaCalRen(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaActividades(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaTipoSustancia(((CalificacionesRenovaciones) event.getObject()).getId());
		cargarListaSitios(((CalificacionesRenovaciones) event.getObject()));
		calrenTipoSustancia = null;
		for (CalrenActividadesCalificacion c : listaCalrenActividadesCalificacion) {
			if (c.getActividadCalificacion().getSi_sustancia() == false) {
				cargarListaVehiculos(calificacionesRenovaciones.getId());
				cargarListaConductores(calificacionesRenovaciones.getId());
			}
		}
	}

	public void onRowCancel(RowEditEvent event) {
		FacesMessage msg = new FacesMessage("Cancelar",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	////////////////////////////////////// CALIFICACIONES RENOVACIONES
	public void cargarListaCalRen(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalRenovaciones1 = serviceCalRen.listarCalRenPorId(id_calren);
		} else {
			listaCalRenovaciones1 = null;
		}
	}

	////////////////////////////////////// ACTIVIDADES
	public void cargarListaActividades(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenActividadesCalificacion = serviceCalrenActividades.listaCalrenActividades(id_calren);
		} else {
			listaCalrenActividadesCalificacion = null;
		}
	}

	////////////////////////////////////// SITIOS
	public void cargarListaSitios(CalificacionesRenovaciones calren) {
		if (calren != null) {
			listaSitios = serviceSitios.listarSitiosCalren(calificacionesRenovaciones);
		} else {
			listaSitios = null;
		}
	}

	public void mostrarData(Sitios i) {
		this.sitios = i;
	}

	//////////////////////////////////////// TIPO DE SUSTANCIA
	public void cargarListaTipoSustancia(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenTipoSustancia = serviceCalrenTipoSustancia.listaCalrenTipoSustancias(id_calren);
			if (listaCalrenTipoSustancia != null && !listaCalrenTipoSustancia.isEmpty()) {
				calrenTipoSustancia = listaCalrenTipoSustancia.get(0);
				if (calrenTipoSustancia != null) {
					cargarListaSustancias(calificacionesRenovaciones.getId(),
							calrenTipoSustancia.getTipoSustancia().getId());
					requiereRT(calificacionesRenovaciones.getId());
				}
			}
		} else {
			listaCalrenTipoSustancia = null;
		}
	}

	public void onRowSelectTipo(SelectEvent event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getCalificacionesRenovaciones().getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
	}

	public void onRowUnselectTipo(UnselectEvent event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getCalificacionesRenovaciones().getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
	}

	///////////////////////////////////// SUSTANCIAS
	public void cargarListaSustancias(Integer id_calren, Integer id_tiposus) {
		if (id_calren != null) {
			this.listaSustancias = serviceSustancias.listarSustanciasEmpCalren(id_calren, id_tiposus);

		} else {
			listaSustancias = null;
		}
	}

	//// Grabar cupo y estado
	public Integer operarSustancias(CalrenSustancias calrenSus) {
		try {
			if (calrenSus != null) {
				calren = serviceCalRen.calrenPorId(calrenSus.getCalificacionesRenovaciones().getId());
				if (calren.getAprobado().equals("N")) {
					calrenSus.setEstado(calrenSustancias.getEstado());
					calrenSus.setTipoCambio(calrenSustancias.getTipoCambio());
					calrenSus.setCupo_asignado(calrenSustancias.getCupo_asignado());
					calrenSus.setPresentacion(calrenSustancias.getPresentacion());
					this.serviceSustancias.modificar(calrenSus);
					actualizaCalren(calrenSus.getCalificacionesRenovaciones().getId());
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio Exitoso", "Actualización completa"));
				} else if (calren.getAprobado().equals("S")) {
					calrenSus.setEstado(calrenSustancias.getEstado());
					calrenSus.setTipoCambio(calrenSustancias.getTipoCambio());
					this.serviceSustancias.modificar(calrenSus);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
							"Cambio de estado y tipo de cambio exitoso", "Actualización Realizada"));
				}

			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return 1;
	}

	public void actualizaCalren(Integer calren) throws Exception {
		if (calren != null) {
			valorKilos = serviceCalculos.ValorKilosEntidad(calren);
			valorKilos = Conversiones.formatearDecimales(valorKilos, 4);
			categoria = serviceCalculos.ValorCategoria(valorKilos);
			calificacionesRenovaciones.setCategoria_actual(categoria);
			calificacionesRenovaciones.setCupo_kg_actual(valorKilos);
			this.serviceCalRen.modificar(calificacionesRenovaciones);
		}
	}

	// LISTAR ACTIVIDADES POR SUSTANCIAS
	public void cargarListaSustanciasActividades(Integer id_calrensustancias) throws Exception {
		if (id_calrensustancias != null) {
			this.listaSustanciasActividades = serviceSusActividades.listaActividadesIdCalRenSus(id_calrensustancias);
		}
	}

	// LISTAR ACTIVIDADES POR SITIOS
	public void cargarListaSitiosActividades(Integer id_sitios) throws Exception {
		// System.out.println("entra a sitios actividades");
		if (id_sitios != null) {
			// System.out.println("entra diferente de null " + id_sitios);
			this.listaSitiosActividades = serviceSitiosActividades.listaSitiosActividadesId(id_sitios);
			// System.out.println("imprime lista " + listaSitiosActividades);
		}

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
		System.out.println("entra a pesaje " + id_sitios);
		if (id_sitios != null) {
			listaPesaje = servicePesaje.listaPesajeIdSitio(id_sitios);
			System.out.println("carga pesaje " + listaPesaje);
		}
	}

	public void cargarListaMedicionVolumetrica(Integer id_sitios) {
		System.out.println("entra a medicion " + id_sitios);
		if (id_sitios != null) {
			listaMedicionVolumetrica = serviceMedicionVolumetrica.listaMedicionVolumetricaIdSitio(id_sitios);
			System.out.println("carga medición " + listaMedicionVolumetrica);
		}
	}

	public void cargarListaOtrosInstrumentos(Integer id_sitios) {
		System.out.println("entra a otros" + id_sitios);
		if (id_sitios != null) {
			listaOtrosInstrumentosMedicion = serviceOtrosInstrumentosMedicion
					.listaOtrosInstrumentosMedicionIdSitio(id_sitios);
			System.out.println("carga otros " + listaOtrosInstrumentosMedicion);

		}
	}

	/////////////// TRANSPORTE
	
	////////////// CARGAR VEHICULOS

	public void cargarListaVehiculos(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenVehiculos = serviceCalrenVehiculos.listarCalrenVehiculosPorIdCalren(id_calren);
		}
	}
	
	public void mostrarDataVehi(CalrenVehiculos i) {
		this.calrenVehiculos = i;
	}
	///////////// CARGAR CONDUCTORES

	public void cargarListaConductores(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenConductores = serviceCalrenConductores.listarCalrenConductoresPorIdCalren(id_calren);
		}
	}

	public void mostrarDataCond(CalrenConductores i) {
		this.calrenConductores = i;
	}
	//////// LISTAR PRESENTACION
	public void cargarListaPresentacion() throws Exception {
		this.listaPresentacion = servicePresentacion.listar();
	}

	//////// LISTAR TIPO CAMBIOS
	public void cargarListaTipoCambio() throws Exception {
		this.listaTipoCambio = serviceTipoCambio.listar();
	}

	////// VALIDACION SI REQUIERE REPRESENTANTE TECNICO
	public void cargarListaSustanciasReqRT(Integer id_calren) {
		if (id_calren != null) {
			this.listaSustanciasReqRT = serviceSustancias.listarSustanciasCalrenReqRT(id_calren);

		} else {
			listaSustanciasReqRT = null;
		}
	}

	public String requiereRT(Integer id_calren) {
		if (calren != null) {
			valorKilos = serviceCalculos.ValorKilosEntidad(id_calren);
			valorKilos = Conversiones.formatearDecimales(valorKilos, 4);
			categoria = serviceCalculos.ValorCategoria(valorKilos);
			this.listaSustanciasReqRT = serviceSustancias.listarSustanciasCalrenReqRT(id_calren);
			if (categoria == 1) {
				mensajeReqRT = "No requiere Representante Técnico";
			} else if (categoria > 1 && listaSustanciasReqRT.size() > 0) {
				mensajeReqRT = "Requiere Representante Técnico";
			} else if (categoria > 2) {
				mensajeReqRT = "Requiere Representante Técnico";

			}
		} else {
			mensajeReqRT = "Sin informacion de categoria y sustancias";
		}
		return mensajeReqRT;
	}

	public static void verPDF(String nombre_ruta) throws IOException {
		// System.out.println("nombre ruta " + nombre_ruta);
		if (nombre_ruta != null) {
			UtilsArchivos.verPDF(nombre_ruta);
		}

	}

}
