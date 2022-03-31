package ec.gob.mdg.controller.entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.dao.ICalificacionesRenovacionesDAO;
import ec.gob.mdg.control.ejb.modelo.CalificacionesRenovaciones;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConEmpresas;
import ec.gob.mdg.control.ejb.service.ICalificacionesRenovacionesService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;

@Named
@ViewScoped
public class ConsultaEntidadesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;

	@Inject
	private OperacionesConEmpresas serviceOperEmpresas;
	
	@Inject
	private ICalificacionesRenovacionesService serviceCalRen;

	@SuppressWarnings("unused")
	private List<Empresa> listaEmpresas = new ArrayList<Empresa>();
	private Empresa empresa = new Empresa();
	
	private List<CalificacionesRenovaciones> listaCalRenovaciones = new ArrayList<>();
	private CalificacionesRenovaciones calificacionesRenovaciones = new CalificacionesRenovaciones();
	
	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;

	//CARGA DATOS GENERALES SEGUN EL TIPO DE EMPRESA
	public Empresa cargaEmpresa(Integer id) throws Exception {
		render_n = false;
		render_o = false;
		render_j = false;
		render_p = false;
		render = false;
		if (id != null) {
			render = true;
			empresa = serviceEmpresa.listaEmpresas(id);
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
		return empresa;
	}
	
	//CARGA CALIFICACIONES Y RENOVACIONES
	
	public List<CalificacionesRenovaciones> cargalista() {
		System.out.println("entra a getlista" + empresa.getId());
		listaCalRenovaciones = serviceCalRen.listaCalRenPorEmpresa(empresa);
		for(CalificacionesRenovaciones c:listaCalRenovaciones) {
			System.out.println(c.getId());
		}
		return serviceCalRen.listaCalRenPorEmpresa(empresa);
	}

	
	//////////////////////////////////////
	
	public List<Empresa> getListaEmpresas() throws Exception {
		return serviceEmpresa.listar();
	}	

	public CalificacionesRenovaciones getCalificacionesRenovaciones() {
		return calificacionesRenovaciones;
	}

	///////////////////////////GETERS

	public void setListaCalRenovaciones(List<CalificacionesRenovaciones> listaCalRenovaciones) {
		this.listaCalRenovaciones = listaCalRenovaciones;
	}
	public void setCalificacionesRenovaciones(CalificacionesRenovaciones calificacionesRenovaciones) {
		this.calificacionesRenovaciones = calificacionesRenovaciones;
	}

	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}
	
	public Empresa getEmpresa() {
		return empresa;
	}

	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	public Boolean getRender_n() {
		return render_n;
	}

	public void setRender_n(Boolean render_n) {
		this.render_n = render_n;
	}

	public Boolean getRender_j() {
		return render_j;
	}

	public void setRender_j(Boolean render_j) {
		this.render_j = render_j;
	}

	public Boolean getRender_o() {
		return render_o;
	}

	public void setRender_o(Boolean render_o) {
		this.render_o = render_o;
	}

	public Boolean getRender_p() {
		return render_p;
	}

	public void setRender_p(Boolean render_p) {
		this.render_p = render_p;
	}

	public Boolean getRender() {
		return render;
	}

	public void setRender(Boolean render) {
		this.render = render;
	}


	

}
