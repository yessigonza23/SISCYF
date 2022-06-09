package ec.gob.mdg.controller;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.RolSiscyf;
import ec.gob.mdg.control.ejb.service.IRolSiscyfService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class RolBean implements Serializable {

	private static final long serialVersionUID = 8346668750642429805L;

	@Inject
	private IRolSiscyfService service;	

	private List<RolSiscyf> lista = new ArrayList<RolSiscyf>();
	private String tipoDialog;
	private RolSiscyf rol  = new RolSiscyf();
	String permiso="N";
	
	@PostConstruct
	public void init() {
		this.listar();
		this.tipoDialog = "Nuevo";
	}
	
	public void listar() {
		try {
			this.lista = this.service.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	public void operar(String accion) {
		try {
			if(accion.equalsIgnoreCase("R")) {
				this.service.registrar(this.rol);
			}else if(accion.equalsIgnoreCase("M")) {
				this.service.modificar(this.rol);
			}
			this.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void mostrarData(RolSiscyf i) {
		this.rol = i;
		this.tipoDialog = "Modificar Rol";
	}
	
	public void limpiarControles() {
		this.rol = new RolSiscyf();
		this.tipoDialog = "Nuevo Rol";
	}	
	

}
