package ec.gob.mdg.controller.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.modelo.EmpresaRepresentantes;
import ec.gob.mdg.control.ejb.service.IEmpresaRepresentantesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaRepresentantesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaRepresentantesService serviceEmpresaRepresentantes;

	@Inject
	private IEmpresaService serviceEmpresa;

	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRT = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRL = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRB = new ArrayList<>();
	private List<EmpresaRepresentantes> listaEmpresaRepresentantesRLo = new ArrayList<>();

	private EmpresaRepresentantes empresaRepresentantes = new EmpresaRepresentantes();
	private Empresa empresa = new Empresa();

	Boolean render_rt;
	Boolean render_rl;
	Boolean render_rb;
	Boolean render_rlo;
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
		if (empresa != null) {
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
			empresaId = Integer.parseInt(empresaS);
			render_rt = false;
			render_rl = false;
			render_rb = false;
			render_rlo = false;
			if (empresaId != null) {
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
				if (empresa != null) {
					listaEmpresaRepresentantesRT = serviceEmpresaRepresentantes.listarRepTecnicos(empresa);
					listaEmpresaRepresentantesRL = serviceEmpresaRepresentantes.listarRepLegales(empresa);
					listaEmpresaRepresentantesRB = serviceEmpresaRepresentantes.listarRepBodegueros(empresa);
					listaEmpresaRepresentantesRLo = serviceEmpresaRepresentantes.listarRepLogisticos(empresa);

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
				}
			}
		}
	}
	
	public void mostrarData(EmpresaRepresentantes i) {
		this.empresaRepresentantes = i;
	}

}
