package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.ActividadCalificacion;
import ec.gob.mdg.control.ejb.service.IActividadCalificacionService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ActividadCalificacionBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IActividadCalificacionService serviceActividadCalificacion;

	private List<ActividadCalificacion> listaActividadCalificacion = new ArrayList<ActividadCalificacion>();

	private ActividadCalificacion actividadCalificacion = new ActividadCalificacion();

	private String tipoDialog=null;

	@PostConstruct
	public void init() {
		listarActividadCalificacion();
		this.tipoDialog = "Nuevo";
	}

	public void listarActividadCalificacion() {
		try {
			this.listaActividadCalificacion = this.serviceActividadCalificacion.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceActividadCalificacion.registrar(actividadCalificacion);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceActividadCalificacion.modificar(actividadCalificacion);
			}
			this.listarActividadCalificacion();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(ActividadCalificacion i) {
		this.actividadCalificacion = i;
		this.tipoDialog = "Modificar Actividad";
	}

	public void limpiarControles() {
		this.actividadCalificacion = new ActividadCalificacion();
		this.tipoDialog = "Nueva Actividad";
	}

}
