package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import lombok.Data;


@Data
@Named
@ViewScoped
public class CambioPuntoBean implements Serializable{


	private static final long serialVersionUID = 1L;

	@Inject
	private IUsuarioService serviceUsuario;
	
	@Inject
	private ICoordinacionService serviceCoordinacion;

	private List<Coordinacion> listaCoordinacion;
	private Coordinacion coordinacionnueva = new Coordinacion();
	
	Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	
	@PostConstruct
	public void init() {
		try {	
			listarCoordinaciones();			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void listarCoordinaciones() {
		try {
			this.listaCoordinacion = this.serviceCoordinacion.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	@Transactional
	public void modificarCoordinacion(Coordinacion coor_nueva) {
		try {		
			us.setCoordinacion(coor_nueva);
			serviceUsuario.modificar(us);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Se cambió con éxito", "Haga clic en Continuar"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	


}
