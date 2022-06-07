package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.modelo.Provincia;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import ec.gob.mdg.control.ejb.service.IProvinciaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ProvinciaBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private ICoordinacionService serviceCoordinacion;

	@Inject
	private IProvinciaService serviceProvincia;
	
	private List<Coordinacion> listaCoordinacion = new ArrayList<>();
	private List<Provincia> listaProvincia = new ArrayList<Provincia>();

	private Coordinacion coordinacion = new Coordinacion();
	private Provincia provincia = new Provincia();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarProvincia();
		listarCoordinacion();
		this.tipoDialog = "Nuevo";
	}

	public void listarProvincia() {
		try {
			this.listaProvincia = this.serviceProvincia.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void listarCoordinacion() {
		try {
			this.listaCoordinacion = this.serviceCoordinacion.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceProvincia.registrar(provincia);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceProvincia.modificar(provincia);
			}
			this.listarProvincia();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Provincia i) {
		this.provincia = i;
		this.tipoDialog = "Modificar Provincia";
	}

	public void limpiarControles() {
		this.provincia = new Provincia();
		this.tipoDialog = "Nueva Provincia";
	}
	
	
}
