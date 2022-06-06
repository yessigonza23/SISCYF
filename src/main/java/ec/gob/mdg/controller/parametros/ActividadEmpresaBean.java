package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.ActividadEmpresa;
import ec.gob.mdg.control.ejb.service.IActividadEmpresaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ActividadEmpresaBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IActividadEmpresaService serviceActividadEmpresa;

	private List<ActividadEmpresa> listaActividadEmpresa = new ArrayList<ActividadEmpresa>();

	private ActividadEmpresa actividadEmpresa = new ActividadEmpresa();

	private String tipoDialog=null;

	@PostConstruct
	public void init() {
		listarActividadEmpresa();
		this.tipoDialog = "Nuevo";
	}

	public void listarActividadEmpresa() {
		try {
			this.listaActividadEmpresa = this.serviceActividadEmpresa.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceActividadEmpresa.registrar(actividadEmpresa);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceActividadEmpresa.modificar(actividadEmpresa);
			}
			this.listarActividadEmpresa();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(ActividadEmpresa i) {
		this.actividadEmpresa = i;
		this.tipoDialog = "Modificar Actividad";
	}

	public void limpiarControles() {
		this.actividadEmpresa = new ActividadEmpresa();
		this.tipoDialog = "Nueva Actividad";
	}

}
