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

import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.CalrenActividadesCalificacion;
import ec.gob.mdg.control.ejb.modelo.CalrenSustancias;
import ec.gob.mdg.control.ejb.modelo.CalrenSustanciasActividades;
import ec.gob.mdg.control.ejb.modelo.CalrenTipoSustancia;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.Presentacion;
import ec.gob.mdg.control.ejb.modelo.TipoCambio;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasActividadesService;
import ec.gob.mdg.control.ejb.service.ICalrenSustanciasService;
import ec.gob.mdg.control.ejb.service.ICalrenTipoSustanciasService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.service.IPresentacionService;
import ec.gob.mdg.control.ejb.service.ITipoCambioService;
import ec.gob.mdg.control.ejb.utils.Conversiones;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.control.funciones.CalculosCalRen;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalRenTipoSustBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalrenActividades;

	@Inject
	private ICalrenTipoSustanciasService serviceCalrenTipoSustancia;

	@Inject
	private ICalrenSustanciasService serviceSustancias;

	@Inject
	private IPresentacionService servicePresentacion;

	@Inject
	private ITipoCambioService serviceTipoCambio;

	@Inject
	private CalculosCalRen serviceCalculos;

	@Inject
	private ICalrenSustanciasActividadesService serviceSusActividades;

	private List<CalrenSustancias> listaSustancias = new ArrayList<>();
	private List<CalrenSustancias> listaSustanciasReqRT = new ArrayList<>();
	private List<CalrenActividadesCalificacion> listaCalrenActividadesCalificacion = new ArrayList<>();
	private List<CalrenTipoSustancia> listaCalrenTipoSustancia = new ArrayList<>();
	private List<Presentacion> listaPresentacion = new ArrayList<>();
	private List<TipoCambio> listaTipoCambio = new ArrayList<>();
	private List<CalrenSustanciasActividades> listaSustanciasActividades = new ArrayList<>();

	private CalificacionesRenovaciones calRen = new CalificacionesRenovaciones();
	private Empresa empresa = new Empresa();
	private CalrenSustancias calrenSustancias = new CalrenSustancias();
	private CalrenActividadesCalificacion calrenActividadesCalificacion = new CalrenActividadesCalificacion();
	private CalrenTipoSustancia calrenTipoSustancia = new CalrenTipoSustancia();
	private CalrenSustanciasActividades calrenSustanciasActividades = new CalrenSustanciasActividades();

	Integer categoria;
	Double valorKilos;
	String mensajeReqRT;

	String calrenS;
	Integer calrenId;
	String empresaS;

	public String getParam() {

		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
	}

	@PostConstruct
	public void init() {
		try {
			cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public CalificacionesRenovaciones cargarDatos() {		
		calrenS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calren");
		
		calrenId = Integer.parseInt(calrenS);
		if (calrenId != null) {
			calRen = serviceCalRen.calrenPorId(calrenId);			
			empresa = serviceEmpresa.listarEmpresaPorId(calRen.getEmpresa().getId());
			cargarListaActividades(calRen.getId());
			cargarListaTipoSustancia(calRen.getId());
		} else {
			
			calRen = null;
		}

		return calRen;
	}

	//// Grabar observaciones para informe de calificacion
	public Integer operar(CalificacionesRenovaciones calrenovaciones) {
		System.out.println("entra a operar");
		try {
			if (calrenovaciones != null && calrenovaciones.getAprobado().equals("N")) {
				calrenovaciones.setObservacion(calrenovaciones.getObservacion());
				this.serviceCalRen.modificar(calrenovaciones);
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

	public void onRowCancel(RowEditEvent<CalificacionesRenovaciones> event) {
		FacesMessage msg = new FacesMessage("Cancelar",
				String.valueOf(((CalificacionesRenovaciones) event.getObject()).getSecuencia()));
		FacesContext.getCurrentInstance().addMessage(null, msg);
	}

	////////////////////////////////////// ACTIVIDADES
	public void cargarListaActividades(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenActividadesCalificacion = serviceCalrenActividades.listaCalrenActividades(id_calren);
		} else {
			listaCalrenActividadesCalificacion = null;
		}
	}

	//////////////////////////////////////// TIPO DE SUSTANCIA
	public void cargarListaTipoSustancia(Integer id_calren) {
		if (id_calren != null) {
			this.listaCalrenTipoSustancia = serviceCalrenTipoSustancia.listaCalrenTipoSustancias(id_calren);			
			if (listaCalrenTipoSustancia != null && !listaCalrenTipoSustancia.isEmpty()) {				
				calrenTipoSustancia = listaCalrenTipoSustancia.get(0);				
				if (calrenTipoSustancia != null) {
					cargarListaSustancias(calRen.getId(), calrenTipoSustancia.getTipoSustancia().getId());
					requiereRT(calRen.getId());
				}
			}
		} else {
			listaCalrenTipoSustancia = null;
		}
	}

	public void onRowSelectTipo(SelectEvent<CalrenTipoSustancia> event) {
		cargarListaSustancias(((CalrenTipoSustancia) event.getObject()).getCalificacionesRenovaciones().getId(),
				((CalrenTipoSustancia) event.getObject()).getTipoSustancia().getId());
	}

	public void onRowUnselectTipo(UnselectEvent<CalrenTipoSustancia> event) {
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
				calRen = serviceCalRen.calrenPorId(calrenSus.getCalificacionesRenovaciones().getId());
				if (calRen.getAprobado().equals("N")) {
					calrenSus.setEstado(calrenSustancias.getEstado());
					calrenSus.setTipoCambio(calrenSustancias.getTipoCambio());
					calrenSus.setCupo_asignado(calrenSustancias.getCupo_asignado());
					calrenSus.setPresentacion(calrenSustancias.getPresentacion());
					this.serviceSustancias.modificar(calrenSus);
		
					actualizaCalren(calrenSus.getCalificacionesRenovaciones().getId());
					
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Cambio Exitoso", "Actualización completa"));
				} else if (calRen.getAprobado().equals("S")) {
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
			calRen.setCategoria_actual(categoria);
			calRen.setCupo_kg_actual(valorKilos);
			this.serviceCalRen.modificar(calRen);
			
		}
	}

	// LISTAR ACTIVIDADES POR SUSTANCIAS
	public void cargarListaSustanciasActividades(Integer id_calrensustancias) throws Exception {
		if (id_calrensustancias != null) {
			this.listaSustanciasActividades = serviceSusActividades.listaActividadesIdCalRenSus(id_calrensustancias);
		}
	}

	//////////////////////////////
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
		if (id_calren != null) {
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
	
	
	/// Ir a Calificaciones
	public void irCalRen() {
		if (calRen.getEmpresa().getId()!=null) {
			empresaS = String.valueOf(calRen.getEmpresa().getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", empresaS);
			Utilitario.irAPagina("/pg/cal/calrenconsultacal");
		}
		
	}
	
	/// Ir a Sitios
	public void irSitios() {
		if (calRen.getId()!=null) {
			calrenS = String.valueOf(calRen.getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("calren", calrenS);
			Utilitario.irAPagina("/pg/cal/calrensitioscal");
		}
	}
	
	/// Ir a Formularios actividades
		public void irFormularios() {
			if (calRen.getId()!=null) {
				calrenS = String.valueOf(calRen.getId());
				final FacesContext context = FacesContext.getCurrentInstance();
				final Flash flash = context.getExternalContext().getFlash();
				flash.put("calren", calrenS);
				Utilitario.irAPagina("/pg/cal/calrenformulariosactcal");
			}
		}

}
