package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.VehiculosTipo;
import ec.gob.mdg.control.ejb.service.IVehiculosTipoService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class VehiculosTipoBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IVehiculosTipoService serviceVehiculosTipo;

	private List<VehiculosTipo> listaVehiculosTipo = new ArrayList<>();

	private VehiculosTipo vehiculosTipo = new VehiculosTipo();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarVehiculosTipo();
		this.tipoDialog = "Nuevo";
	}

	public void listarVehiculosTipo() {
		try {
			this.listaVehiculosTipo =this.serviceVehiculosTipo.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceVehiculosTipo.registrar(vehiculosTipo);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceVehiculosTipo.modificar(vehiculosTipo);
			}
			this.listarVehiculosTipo();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(VehiculosTipo i) {
		this.vehiculosTipo = i;
		this.tipoDialog = "Modificar Tipo Vehiculos";
	}

	public void limpiarControles() {
		this.vehiculosTipo = new VehiculosTipo();
		this.tipoDialog = "Nuevo Tipo Vehículos";
	}
	
}
