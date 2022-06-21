package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.text.DateFormat;
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
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.utils.UtilsArchivos;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaPorEmpresaTramitesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;
	
	@Inject
	private IEmpresaService serviceEmpresa;

	private List<BanTipoTramite> listaTramites = new ArrayList<BanTipoTramite>();

	private BanTipoTramite banTipoTramite = new BanTipoTramite();
	private Empresa empresa = new Empresa();

	String siglasTramite;
	String empresaS;
	Integer empresaId;
	Date fecha_inicio;
	Date fecha_fin;
	Integer num_meses = 0;
	String codigo_emp;
	

	@PostConstruct
	public void init() {
		cargaEmpresa();
	}
	
	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
	}
	
	public void cargaEmpresa() {
		if (empresa != null) {
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
		}
		if (empresaS != null) {
			empresaId = Integer.parseInt(empresaS);
			if (empresaId != null) {
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
			}
		}		
	}	
	
	public void cargarDatos() {
		if (fecha_inicio!= null &&  fecha_fin!=null) {
			num_meses = UtilsArchivos.calcularMesesAFecha(fecha_inicio, fecha_fin);			
			if(num_meses>3) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"El periodo de tiempo es hasta 3 meses ", "Aviso"));
			}else {
				this.listaTramites = serviceBanTipoTramite.listarTramitesEmpresa(empresa, fecha_inicio, fecha_fin);
			}
		}else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Sin parametros", "Error"));
		}
	}

	public void onRowSelect(SelectEvent<BanTipoTramite> event) throws Exception {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);

		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", empresa.getId());
		flash.put("tramite", ((BanTipoTramite) event.getObject()).getSiglas());
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaporempresadet");

	}

	public void onRowUnselect(UnselectEvent<BanTipoTramite> event) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);

		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", empresa.getId());
		flash.put("tramite", ((BanTipoTramite) event.getObject()).getSiglas());
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaporempresadet");
	}

}
