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

		us.setUsername(us.getUsername());
		boolean respuesta = serviceOpUsuarios.rucEstaRegistrado(us.getUsername());

		boolean respuestaB = serviceOpUsuarios.usuarioBloqueado(us.getUsername());
		
		UsuarioSesiones usuarioSesiones = new UsuarioSesiones();
	
		if (respuesta == false) {
			try {
				usuarioSesiones.setFecha(fechaActual);
				usuarioSesiones.setMensaje("Usuario no Existe");
				usuarioSesiones.setTipo("Fallido");
				usuarioSesiones.setUsername(us.getUsername());
				serviceUsuarioSesiones.registrar(usuarioSesiones);

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
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
						"El usuario ha sido bloqueado, enviar correo al Administrador Funcional de la Dirección",
						"Aviso"));
			} else if (respuestaB == false) {
				try {
					
					Usuario usuario = serviceUsuario.login(us);				
					
					if (usuario != null && usuario.getEstado().equalsIgnoreCase("A")) {
						System.out.println("entra a usuario activo" );
						
						if (usuario.getFecha_registro() == null) {
							
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
							
							
						} else if (usuario.getFecha_cambio_clave() != null) {						
							LocalDateTime fecha = usuario.getFecha_cambio_clave();
							fecha = fecha.plusDays(90);

							if (fecha.compareTo(LocalDateTime.now()) < 0) {
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/cambioClave?faces-redirect=true";
							} else {
								
								System.out.println("entra a principal " + usuario.getNombre());
								FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
										usuario);
								redireccion = "/protegido/principal?faces-redirect=true";
							}
						}else if (usuario.getFecha_cambio_clave() == null){
							System.out.println("entra a cambiar clave " );
							FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario",
									usuario);
							redireccion = "/cambioClave?faces-redirect=true";
						}
						System.out.println("Termina antes de insertar " );
						
						usuarioSesiones.setFecha(fechaActual);
						
						usuarioSesiones.setMensaje("Usuario correcto");
						usuarioSesiones.setTipo("Exitoso");
						System.out.println("fecha" + usuarioSesiones.getTipo() );
						usuarioSesiones.setUsername(us.getUsername());
						System.out.println("fecha" + usuarioSesiones.getUsername() );
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
