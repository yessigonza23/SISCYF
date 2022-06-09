package ec.gob.mdg.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.mindrot.jbcrypt.BCrypt;

import ec.gob.mdg.control.ejb.modelo.Coordinacion;
import ec.gob.mdg.control.ejb.modelo.Correo;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConUsuario;
import ec.gob.mdg.control.ejb.service.ICoordinacionService;
import ec.gob.mdg.control.ejb.service.ICorreoService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import ec.gob.mdg.control.ejb.utils.CedulaRuc;
import ec.gob.mdg.dinardap.modelo.RegistroCivilCedulaDTO;
import ec.gob.mdg.dinardap.servicios.ServiciosWeb;
import lombok.Data;

@Data
@Named
@ViewScoped
public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = -6912327508466146927L;

	@Inject
	private IUsuarioService serviceUsuario;
	
	@Inject
	private OperacionesConUsuario serviceOpUsuario;
	
	@Inject
	private ICorreoService serviceCorreo;
	
	@Inject
	private ICoordinacionService serviceCoordinación;

	private Correo correo;
	private Usuario usuario;
	private List<Coordinacion> listaCoordinacion = new ArrayList<Coordinacion>();
	
	RegistroCivilCedulaDTO usuarioRegCivil = new RegistroCivilCedulaDTO();
	boolean estadeshabilitado;
	boolean estadeshabilitado_ap = true;
	Integer idusuario = 0;
	boolean valida = false;
	String claveNueva = null;
	boolean validador;
	boolean render = false;

	@PostConstruct
	public void init() {
		this.usuario = new Usuario();
		// usuario.setUsername("1716925050");
		render = false;
		claveNueva = generarSecuenciaAleatoria();
		desactivarAP();
		listarCoordinacion();
		// consultaIdentificacion();
	}

	/// Consultar por cédula en el WS de Registro Civil
	
	public void consultaIdentificacion(String id) {
		if (id.length() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Ingrese el usuario", "Datos incompletos"));				
		} else {
			boolean validaI =validaIdentificacion(id);
			
			if (validaI == false) {
				usuarioRegCivil = ServiciosWeb.consultarCiudadanoRegistroCivil(id);
				if (!(usuarioRegCivil.getNombre() == null)) {
					usuario.setNombre(usuarioRegCivil.getNombre());
					render = true;				
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Usuario correcto", "Continuar"));
				}
			}else if (validaI == true) {
				render = false;
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Tipo de identificacion erronea", "Error"));
			}
		}
	}
	
	public void listarCoordinacion() {
		try {
			this.listaCoordinacion = this.serviceCoordinación.listar();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	//VALIDA SI EXISTE EL USUARIO
		public boolean validaCedula(String cedula) {
			valida=serviceOpUsuario.validaUsuarioCedula(cedula);

			if(valida==true) {
				estadeshabilitado=true;
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Este usuario ya ha sido ingresado", "ERROR"));
			}
			return valida;
		}

	// Registrar usuario
	public void registrar() {
		try {
			String clave = claveNueva;// this.usuario.getContrasena();
			String claveHash = BCrypt.hashpw(clave, BCrypt.gensalt());
			usuario.setNombre(usuario.getNombre().toUpperCase());
			usuario.setCargo(usuario.getCargo().toUpperCase());
			usuario.setTitulo(usuario.getTitulo().toUpperCase());
			usuario.setPassword(claveHash);
			usuario.setEstado("A");
			usuario.setTipo_interno_externo("I");
			usuario.setEnabled(true);
			usuario.setReseteaClave(false);
			usuario.setFecha_registro(LocalDateTime.now());
			usuario.setCantidadDeEntradas(1);
			usuario.setTipoCertificado(usuario.getTipoCertificado());
			this.serviceUsuario.registrar(usuario);

			estadeshabilitado = true;
			valida = true;
			activarAP();
			enviarContrasenia(usuario.getUsername(), claveNueva);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// VALIDADOR DE CEDULA-RUC
	public boolean  validaIdentificacion(String id) {
      if(id.length()==10) {
		
		String validaIdentificacion = CedulaRuc.comprobacion(id);
		if (validaIdentificacion.equals("T")) {
			validador = false;
		} else {
			validador = true;
		}
      }else {
    	  validador = true;
      }
      return validador;
	}

	// NUEVO REGISTR0
	public Boolean nuevo() {
		estadeshabilitado = false;
		return estadeshabilitado;
	}

	//// ACTIVAR EL BOTON DE ASIGNAR PUNTO
	public Boolean activarAP() {
		estadeshabilitado_ap = false;
		return estadeshabilitado_ap;
	}

	//// DESACTIVAR EL BOTON DE ASIGNAR PUNTO
	public Boolean desactivarAP() {
		estadeshabilitado_ap = true;
		return estadeshabilitado_ap;
	}

	public static String generarSecuenciaAleatoria() {
		String sec = "";
		do {
			if (generarNumeroAleatorios(0, 1) == 0)
				sec += generarNumeroAleatorios(0, 9);
			else
				sec += (char) generarNumeroAleatorios(65, 90);
		} while (sec.length() < 10);
		return sec;
	}

	public static int generarNumeroAleatorios(int minimo, int maximo) {
		return (int) Math.floor(Math.random() * (maximo - minimo + 1) + (minimo));
	}

	public void enviarContrasenia(String cedula, String clave) {

		eviarCorreo(cedula, clave);
	}

	public boolean eviarCorreo(String ci, String clave) {

		correo = serviceCorreo.obtenerDatosCorreo();
		Properties props = System.getProperties();
		props.put("mail.smtp.host", correo.getMailSmtpHost()); 		
		props.put("mail.smtp.user", correo.getMailEmisor());
		props.put("mail.smtp.clave", correo.getMailPasswordEmisor());
		props.put("mail.smtp.auth", correo.getMailSmtpAuth()); 
		props.put("mail.smtp.starttls.enable", correo.getMailSmtpStartTlsEnable()); 
		props.put("mail.smtp.port", correo.getMailSmtpPort());
		props.put("mail.smtp.ssl.trust", correo.getMailSmtpSslTrust());
		String asuntoMensaje = "MDG - Sistema SISCYF - Credenciales del Usuario " + usuario.getNombre() + " "
				+ usuario.getNombre();

		String cuerpoMensaje = "<html><head><title></title></head><body>" + "Estimado(a) Usuario (a): "
				+ usuario.getNombre();

		cuerpoMensaje += "<br><br>Le informamos que se ha realizado el registro de su usuario para el Sistema SISCYF, su usuario es:  "
				+ usuario.getUsername() + ", con la clave: " + clave + "<br><br>Atentamente,<br>"
				+ "MDG - Sistema SISCYF <br><br>" + "</body></html>";

		Session session = Session.getInstance(props, null);

		try {
			MimeBodyPart textoMensaje = new MimeBodyPart();
			textoMensaje.setContent(cuerpoMensaje, "text/html");

			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(textoMensaje);
			
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(correo.getMailEmisor(), "Ministerio de Gobierno"));			
			message.addRecipients(Message.RecipientType.TO, usuario.getCorreo_electronico());			
			message.setSubject(asuntoMensaje);			
			message.setContent(multiParte);
			
			Transport transport = session.getTransport("smtp");			
			transport.connect(correo.getMailSmtpHost(), correo.getMailEmisor(), correo.getMailPasswordEmisor());			
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();			
			
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Usuario registrado y se remite las credenciales al email registrado", "Aviso"));
			return true;
		} catch (Exception e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: No fue enviado el correo", "ERROR"));
			return false;
		}
	}

}
