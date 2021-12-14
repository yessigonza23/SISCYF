package ec.gob.mdg.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
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

import ec.gob.mdg.control.ejb.modelo.Correo;
import ec.gob.mdg.control.ejb.modelo.Usuario;
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
	private ICorreoService serviceCorreo;

	private Correo correo;
	private Usuario usuario;
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
		claveNueva = generarSecuenciaAleatoria();
		desactivarAP();
		// consultaIdentificacion();
	}

	/// Consultar por cédula en el WS de Registro Civil
	
	public void consultaIdentificacion(String id) {
		System.out.println("entra al método " + id);
		int aInteger = id.length();
		System.out.println("tamaño " + aInteger);
		
		if (id.length() == 0) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "maaaaaaaaaaaal", "lll"));
			
			
		} else {
			System.out.println("entra ELSE " + id);
			validaIdentificacion(id);
			usuarioRegCivil = ServiciosWeb.consultarCiudadanoRegistroCivil(id);

			if (!(usuarioRegCivil.getNombre() == null)) {
				usuario.setNombre(usuarioRegCivil.getNombre());
				render = true;
				System.out.println("Nombre de la identificación" + usuarioRegCivil.getNombre());
			}
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "biennnnn", "ok"));
		}

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
			usuario.setFecha_registro(LocalDateTime.now());
			usuario.setCantidadDeEntradas(1);
			usuario.setTipoCertificado(usuario.getTipoCertificado());
			this.serviceUsuario.registrar(usuario);

			estadeshabilitado = true;
			valida = true;
			activarAP();
			System.out.println("ANTES DE ENVIAR " + usuario.getUsername() + " - " + claveNueva);
			enviarContrasenia(usuario.getUsername(), claveNueva);

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// VALIDADOR DE CEDULA-RUC
	public void validaIdentificacion(String id) {
      if(id.length()==10) {
		System.out.println("entra a valida identifciación " + id);
		String validaIdentificacion = CedulaRuc.comprobacion(id);
		System.out.println("validaIdentificacion " + validaIdentificacion);
		if (validaIdentificacion.equals("T")) {
			validador = false;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Identificación correcta", validaIdentificacion));
		} else {
			System.out.println("entra a diferente de T: " + validaIdentificacion);
			validador = true;
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Tipo de identificacion erronea", validaIdentificacion));
		}
      }else {
    	  FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Identificacion erronea", "Error"));
      }
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
		System.out.println("entra a enviar contraseña 1");
		eviarCorreo(cedula, claveNueva);
	}

	public boolean eviarCorreo(String ci, String clave) {
		System.out.println("entra a enviar correo " + ci + " - " + clave);
		
		correo = serviceCorreo.obtenerDatosCorreo();
		
		System.out.println("correo.getMailSmtpHost() " + correo.getMailSmtpHost());
		
		Properties props = System.getProperties();
		props.put("mail.smtp.host", correo.getMailSmtpHost()); // El servidor SMTP de Google
		
		props.put("mail.smtp.user", correo.getMailEmisor());
		System.out.println("correo.getMailEmisor() " + correo.getMailEmisor());
		props.put("mail.smtp.clave", correo.getMailPasswordEmisor()); // La clave de la cuenta
		System.out.println("correo.getMailPasswordEmisor() " + correo.getMailPasswordEmisor());
		props.put("mail.smtp.auth", correo.getMailSmtpAuth()); // Usar autenticacin mediante usuario y clave
		System.out.println("correo.getMailSmtpAuth() " + correo.getMailSmtpAuth());
		
		
		props.put("mail.smtp.starttls.enable", correo.getMailSmtpStartTlsEnable()); // Para conectar de manera segura al
		System.out.println("correo.getMailSmtpStartTlsEnable() " + correo.getMailSmtpStartTlsEnable());
		
		
		
		props.put("mail.smtp.port", correo.getMailSmtpPort()); // El puerto SMTP seguro de Google
		System.out.println("correo.getMailSmtpPort() " + correo.getMailSmtpPort());
		props.put("mail.smtp.ssl.trust", correo.getMailSmtpSslTrust());
		System.out.println("correo.getMailSmtpSslTrust() " + correo.getMailSmtpSslTrust());
		String asuntoMensaje = "MDG - Sistema SISCYF - Credenciales del Usuario " + usuario.getNombre() + " "
				+ usuario.getNombre();

		String cuerpoMensaje = "<html><head><title></title></head><body>" + "Estimado(a) Usuario (a): "
				+ usuario.getNombre();

		cuerpoMensaje += "<br><br>Le informamos que se ha realizado la creación de su usuario para el Sistema SISCYF, su usuario es:  "
				+ usuario.getUsername() + ", con la clave: " + clave + "<br><br>Atentamente,<br>"
				+ "MDG - Sistema SISCYF <br><br>" + "</body></html>";

		Session session = Session.getInstance(props, null);
		session.setDebug(true);
		System.out.println("PASA MENSAJE");
		try {
			MimeBodyPart textoMensaje = new MimeBodyPart();
			textoMensaje.setContent(cuerpoMensaje, "text/html");

			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(textoMensaje);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(correo.getMailEmisor(), "Ministerio de Gobierno"));
			System.out.println("correo electrónico " + correo.getMailPasswordEmisor());
			
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
