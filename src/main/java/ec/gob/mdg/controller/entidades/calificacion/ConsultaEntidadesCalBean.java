package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaEntidadesCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	private Empresa empresa = new Empresa();

	String empresaS;
	Integer empresaId;
	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;

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

	/// Ir a detalle de empresa
	public String irCalRen(String codigo) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", codigo);

		return "/pg/cal/calrenconsultacal?faces-redirect=true";
	}

}
