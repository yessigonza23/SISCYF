package ec.gob.mdg.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.modelo.UsuarioSesiones;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConUsuario;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import ec.gob.mdg.control.ejb.service.IUsuarioSesionesService;
import ec.gob.mdg.control.ejb.utils.UtilsDate;
import lombok.Data;

@Data
@Named
@ViewScoped
public class IndexBean implements Serializable {

	private static final long serialVersionUID = 992085370965349203L;

	@Inject
	private IUsuarioService serviceUsuario;

	@Inject
	private IUsuarioSesionesService serviceUsuarioSesiones;

	@Inject
	private OperacionesConUsuario serviceOpUsuarios;

	private Usuario us;
	private UsuarioSesiones usuarioSesiones;

	Date fechaActual;
	Integer contador;

	@PostConstruct
	public void init() {
		fechaActual = UtilsDate.timestamp();
		this.us = new Usuario();
		contador = 0;
	}

	@Transactional
	public String login() throws Exception {
		String redireccion = "";

		// us.setUsername(us.getUsername());
		if (us.getUsername() != null) {
			boolean respuesta = serviceOpUsuarios.rucEstaRegistrado(us.getUsername());
			boolean respuestaB = serviceOpUsuarios.usuarioBloqueado(us.getUsername());

			if (respuesta == false) {
				try {
					grabaSesion("Usuario no Existe", "Fallido");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Credenciales incorrectas, Usuario no existe ", "Aviso, "));
				} catch (Exception e) {
					System.out.println("error no existe null " + e);
					e.printStackTrace();
				}
			} else if (respuesta == true) {
				if (respuestaB == true) {
					grabaSesion("Usuario Bloqueado", "Fallido");
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
							"El usuario ha sido bloqueado, enviar correo al Administrador Funcional de la Dirección",
							"Aviso"));
				} else if (respuestaB == false) {
					Usuario usuario = serviceUsuario.login(us);

					if (usuario != null && usuario.getEstado().equalsIgnoreCase("A")) {
						
						if (usuario.isReseteaClave() == true) {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
						} else if (usuario.getFecha_cambio_clave() == null) {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
						} else if (usuario.getFecha_cambio_clave() != null) {

							LocalDateTime ldt = usuario.getFecha_cambio_clave();
							ZonedDateTime zdt = ldt.atZone(ZoneId.systemDefault());
							Date fecha = Date.from(zdt.toInstant());

							// Date fecha = usuario.getFecha_cambio_clave();
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(fecha);
							calendar.add(Calendar.DAY_OF_YEAR, 90);
							Date fechaS = Date.from(zdt.toInstant());
							

							if (fechaS.compareTo(fechaActual) > 0) {
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/cambioClave?faces-redirect=true";
							} else {
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/pg/adm/principal?faces-redirect=true";
							}
						}
						grabaSesion("Usuario Correcto","Exitoso");

					} else {
					
						contador++;
						grabaSesion( "Contraseña Incorrecta","Fallido");

						if (contador < 3) {

							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_WARN,
											"Credenciales incorrectas, " + contador
													+ " de 3 intentos, al tercer intento el usuario será bloqueado",
											"Aviso, "));
					
						} else {
							System.out.println("ENTRA MAS DE 3");

							us.setEstado("B");
							serviceUsuario.modificar(us);

							FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(
									FacesMessage.SEVERITY_WARN,
									"Credenciales incorrectas, el usuario fue bloqueado, comuníquese con la Dirección Financiera",
									"Aviso"));
						}
					}
				}
			}
		}
		return redireccion;
	}

	// almacena tabla usuariosesiones
	@Transactional
	public void grabaSesion(String Mensaje, String Tipo) {
		UsuarioSesiones usuarioSesiones = new UsuarioSesiones();

		usuarioSesiones.setFecha(fechaActual);
		usuarioSesiones.setMensaje(Mensaje);
		usuarioSesiones.setTipo(Tipo);
		usuarioSesiones.setUsername(us.getUsername());
		try {
			serviceUsuarioSesiones.registrar(usuarioSesiones);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
