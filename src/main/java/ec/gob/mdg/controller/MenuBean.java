package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

import ec.gob.mdg.control.ejb.dao.IMenuSiscyfDAO;
import ec.gob.mdg.control.ejb.modelo.MenuSiscyf;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConUsuario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class MenuBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@EJB
	private IMenuSiscyfDAO MenuDao;
	
	
	@Inject
	private OperacionesConUsuario servicioMenuRol;
	
	Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	
	private List<MenuSiscyf> listaMenu;
	private MenuModel model;
	
	
	@PostConstruct
	public void init() {		
		this.listarMenus();
		model = new DefaultMenuModel();
		this.establecerPermisos();
	}
	
	public void listarMenus() {
		try {
			listaMenu = servicioMenuRol.listaMenuRolporUsuario(us);
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
		
	public void establecerPermisos() {

	        for (MenuSiscyf m : listaMenu) {
	        	if (m.getTipo().equals("S")) {
	                DefaultSubMenu firstSubmenu = new DefaultSubMenu(m.getNombre());

	                for (MenuSiscyf i : listaMenu) {
	                	MenuSiscyf submenu = i.getSubmenu();
	                    if (submenu != null) {
	                    	if (submenu.getId() == m.getId()) {
	                        	DefaultMenuItem item = new DefaultMenuItem(i.getNombre());
	                            item.setUrl(i.getLink());
	                            firstSubmenu.addElement(item);
	                        }
	                    }
	                }	                
	                model.addElement(firstSubmenu);
	              } 
	        	else {
	            	if (m.getSubmenu() == null) {
	                    DefaultMenuItem item = new DefaultMenuItem(m.getNombre());
	                    item.setUrl(m.getLink());
	                    model.addElement(item);
	                }
	            }
	            
	        }
	}

	
}
