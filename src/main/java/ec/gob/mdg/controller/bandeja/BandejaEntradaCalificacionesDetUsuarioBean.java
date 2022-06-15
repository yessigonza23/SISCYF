package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaCalificacionesDetUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;
	
	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	private List<BandejaEntrada> listaBandejaEntrada = new ArrayList<>();
	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	String siglasTramite;
	String empresaS;

	Date fecha_fin;
	Date fecha_inicio;

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	@PostConstruct
	public void init() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
		banTipoTramite=serviceBanTipoTramite.muestraPorSiglas(siglasTramite);
		try {
			fecha_inicio = formato.parse(
					(String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaInicio"));
			fecha_fin = formato
					.parse((String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaFin"));
			cargarDatos();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		
		if (fecha_inicio!=null && fecha_fin !=null) {
			listaBandejaEntrada = serviceBandejaEntrada.listarPorTipoTramiteUsuario(usuario, siglasTramite, fecha_inicio,
					fecha_fin);
		}else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sin datos", "Error"));
		}
	}

	public void onRowSelect(SelectEvent<BandejaEntrada> event) throws Exception {
		if (((BandejaEntrada) event.getObject()).getBanCatalogoEstados().getSiglas().equals("R")) {
			empresaS = String.valueOf(((BandejaEntrada) event.getObject()).getEmpresa().getId());
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			String fecha_inicioS = dateFormat.format(fecha_inicio);
			String fecha_finS = dateFormat.format(fecha_fin);
			
			
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", empresaS);
			flash.put("tramite", ((BandejaEntrada) event.getObject()).getBanTipoTramite().getSiglas());
			flash.put("fechaInicio", fecha_inicioS);
			flash.put("fechaFin", fecha_finS);
			
			if (((BandejaEntrada) event.getObject()).getBanTipoTramite().getSiglas().equals("C")) {
				Utilitario.irAPagina("/pg/cal/entprincipalcal");
			}
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "El trámite aun no ha sido procesado ", "Aviso, "));
		}
	}

	public void onRowUnselect(UnselectEvent<BandejaEntrada> event) {
		if (((BandejaEntrada) event.getObject()).getBanCatalogoEstados().getSiglas().equals("R")) {
			empresaS = String.valueOf(((BandejaEntrada) event.getObject()).getEmpresa().getId());
			final FacesContext context = FacesContext.getCurrentInstance();
			final Flash flash = context.getExternalContext().getFlash();
			flash.put("empresa", empresaS);
			
			if (((BandejaEntrada) event.getObject()).getBanTipoTramite().getSiglas().equals("C")) {
				Utilitario.irAPagina("/pg/cal/entprincipalcal");
			}
			
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "El trámite aun no ha sido procesado ", "Aviso, "));
		}
	}


	//// Regresar a bandeja de estados
	public void regresarBandejaEntrada() {		
		Utilitario.irAPagina("/pg/ban/bandejaentradausuario");
	}

}
