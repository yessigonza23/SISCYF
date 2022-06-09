package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.MenuRolSiscyf;
import ec.gob.mdg.control.ejb.modelo.MenuSiscyf;
import ec.gob.mdg.control.ejb.modelo.RolSiscyf;
import ec.gob.mdg.control.ejb.service.IMenuRolSiscyfService;
import ec.gob.mdg.control.ejb.service.IMenuSiscyfService;
import ec.gob.mdg.control.ejb.service.IRolSiscyfService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class AsignarMenuRolBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IRolSiscyfService serviceRol;

	@Inject
	private IMenuRolSiscyfService serviceMenuRol;

	@Inject
	private IMenuSiscyfService serviceMenu;

	private List<MenuRolSiscyf> listaMenuRol;
	private List<MenuSiscyf> listaMenu;
	private MenuSiscyf menu;
	private RolSiscyf rol;
	String idRols = null;
	Integer idRol;

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idRol");
	}

	@PostConstruct
	public void init() {
		this.listar();
	}

	public void listar() {
		idRols = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idRol");
		idRol = Integer.parseInt(idRols);
		rol = serviceRol.mostrarRolPorId(idRol);
		listaMenuRol = serviceMenuRol.listaMenuRolAsignados(idRol);
		listaMenu = serviceMenuRol.listaMenuPendientes(idRol);
	}

	public void quitar(Integer id_menu, Integer id_rol) {
		this.serviceMenuRol.eliminarMenuRol(id_menu, id_rol);
		listaMenuRol = serviceMenuRol.listaMenuRolAsignados(id_rol);
		listaMenu = serviceMenuRol.listaMenuPendientes(id_rol);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Se quita el menú", "Exitósamente"));
	}

	public void asignar(Integer id_menu, Integer id_rol) {
		
		menu = this.serviceMenu.mostraMenu(id_menu);
		rol = this.serviceRol.mostrarRolPorId(id_rol);
		MenuRolSiscyf menuRol = new MenuRolSiscyf();
		try {
			menuRol.setMenu(menu);
			menuRol.setRol(rol);
			
			this.serviceMenuRol.registrar(menuRol);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listaMenuRol = serviceMenuRol.listaMenuRolAsignados(id_rol);
		
		listaMenu = serviceMenuRol.listaMenuPendientes(id_rol);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Se asigna el menú", "Exitosamente"));
	}


}
