package ec.gob.mdg.controller;

import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class MasterBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public void verificarSesion() throws IOException {
		FacesContext context = FacesContext.getCurrentInstance();
		try {
			Usuario us = (Usuario) context.getExternalContext().getSessionMap().get("usuario");

			if (us == null) {
				Utilitario.irAPagina("/index.xhtml");
			}
		} catch (Exception e) {
			Utilitario.irAPagina("/index.xhtml");
		}
	}

	public String mostrarUsuarioLogueado() {
		// captura del tipo de usuario que ha iniciado sesion
		Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
		return us.getNombre();
	}

	public void timeout() throws IOException {
		cerrarSesion();
		index();

	}

	public void index() {
		Utilitario.irAPagina("/index.xhtml");
	}

	public void cerrarSesion() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
	}

}
