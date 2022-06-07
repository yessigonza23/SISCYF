package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Canton;
import ec.gob.mdg.control.ejb.modelo.Provincia;
import ec.gob.mdg.control.ejb.service.ICantonService;
import ec.gob.mdg.control.ejb.service.IProvinciaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class CantonBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IProvinciaService serviceProvincia;

	@Inject
	private ICantonService serviceCanton;
	
	private List<Canton> listaCanton = new ArrayList<>();
	private List<Provincia> listaProvincia = new ArrayList<Provincia>();

	private Canton canton = new Canton();
	private Provincia provincia = new Provincia();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarProvincia();
		listarCanton();
		this.tipoDialog = "Nuevo";
	}

	public void listarProvincia() {
		try {
			this.listaProvincia = this.serviceProvincia.listar();
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
				this.serviceCanton.registrar(canton);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceCanton.modificar(canton);
			}
			this.listarCanton();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Canton i) {
		this.canton = i;
		this.tipoDialog = "Modificar Cantón";
	}

	public void limpiarControles() {
		this.canton = new Canton();
		this.tipoDialog = "Nuevo Cantón";
	}
	
	
}
