package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Pais;
import ec.gob.mdg.control.ejb.modelo.Conductores;
import ec.gob.mdg.control.ejb.service.IPaisService;
import ec.gob.mdg.control.ejb.service.IConductoresService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConductoresBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IConductoresService serviceConductores;
	
	@Inject
	private IPaisService servicePais;

	private List<Conductores> listaConductores = new ArrayList<>();
	
	private List<Pais> listaPais = new ArrayList<>();

	private Conductores conductores = new Conductores();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarConductores();
		listarPais();
		this.tipoDialog = "Nuevo";
	}

	public void listarConductores() {
		try {
			this.listaConductores =this.serviceConductores.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	
	public void listarPais() {
		try {
			this.listaPais =this.servicePais.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceConductores.registrar(conductores);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceConductores.modificar(conductores);
			}
			this.listarConductores();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Conductores i) {
		this.conductores = i;
		this.tipoDialog = "Modificar Conductor";
	}

	public void limpiarControles() {
		this.conductores = new Conductores();
		this.tipoDialog = "Nuevo Conductor";
	}
	
}
