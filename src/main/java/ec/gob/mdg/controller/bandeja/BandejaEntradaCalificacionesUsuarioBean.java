package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
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
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaCalificacionesUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanCatalogoEstadosService serviceBanCatalogoEstados;

	private List<BanCatalogoEstados> listaEstados = new ArrayList<>();
	private BanCatalogoEstados banCatalogoEstados = new BanCatalogoEstados();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	Date fecha_inicio;
	Date fecha_fin;
	String siglasTramite;
	String siglasEstado;

	@PostConstruct
	public void init() {
		cargarDatos();
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {	
		listaEstados = serviceBanCatalogoEstados.listarEstadosPorTramite((String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite"));
		
	}

	public void onRowSelect(SelectEvent<BanCatalogoEstados> event) throws Exception {		
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("est_siglas", ((BanCatalogoEstados) event.getObject()).getSiglas());
		
		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	public void onRowUnselect(UnselectEvent<BanCatalogoEstados> event) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("est_siglas", ((BanCatalogoEstados) event.getObject()).getSiglas());
		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	/// Regresar
	public void regresarBandeja() {
		Utilitario.irAPagina("/pg/ban/bandejaentrada");
	}

}
