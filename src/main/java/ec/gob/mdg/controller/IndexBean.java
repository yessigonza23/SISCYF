package ec.gob.mdg.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

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

	public String login() throws Exception {
		String redireccion = "";

		boolean respuesta = serviceOpUsuarios.siExisteUsuarioConParametrosIngresados(us);
		boolean respuestaB = serviceOpUsuarios.usuarioBloqueado(us);

		if (respuesta == false) {
			try {
				System.out.println("entra a usuario no existe");
				usuarioSesiones.setFecha(fechaActual);
				usuarioSesiones.setMensaje("Usuario no Existe");
				usuarioSesiones.setTipo("Fallido");
				usuarioSesiones.setUsername(us.getUsername());
				serviceUsuarioSesiones.registrar(usuarioSesiones);
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"Credenciales incorrectas, Usuario no existe ", "Aviso, "));
			} catch (Exception e) {
				System.out.println("error usuario existe null " + e);
				e.printStackTrace();
			}

		} else if (respuesta == true) {

			if (respuestaB == true) {
				usuarioSesiones.setFecha(fechaActual);
				usuarioSesiones.setMensaje("Usuario Bloqueado");
				usuarioSesiones.setTipo("Fallido");
				usuarioSesiones.setUsername(us.getUsername());
				serviceUsuarioSesiones.registrar(usuarioSesiones);
				
						FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN,
						"El usuario ha sido bloqueado, enviar correo al Administrador Funcional de la Dirección", "Aviso"));
			} else if (respuestaB == false) {
				try {
					Usuario usuario = serviceUsuario.login(us);

					if (usuario != null && usuario.getEstado().equalsIgnoreCase("A")) {

						if (usuario.getFecha_registro() == null) {
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
						} else if (usuario.getFecha_cambio_clave() != null) {
							LocalDateTime fecha = usuario.getFecha_cambio_clave();
							fecha= fecha.plusDays(90);
//							
//							
//							Calendar calendar = Calendar.getInstance();
//							calendar.setTime(fecha);
//							calendar.add(Calendar.DAY_OF_YEAR, 90);
//							Date fechaS = calendar.getTime();

							if (fecha.compareTo(LocalDateTime.now()) < 0) {
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/cambioClave?faces-redirect=true";
							} else {
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/protegido/principal?faces-redirect=true";
							}
						}

						usuarioSesiones.setFecha(fechaActual);
						usuarioSesiones.setMensaje("Usuario correcto");
						usuarioSesiones.setTipo("Exitoso");
						usuarioSesiones.setUsername(us.getUsername());
						serviceUsuarioSesiones.registrar(usuarioSesiones);
						
					} else {

						contador++;

						usuarioSesiones.setFecha(fechaActual);
						usuarioSesiones.setMensaje("Contraseña Incorrecta");
						usuarioSesiones.setTipo("Fallido");
						usuarioSesiones.setUsername(us.getUsername());
						serviceUsuarioSesiones.registrar(usuarioSesiones);
						
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
				} catch (Exception e) {

					if (contador > 3) {
						try {
							usuarioSesiones.setFecha(fechaActual);
							usuarioSesiones.setMensaje("Usuario Bloqueado");
							usuarioSesiones.setTipo("Fallido");
							usuarioSesiones.setUsername(us.getUsername());
							serviceUsuarioSesiones.registrar(usuarioSesiones);
							
							FacesContext.getCurrentInstance().addMessage(null,
									new FacesMessage(FacesMessage.SEVERITY_WARN,
											"El usuario ha sido bloqueado, comuníquese con la Dirección Financiera",
											"Aviso"));
						} catch (Exception e1) {
							System.out.println("error " + e);
							e1.printStackTrace();
						}

					}
				}
			}

		}

		return redireccion;
	}

	

}
