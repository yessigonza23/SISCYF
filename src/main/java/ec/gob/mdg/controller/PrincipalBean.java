package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.utils.UtilsDate;
import lombok.Data;

@Data
@Named
@ViewScoped
public class PrincipalBean implements Serializable {

	private static final long serialVersionUID = 1L;	

	Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	Date fechaActual;

	@PostConstruct
	public void init() {
		try {
			fechaActual = UtilsDate.timestamp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
