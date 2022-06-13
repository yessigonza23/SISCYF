package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.MenuSiscyf;
import ec.gob.mdg.control.ejb.service.IMenuSiscyfService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class MenuDinamicoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private IMenuSiscyfService serviceMenu;
	
	private List<MenuSiscyf> listaMenu=new ArrayList<MenuSiscyf>();
	private List<MenuSiscyf> listaSubMenu=new ArrayList<MenuSiscyf>();
	private MenuSiscyf submenu = new MenuSiscyf();
	private MenuSiscyf menu= new MenuSiscyf();
	private String tipoDialog;
	
	@PostConstruct
	public void init() {
		this.listar();
		this.tipoDialog = "Nuevo";
	}
	
	public void listar() {
		try {
			listaMenu = serviceMenu.listar();
			listaSubMenu = serviceMenu.listar();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	public void operar(String accion) {
		try {
			if(accion.equalsIgnoreCase("R")) {
				System.out.println(accion + " acción " + submenu);
				menu.setSubmenu(submenu);
				this.serviceMenu.registrar(this.menu);
				System.out.println("registra termina");
			}else if(accion.equalsIgnoreCase("M")) {
				this.serviceMenu.modificar(this.menu);
			}
			this.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void mostrarData(MenuSiscyf i) {
		this.menu = i;
		this.tipoDialog = "Modificar Menu";
	}
	
	public void limpiarControles() {
		this.menu = new MenuSiscyf();
		this.tipoDialog = "Nuevo Menu";
	}
	

	
}
