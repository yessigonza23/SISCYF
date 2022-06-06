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
import ec.gob.mdg.control.ejb.modelo.VistaPaisesImpExpDTO;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.ICalrenActividadesCalificacionService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.service.IVistaPaisesImpExpDTOService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaCalRenFormulariosImpExpCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@Inject
	private ICalrenActividadesCalificacionService serviceCalRenActCal;

//	@Inject
//	private ICalrenSustanciasActividadesService serviceCalRenSusAct;

	@Inject
	private IVistaPaisesImpExpDTOService serviceVistaPaisesImpExpDTO;

	///// LISTAR LOS TIPOS SI ES IMPORTACION Y EXPORTACION
	private List<VistaPaisesImpExpDTO> listaTipoIE = new ArrayList<VistaPaisesImpExpDTO>();
	private VistaPaisesImpExpDTO tipoIE = new VistaPaisesImpExpDTO();
	//// LISTAR SUSTANCIAS
	private List<VistaPaisesImpExpDTO> listaSustancias = new ArrayList<>();
	private VistaPaisesImpExpDTO sustancias = new VistaPaisesImpExpDTO();
	//// LISTAR PAISES
	private List<VistaPaisesImpExpDTO> listaPaises = new ArrayList<VistaPaisesImpExpDTO>();

	private Empresa empresa = new Empresa();
	private CalificacionesRenovaciones calRen = new CalificacionesRenovaciones();
	private CalrenActividadesCalificacion calRenActCal = new CalrenActividadesCalificacion();

	String calrenactS;
	Integer calrenactId;
	String abreviatura;
	String calrenS;
	Integer calrenaId;

	@PostConstruct
	public void init() {
		try {
			System.out.println("entra a init");
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
		abreviatura = "I/E";
		calrenactS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("calrenact");
		calrenactId = Integer.parseInt(calrenactS);
		System.out.println("1");
		if (calrenactId != null) {
			calRenActCal = serviceCalRenActCal.listaCalrenActividadesId(calrenactId);
			System.out.println("2");
			if (calRenActCal != null) {
				calRen = serviceCalRen.calrenPorId(calRenActCal.getCalificacionesRenovaciones().getId());
				System.out.println("3");
				if (calRen != null) {
					empresa = serviceEmpresa.listarEmpresaPorId(calRen.getEmpresa().getId());
					listaTipoIE = serviceVistaPaisesImpExpDTO.listarTipo(calRen.getId());
					System.out.println("4: listaTipoIE " + listaTipoIE);

					if (listaTipoIE != null && !listaTipoIE.isEmpty()) {
						System.out.println("4.1: " + listaTipoIE);
						tipoIE = listaTipoIE.get(0);
						System.out.println("5"+tipoIE.getImpexp_actividad_codigo());
						if (tipoIE != null) {
							listaSustancias = serviceVistaPaisesImpExpDTO.listarSustancias(calRen.getId(),
									tipoIE.getImpexp_actividad_codigo());
							System.out.println("6");
							if (listaSustancias != null && !listaSustancias.isEmpty()) {
								sustancias = listaSustancias.get(0);
								System.out.println("7");
								if (sustancias != null) {
									System.out.println(
											"aaaaaaaaaaaantes de cargar paises " + sustancias.getCalrensusact_id());

									cargarListaPaises(sustancias.getCalrensusact_id(),tipoIE.getImpexp_actividad_codigo());
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

	public void cargarListaPaises(Integer id_CalRenSusAct,String actividad) {
		System.out.println("carga paisesssssssssssssssss" + id_CalRenSusAct);
		listaPaises = serviceVistaPaisesImpExpDTO.listarPaises(id_CalRenSusAct,actividad);
	}

	public void cargarListaSustancias(Integer id_calren, String actividad ) {
		System.out.println("carga SUSTANCIASSSSSSSSSSSSSSSSSSS: " + id_calren + "_" + actividad);
		abreviatura = "I/E";
		listaSustancias = serviceVistaPaisesImpExpDTO.listarSustancias(id_calren, actividad);
		if (listaSustancias != null && !listaSustancias.isEmpty()) {
			sustancias = listaSustancias.get(0);

			if (sustancias != null) {
				System.out.println("aaaaaaaaaaaantes de cargar paises " + sustancias.getCalrensusact_id());

				cargarListaPaises(sustancias.getCalrensusact_id(),actividad);
			}
		}
	}

	///// LISTAR SUSTANCIAS POR TIPO IMP/EXP

	public void onRowSelectTipo(SelectEvent<VistaPaisesImpExpDTO> event) {
		cargarListaSustancias(
				((VistaPaisesImpExpDTO) event.getObject()).getCalren_id(),
				((VistaPaisesImpExpDTO) event.getObject()).getActcal_abreviatura());
	}

	public void onRowUnselectTipo(UnselectEvent<VistaPaisesImpExpDTO> event) {
		cargarListaSustancias(
				((VistaPaisesImpExpDTO) event.getObject()).getCalren_id(),
				((VistaPaisesImpExpDTO) event.getObject()).getActcal_abreviatura());
	}

	/////// LISTAR PAISES

	public void onRowSelect(SelectEvent<VistaPaisesImpExpDTO> event) {
		cargarListaPaises(((VistaPaisesImpExpDTO) event.getObject()).getCalrensusact_id(),tipoIE.getImpexp_actividad_codigo());
	}

	public void onRowUnselect(UnselectEvent<VistaPaisesImpExpDTO> event) {
		cargarListaPaises(((VistaPaisesImpExpDTO) event.getObject()).getCalrensusact_id(),tipoIE.getImpexp_actividad_codigo());
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
