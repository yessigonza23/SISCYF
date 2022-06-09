package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.RolSiscyf;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.modelo.UsuarioRolSiscyf;
import ec.gob.mdg.control.ejb.service.IRolSiscyfService;
import ec.gob.mdg.control.ejb.service.IUsuarioRolSiscyfService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class AsignarRolUsuarioBean implements Serializable {

	private static final long serialVersionUID = 7758987860831881860L;

	@Inject
	private IUsuarioService serviceUsuario;

	@Inject
	private IUsuarioRolSiscyfService serviceUsuarioRol;

	@Inject
	private IRolSiscyfService serviceRol;

	private Usuario usuario;
	private RolSiscyf rol;

	private List<UsuarioRolSiscyf> listaUsuarioRol;
	private List<RolSiscyf> listaRol;

	String idUsuarios;
	Integer idUsuario;

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idUsuario");
	}

	@PostConstruct
	public void init() {
		listar();
	}

	public void listar() {
		idUsuarios = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("idUsuario");
		idUsuario = Integer.parseInt(idUsuarios);
		usuario = serviceUsuario.mostrarUsuarioPorId(idUsuario);		
	}
	
	public void quitar(Integer id_usuario, Integer id_rol) {		
		usuario = serviceUsuario.mostrarUsuarioPorId(id_usuario);		
		this.serviceUsuarioRol.eliminarRolUsuario(id_usuario, id_rol);	
		
		listaUsuarioRol = serviceUsuarioRol.listarRolesPorUsuario(usuario);
		listaRol = serviceUsuarioRol.listarRolesPendientes(usuario);

		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Se elimina el rol", "Exitósamente"));
	}

	public void asignar(Integer id_usuario, Integer id_rol) {
//		System.out.println("");
		usuario = serviceUsuario.mostrarUsuarioPorId(id_usuario);
		rol = this.serviceRol.mostrarRolPorId(id_rol);
		UsuarioRolSiscyf usuarioRol = new UsuarioRolSiscyf();
		try {
			usuarioRol.setUsuario(usuario);
			usuarioRol.setRolsiscyf(rol);
			this.serviceUsuarioRol.registrar(usuarioRol);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listaUsuarioRol = serviceUsuarioRol.listarRolesPorUsuario(usuario);
		listaRol = serviceUsuarioRol.listarRolesPendientes(usuario);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Se asigna el rol", "Exitósamente"));
	}
	
}
