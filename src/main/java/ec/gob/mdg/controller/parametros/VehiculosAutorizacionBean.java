package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.VehiculosAutorizacion;
import ec.gob.mdg.control.ejb.service.IVehiculosAutorizacionService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class VehiculosAutorizacionBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IVehiculosAutorizacionService serviceVehiculosAutorizacion;

	private List<VehiculosAutorizacion> listaVehiculosAutorizacion = new ArrayList<>();

	private VehiculosAutorizacion vehiculosAutorizacion = new VehiculosAutorizacion();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarVehiculosAutorizacion();
		this.tipoDialog = "Nuevo";
	}

	public void listarVehiculosAutorizacion() {
		try {
			this.listaVehiculosAutorizacion =this.serviceVehiculosAutorizacion.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceVehiculosAutorizacion.registrar(vehiculosAutorizacion);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceVehiculosAutorizacion.modificar(vehiculosAutorizacion);
			}
			this.listarVehiculosAutorizacion();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(VehiculosAutorizacion i) {
		this.vehiculosAutorizacion = i;
		this.tipoDialog = "Modificar Autorización de Vehiculos";
	}

	public void limpiarControles() {
		this.vehiculosAutorizacion = new VehiculosAutorizacion();
		this.tipoDialog = "Nuevo Autorización de Vehículos";
	}
	
}
