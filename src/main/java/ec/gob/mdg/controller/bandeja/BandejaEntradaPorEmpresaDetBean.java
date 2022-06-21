package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
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
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaPorEmpresaDetBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	@Inject
	private IEmpresaService serviceEmpresa;

	private List<BandejaEntrada> listaBandejaEntrada = new ArrayList<>();
	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();
	private Empresa empresa = new Empresa();

	String siglasTramite;
	String empresaS;
	Integer empresaId;

	Date fecha_fin;
	Date fecha_inicio;

	@PostConstruct
	public void init() {
		cargaEmpresa();
//		SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
//		siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
//		banTipoTramite = serviceBanTipoTramite.muestraPorSiglas(siglasTramite);
//		try {
//			fecha_inicio = formato.parse(
//					(String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaInicio"));
//			fecha_fin = formato
//					.parse((String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaFin"));
//			cargarDatos();
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

//	public String getParam() {
//		return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
//	}

	public void cargaEmpresa() {
		
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	
		if (empresaS != null) {
			empresaId = Integer.parseInt(empresaS);
			if (empresaId != null) {
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
			}
		}
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {

		if (fecha_inicio != null && fecha_fin != null) {
			listaBandejaEntrada = serviceBandejaEntrada.listarPorEmpresaTramite(empresa, siglasTramite, fecha_inicio,
					fecha_fin);

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Sin datos", "Error"));
		}
	}

	public void onRowSelect(SelectEvent<BandejaEntrada> event) throws Exception {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("id_bandeja", ((BandejaEntrada) event.getObject()).getNum_tramite());
		Utilitario.irAPagina("/pg/cal/entprincipalcal");
	}

	public void onRowUnselect(UnselectEvent<BandejaEntrada> event) {
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("id_bandeja", ((BandejaEntrada) event.getObject()).getNum_tramite());
		Utilitario.irAPagina("/pg/cal/entprincipalcal");
	}

	//// Regresar a bandeja de estados
	public void regresarBandejaEntrada() {
		Utilitario.irAPagina("/pg/ban/bandejaentradausuario");
	}

}
