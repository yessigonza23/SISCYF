package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class BandejaEntradaCalificacionesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanCatalogoEstadosService serviceBanCatalogoEstados;

	private List<BanCatalogoEstados> listaEstados = new ArrayList<>();
	private BanCatalogoEstados banCatalogoEstados = new BanCatalogoEstados();

	Date fecha_inicio;
	Date fecha_fin;
	String siglasTramite;
	String siglasEstado;

	@PostConstruct
	public void init() {
		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
		listaEstados = serviceBanCatalogoEstados.listarEstadosPorTramite(siglasTramite);
	}

	public void onRowSelect(SelectEvent<BanCatalogoEstados> event) throws Exception {
		siglasEstado = ((BanCatalogoEstados) event.getObject()).getSiglas();
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("estado", siglasEstado);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	public void onRowUnselect(UnselectEvent<BanCatalogoEstados> event) {
		siglasEstado = ((BanCatalogoEstados) event.getObject()).getSiglas();
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("estado", siglasEstado);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	/// Regresar
	public void regresarBandeja() {
		Utilitario.irAPagina("/pg/ban/bandejaentrada");
	}

}
