package ec.gob.mdg.controller;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
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
import ec.gob.mdg.control.ejb.operaciones.OperacionesConUsuario;
import ec.gob.mdg.control.ejb.service.ICorreoService;
import ec.gob.mdg.control.ejb.service.IUsuarioService;
import ec.gob.mdg.control.ejb.utils.UtilsDate;


@Named
@ViewScoped
public class OlvidoContraseniaBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IUsuarioService serviceUsuario;
	
	@Inject
	private OperacionesConUsuario serviceOpUsuario;

	@Inject
	private OperacionesConUsuario serviceOpUsuarios;
	
	@Inject
	private ICorreoService serviceCorreo;
	
	String cedula = null;
	String claveNueva = null;
	Boolean resultado = false;
	Usuario usuario;
	Date fechaActual ;
	private Correo correo;

	@PostConstruct
	public void init() {
		try {

			cedula = null;
			claveNueva = null;
			fechaActual = UtilsDate.timestamp();		
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void validarCedula(String cedula) {
		try {
			if (cedula != null) {
				resultado = serviceOpUsuarios.validaUsuarioCedula(cedula);
				if (resultado == true) {
					modificar();
					
				} else {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL,
							"No. Identificaión no encontrada", "ERROR"));
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_FATAL, "Ingrese el No. de identificación", "ERROR"));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		eviarCorreo(cedula, claveNueva);
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, "Se ha enviado la nueva clave a su correo electrónico registrado", "Cambio Exitoso"));
		modificar();
	}
	
	
	// registrar usuario
	public void modificar() {		
		usuario = serviceOpUsuario.UsuarioCedula(cedula);
		try {	
			claveNueva = generarSecuenciaAleatoria();
			String clave = claveNueva;			
			String claveHash = BCrypt.hashpw(clave, BCrypt.gensalt());
			
			usuario.setPassword(claveHash);
			usuario.setReseteaClave(true);			
			usuario.setFecha_cambio_clave(LocalDateTime.now());
			this.serviceUsuario.modificar(usuario);
			
			eviarCorreo(cedula, claveNueva);
			
			

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public boolean eviarCorreo(String ci, String clave) {

		usuario = serviceOpUsuario.UsuarioCedula(cedula);
		correo = serviceCorreo.obtenerDatosCorreo();
			
		Properties props = System.getProperties();
		props.put("mail.smtp.host", correo.getMailSmtpHost()); // El servidor SMTP de Google
		props.put("mail.smtp.user", correo.getMailEmisor());
		props.put("mail.smtp.clave", correo.getMailPasswordEmisor()); // La clave de la cuenta
		props.put("mail.smtp.auth", correo.getMailSmtpAuth()); // Usar autenticacion mediante usuario y clave
		props.put("mail.smtp.starttls.enable", correo.getMailSmtpStartTlsEnable()); // Para conectar de manera segura al servidor SMTP
		props.put("mail.smtp.port",correo.getMailSmtpPort()); // El puerto SMTP seguro de Google
		props.put("mail.smtp.ssl.trust", "*");
	
		String asuntoMensaje = "MDG - Sistema Siscyf - Recupera Password del Usuario "
				+ usuario.getNombre() ;

		String cuerpoMensaje  = "<html><head><title></title></head><body>" + "Estimado (a) "+ usuario.getNombre() ;

		cuerpoMensaje += "<br><br>Le informamos que se ha realizado el proceso de recuperaci&oacute;n de clave del Sistema Siscyf, su nueva clave es  "
		              + clave 
		              + "<br><br>Atentamente,<br>"
					  + correo.getMail_nombre_institucion() +" - Sistema SISCYF <br><br>"+ "</body></html>";
		
		Session session = Session.getInstance(props, null);
		

		try {
			MimeBodyPart textoMensaje = new MimeBodyPart();
			textoMensaje.setContent(cuerpoMensaje,"text/html");
			
			MimeMultipart multiParte = new MimeMultipart();
			multiParte.addBodyPart(textoMensaje);

			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(correo.getMailEmisor(),correo.getMail_nombre_institucion()));
			message.addRecipients(Message.RecipientType.TO, usuario.getCorreo_electronico()); 
			message.setSubject(asuntoMensaje);
			message.setContent(multiParte);
			Transport transport = session.getTransport("smtp");
			transport.connect(correo.getMailSmtpHost(), correo.getMailEmisor(),
					correo.getMailPasswordEmisor());
			transport.sendMessage(message, message.getAllRecipients());
			transport.close();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Clave nueva generada, se envía al e-mail registrado", "Aviso"));
			return true;
		} catch (Exception e) {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR: No se Envia por correo", "ERROR"));
			return false;
		}
	}

	
	
//getters & setter
	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	

	public String getClaveNueva() {
		return claveNueva;
	}

	public void setClaveNueva(String claveNueva) {
		this.claveNueva = claveNueva;
	}

	public Boolean getResultado() {
		return resultado;
	}

	public void setResultado(Boolean resultado) {
		this.resultado = resultado;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Date getFechaActual() {
		return fechaActual;
	}

	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}
	public Correo getCorreo() {
		return correo;
	}
	public void setCorreo(Correo correo) {
		this.correo = correo;
	}


	
	

}
