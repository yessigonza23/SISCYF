package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class CoordinacionBean implements Serializable{

	private static final long serialVersionUID = -932196271143767334L;

	@Inject
	private ICoordinacionService serviceCoordinacion;
		
	private List<Coordinacion> lista;
	private String tipoDialog;
	private Coordinacion coordinacion= new Coordinacion();;
		
	@PostConstruct
	public void init() {
		this.coordinacion = new Coordinacion();
		this.tipoDialog = "Nuevo";
	}
				
	public void operar(String accion) {
		try {
			if(accion.equalsIgnoreCase("R")) {
				this.serviceCoordinacion.registrar(this.coordinacion);
			}else if(accion.equalsIgnoreCase("M")) {
				this.serviceCoordinacion.modificar(this.coordinacion);
			}
			this.getLista();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void mostrarData(Coordinacion i) {
		this.coordinacion = i;
		this.tipoDialog = "Modificar Coordinación";
	}
	
	public void limpiarControles() {
		this.coordinacion = new Coordinacion();
		this.tipoDialog = "Nuevo Coordinación";
	}
	
	//sacar lista de coordinaciones

	public List<Coordinacion> getLista() {
		return lista;
	}

	

}
