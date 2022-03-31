package ec.gob.mdg.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.mindrot.jbcrypt.BCrypt;

import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.modelo.UsuarioPasswords;
import ec.gob.mdg.control.ejb.service.IUsuarioPasswordsService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class UsuarioModificaBean implements Serializable {

	private static final long serialVersionUID = -6912327508466146927L;

	@Inject
	private IUsuarioService serviceUsuario;

	@Inject
	private IUsuarioPasswordsService serviceUsuarioPassword;

	Usuario usuario;
	boolean estadeshabilitado;
	private List<UsuarioPasswords> listaUsuarioPasswords;
	private boolean claveRepetida = false;
	Integer contador=0;

	@PostConstruct
	public void init() {
		mostrar();
	}

	// MOSTRAR INFORMACION DEL USUARIO
	public void mostrar() {
		usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	}

	// registrar usuario
	@Transactional
	public void modificar() {
		try {
			UsuarioPasswords uPasswords = new UsuarioPasswords();

			String clave = this.usuario.getPassword();
			String claveHashNew = BCrypt.hashpw(clave, BCrypt.gensalt());

			// VALIDAR QUE NO SEA LA MISMA CLAVE
			listaUsuarioPasswords = serviceUsuarioPassword.listaPasswords(usuario);
			claveRepetida = false;
			if (listaUsuarioPasswords.size() > 0) {
				for (UsuarioPasswords up : listaUsuarioPasswords) {
					String claveHash = up.getPassword();
               
               boolean bcript =BCrypt.checkpw(clave, claveHash);
					if (bcript == true) {
						claveRepetida = true;
						FacesContext.getCurrentInstance().addMessage(null,
								new FacesMessage(FacesMessage.SEVERITY_ERROR, "La clave no se puede repetir", "Error"));
						break;
					}
				}

			}

		//	System.out.println("Clave Repetida " + claveRepetida);
			if (claveRepetida == false) {
				boolean respuesta = validaClave(clave);

				if (respuesta == true) {
					usuario.setPassword(claveHashNew);
					usuario.setFecha_cambio_clave(LocalDateTime.now());
					usuario.setReseteaClave(false);
					this.serviceUsuario.modificar(usuario);
					estadeshabilitado = true;

					// grabar datos en usuario passwords
					uPasswords.setUsuario(usuario);
					uPasswords.setPassword(claveHashNew);
					serviceUsuarioPassword.registrar(uPasswords);

					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Se cambio la clave con éxito", "Aviso"));
				}
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/// VALIDAR CLAVE
	public boolean validaClave(String clave) {
		boolean respuesta = false;
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,12}";
		respuesta = clave.matches(pattern);

		if (respuesta == false) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
					"La clave debe contener mínimo 8 y máximo 12 caracteres, "
							+ "al menos una letra mayúscula, al menos una letra minúscula, al menos un caracter especial",
					"ERROR"));
		}
		return respuesta;
	}

	// NUEVO REGISTR0
	public Boolean nuevo() {
		estadeshabilitado = false;
		return estadeshabilitado;
	}


}
