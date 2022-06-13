package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@SessionScoped
public class BandejaEntradaCalificacionesDetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;

	private List<BandejaEntrada> listaBandejaEntrada = new ArrayList<>();
	private BandejaEntrada bandejaEntrada= new BandejaEntrada();

	String siglasTramite="C";
	String siglasEstado;
	Date fecha_inicio;
	Date fecha_fin;

	

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("estado");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		siglasTramite="C";
		siglasEstado = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("estado");
		listaBandejaEntrada = serviceBandejaEntrada.listarPorEstado(siglasTramite, siglasEstado, fecha_inicio, fecha_fin);
	}

	public void onRowSelect(SelectEvent<BandejaEntrada> event) throws Exception {
//		siglasEstado = ((BandejaEntrada) event.getObject()).getSiglas();
//		final FacesContext context = FacesContext.getCurrentInstance();
//		final Flash flash = context.getExternalContext().getFlash();
//		flash.put("estado", siglasEstado);
//
//		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	public void onRowUnselect(UnselectEvent<BanCatalogoEstados> event) {
//		siglasEstado = ((BanCatalogoEstados) event.getObject()).getSiglas();
//		final FacesContext context = FacesContext.getCurrentInstance();
//		final Flash flash = context.getExternalContext().getFlash();
//		flash.put("estado", siglasEstado);
//
//		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondet");
	}

	/// Regresar
	public void regresarBandeja() {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("estado", siglasEstado);
		Utilitario.irAPagina("/pg/ban/bandejaentrada");
	}

}
