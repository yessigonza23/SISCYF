package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Canton;
import ec.gob.mdg.control.ejb.modelo.Parroquia;
import ec.gob.mdg.control.ejb.service.ICantonService;
import ec.gob.mdg.control.ejb.service.IParroquiaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ParroquiaBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IParroquiaService serviceParroquia;

	@Inject
	private ICantonService serviceCanton;
	
	private List<Canton> listaCanton = new ArrayList<>();
	private List<Parroquia> listaParroquia = new ArrayList<>();

	private Canton canton = new Canton();
	private Parroquia parroquia = new Parroquia();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarParroquia();
		listarCanton();
		this.tipoDialog = "Nuevo";
	}

	public void listarParroquia() {
		try {
			this.listaParroquia = this.serviceParroquia.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void listarCanton() {
		try {
			this.listaCanton = this.serviceCanton.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceParroquia.registrar(parroquia);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceParroquia.modificar(parroquia);
			}
			this.listarParroquia();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Parroquia i) {
		this.parroquia = i;
		this.tipoDialog = "Modificar Parroquia";
	}

	public void limpiarControles() {
		this.parroquia = new Parroquia();
		this.tipoDialog = "Nueva Parroquia";
	}
	
	
}
