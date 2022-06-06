package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.modelo.Provincia;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import ec.gob.mdg.control.ejb.service.IProvinciaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class CoordinacionBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private ICoordinacionService serviceCoordinacion;

	@Inject
	private IProvinciaService serviceProvincia;
	
	private List<Coordinacion> listaCoordinacion = new ArrayList<Coordinacion>();
    private List<Provincia> listaProvincia = new ArrayList<>() ;
	
    private Coordinacion coordinacion = new Coordinacion();
	private Provincia provincia = new Provincia();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarCoordinacion();
		this.tipoDialog = "Nuevo";
	}

	public void listarCoordinacion() {
		try {
			this.listaCoordinacion = this.serviceCoordinacion.listar();
			
			if (listaCoordinacion != null && !listaCoordinacion.isEmpty()) {
				coordinacion = listaCoordinacion.get(0);
				if (coordinacion != null) {
					listarProvincia(coordinacion);
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	@Transactional
	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				System.out.println("entra a registrar Coordinación: " + coordinacion.getNombre());
				
				this.serviceCoordinacion.registrar(coordinacion);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceCoordinacion.modificar(coordinacion);
			}
			this.listarCoordinacion();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Coordinacion i) {
		this.coordinacion = i;
		this.tipoDialog = "Modificar Coordinación";
	}

	public void limpiarControles() {
		this.coordinacion = new Coordinacion();
		this.tipoDialog = "Nueva Coordinación";
	}
	
	public void onRowSelect(SelectEvent<Coordinacion> event) {
		listarProvincia((Coordinacion) event.getObject());
	}

	public void onRowUnselect(UnselectEvent<Coordinacion> event) {
		listarProvincia((Coordinacion) event.getObject());
	}
	
	/////	PROVINCIA
	public void listarProvincia(Coordinacion coordinacion) {
		try {
			this.listaProvincia = this.serviceProvincia.listaPorCoordinacion(coordinacion);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void operarProvincia(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceProvincia.registrar(provincia);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceProvincia.modificar(provincia);
			}
			listarCoordinacion();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarDataP(Provincia i) {
		this.provincia = i;
		this.tipoDialog = "Modificar Provincia";
	}

	public void limpiarControlesP() {
		this.provincia = new Provincia();
		this.tipoDialog = "Nueva Provincia";
	}
}
