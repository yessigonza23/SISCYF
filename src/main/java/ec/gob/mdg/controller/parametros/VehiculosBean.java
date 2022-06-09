package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Pais;
import ec.gob.mdg.control.ejb.modelo.Vehiculos;
import ec.gob.mdg.control.ejb.modelo.VehiculosTipo;
import ec.gob.mdg.control.ejb.service.IPaisService;
import ec.gob.mdg.control.ejb.service.IVehiculosService;
import ec.gob.mdg.control.ejb.service.IVehiculosTipoService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class VehiculosBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IVehiculosService serviceVehiculos;
	
	@Inject
	private IVehiculosTipoService serviceVehiculosTipo;
	
	@Inject
	private IPaisService servicePais;

	private List<Vehiculos> listaVehiculos = new ArrayList<>();
	private List<VehiculosTipo> listaVehiculosTipo = new ArrayList<>();
	private List<Pais> listaPais = new ArrayList<>();

	private Vehiculos vehiculos = new Vehiculos();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarVehiculos();
		listarVehiculosTipo();
		listarPais();
		this.tipoDialog = "Nuevo";
	}

	public void listarVehiculos() {
		try {
			this.listaVehiculos =this.serviceVehiculos.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void listarVehiculosTipo() {
		try {
			this.listaVehiculosTipo =this.serviceVehiculosTipo.listar();
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
				this.serviceVehiculos.registrar(vehiculos);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceVehiculos.modificar(vehiculos);
			}
			this.listarVehiculos();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Vehiculos i) {
		this.vehiculos = i;
		this.tipoDialog = "Modificar Vehiculo";
	}

	public void limpiarControles() {
		this.vehiculos = new Vehiculos();
		this.tipoDialog = "Nuevo Vehículo";
	}
	
}
