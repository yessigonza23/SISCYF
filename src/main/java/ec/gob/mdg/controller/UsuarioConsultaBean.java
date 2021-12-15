package ec.gob.mdg.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConUsuario;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class UsuarioConsultaBean implements Serializable {

	private static final long serialVersionUID = -6912327508466146927L;

	@Inject
	private IUsuarioService serviceUsuario;
	
	@Inject
	private OperacionesConUsuario serviceOperacionesUsuario;

	private List<Usuario> listaUsuario = new ArrayList<>();
	boolean estadeshabilitado;
	boolean estadeshabilitado_ap = true;
	Integer idusuario = 0;
	boolean valida=false;
	private String tipoDialog;
	String contrasena;
	
	private Usuario usuario;
	
	//private Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	
	@PostConstruct
	public void init() {
		this.listarUsuarioPunto();
		usuario = new Usuario();
	}

	//VALIDA SI EXISTE EL USUARIO
	public boolean validaCedula(String cedula) {
		valida=serviceOperacionesUsuario.validaUsuarioCedula(cedula);
		System.out.println("imprime el valida : "+valida);
		if(valida==true) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_FATAL, "Esta cédula ya fue ingresada", "ERROR"));
		}
		return valida;
	}

	public void listarUsuarioPunto() {
		try {
			this.listaUsuario = this.serviceUsuario.listar();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	public void operar(String accion) {
		try {
			 if(accion.equalsIgnoreCase("M")) {
				 contrasena="1";
//				 String clave = contrasena;	
//					String claveHash = BCrypt.hashpw(clave, BCrypt.gensalt());
//					
//					usuario.setContrasena(claveHash);
					usuario.setNombre(usuario.getNombre().toUpperCase());
					usuario.setCorreo_electronico(usuario.getCorreo_electronico().toLowerCase());
					usuario.setCargo(usuario.getCargo().toUpperCase());
					usuario.setUsername(usuario.getUsername().toLowerCase());
					usuario.setTitulo(usuario.getTitulo());
					usuario.setEstado(usuario.getEstado());
					
				this.serviceUsuario.modificar(this.usuario);
				
			}
			this.listarUsuarioPunto();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void mostrarData(Usuario i) {
		this.usuario = i;
		this.tipoDialog = "Modificar Usuario";
	}
	

}
