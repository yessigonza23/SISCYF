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

import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.EmpresaRepresentantes;
import ec.gob.mdg.control.ejb.service.IEmpresaRepresentantesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.dinardap.modelo.SuperCiasRepresentantesRucDTO;
import ec.gob.mdg.dinardap.servicios.ServiciosWeb;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaRepresentantesCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaRepresentantesService serviceEmpresaRepresentantes;

	@Inject
	private IEmpresaService serviceEmpresa;

	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRT = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRL = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRB = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRLo = new ArrayList<>();
	private List<SuperCiasRepresentantesRucDTO> listaEmpresaRepresentantesSCias = new ArrayList<>();

	private EmpresaRepresentantes empresaRepresentantes = new EmpresaRepresentantes();
	private Empresa empresa = new Empresa();
	private SuperCiasRepresentantesRucDTO ciasRepresentantesRucDTO = new SuperCiasRepresentantesRucDTO();

	Boolean render_rt;
	Boolean render_rl;
	Boolean render_rb;
	Boolean render_rlo;
	Boolean render_rcias;
	String empresaS;
	Integer empresaId;

	@PostConstruct
	public void init() {
		try {
			cargaRepresentantes();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	public void cargaRepresentantes() {

		empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
		empresaId = Integer.parseInt(empresaS);
		render_rt = false;
		render_rl = false;
		render_rb = false;
		render_rlo = false;
		render_rcias = false;
		if (empresaId != null) {
			empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
			if (empresa != null) {
				listaEmpresaRepresentantesRT = serviceEmpresaRepresentantes.listarRepTecnicos(empresa);
				listaEmpresaRepresentantesRL = serviceEmpresaRepresentantes.listarRepLegales(empresa);
				listaEmpresaRepresentantesRB = serviceEmpresaRepresentantes.listarRepBodegueros(empresa);
				listaEmpresaRepresentantesRLo = serviceEmpresaRepresentantes.listarRepLogisticos(empresa);
				listaEmpresaRepresentantesSCias = ServiciosWeb.consultarRepresentantesSupercias(empresa.getRuc());

				if (listaEmpresaRepresentantesRT.size() > 0) {
					render_rt = true;
				}
				if (listaEmpresaRepresentantesRL.size() > 0) {
					render_rl = true;
				}
				if (listaEmpresaRepresentantesRB.size() > 0) {
					render_rb = true;
				}
				if (listaEmpresaRepresentantesRLo.size() > 0) {
					render_rlo = true;
				}
				if (listaEmpresaRepresentantesSCias.size() > 0) {
					render_rcias = true;
				}
			}
		}

	}

	public void mostrarData(EmpresaRepresentantes i) {
		this.empresaRepresentantes = i;
	}

	/// Regresar a detalle de empresa
	public void irDetalleEmpresa() {
		if (empresa != null) {
			empresaS = String.valueOf(empresa.getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", empresaS);
			Utilitario.irAPagina("/pg/cal/entprincipalcal");
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay datos o No puede regresar", "Sin datos"));
		}
	}
}
