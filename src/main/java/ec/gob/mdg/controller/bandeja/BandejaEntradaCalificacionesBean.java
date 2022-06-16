package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaCalificacionesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	@Inject
	private IBanCatalogoEstadosService serviceBanCatalogoEstados;

	private List<BanCatalogoEstados> listaEstados = new ArrayList<>();
	private BanCatalogoEstados banCatalogoEstados = new BanCatalogoEstados();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	Date fecha_inicio;
	Date fecha_fin;
	String siglasTramite;
	String siglasEstado;
	String renderAsigna = null;

	@PostConstruct
	public void init() {
		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
		siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
		banTipoTramite = serviceBanTipoTramite.muestraPorSiglas(siglasTramite);
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
		if (siglasTramite!=null && usuario!=null && fecha_inicio!=null && fecha_fin!=null) {
			listaEstados = serviceBanCatalogoEstados.listarEstadosPorTramite(siglasTramite, usuario, fecha_inicio,
					fecha_fin);
		}
		
	}

	public void onRowSelect(SelectEvent<BanCatalogoEstados> event) throws Exception {
		if (((BanCatalogoEstados) event.getObject()).getSiglas().equals("E")) {
			renderAsigna = "T";
		} else {
			renderAsigna = "F";
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);

		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("est_siglas", ((BanCatalogoEstados) event.getObject()).getSiglas());
		flash.put("tramite", siglasTramite);
		flash.put("renderAsigna", renderAsigna);
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	public void onRowUnselect(UnselectEvent<BanCatalogoEstados> event) {
		if (((BanCatalogoEstados) event.getObject()).getSiglas().equals("E")) {
			renderAsigna = "T";
		} else {
			renderAsigna = "F";
		}
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);

		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("est_siglas", ((BanCatalogoEstados) event.getObject()).getSiglas());
		flash.put("tramite", siglasTramite);
		flash.put("renderAsigna", renderAsigna);
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");

	}

	/// Regresar
	public void regresarBandeja() {
		Utilitario.irAPagina("/pg/ban/bandejaentrada");
	}

}
