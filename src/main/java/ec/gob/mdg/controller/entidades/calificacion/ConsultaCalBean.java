package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
public class ConsultaCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;
	private List<Empresa> listaEmpresas = new ArrayList<Empresa>();
	private Empresa empresa = new Empresa();

	String nombre_emp;
	String id_emp;

	public void consultarListaEmpresas(String nombre) {
		if (nombre != null) {
			listaEmpresas = this.serviceEmpresa.listarEmpresasPorNombre(nombre.toUpperCase());
		}
	}

	/// Ir a detalle de empresa
	public String irDetalleEmpresa(String codigo) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", codigo);
		
		return "/pg/cal/entprincipalcal?faces-redirect=true";
	}
	

}
