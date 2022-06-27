package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaPorEmpresaTramitesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;
	
	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;

	@Inject
	private IEmpresaService serviceEmpresa;

	private List<BanTipoTramite> listaTramites = new ArrayList<BanTipoTramite>();
	private List<BandejaEntrada> listaBandeja = new ArrayList<>();
	private List<BandejaEntrada> listaBandejaDetalle = new ArrayList<>();
	
	private BanTipoTramite banTipoTramite = new BanTipoTramite();
	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
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
		cargarDatos();
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("empresa");
	}

	public void cargaEmpresa() {

		empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");

		if (empresaS != null) {
			empresaId = Integer.parseInt(empresaS);
			if (empresaId != null) {
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
			}
		}
	}

	public void cargarDatos() {
		this.listaTramites = serviceBanTipoTramite.listarTramitesPorEmpresa(empresa);
	}

	public void onRowSelect(SelectEvent<BanTipoTramite> event) throws Exception {
		this.listaBandeja = serviceBandejaEntrada.listarPorEmpresaTipoTramite(empresa, ((BanTipoTramite) event.getObject()).getSiglas());
	}

	public void onRowUnselect(UnselectEvent<BanTipoTramite> event) {
		this.listaBandeja = serviceBandejaEntrada.listarPorEmpresaTipoTramite(empresa, ((BanTipoTramite) event.getObject()).getSiglas());
	}
	
	public void onRowSelectTramite(SelectEvent<BandejaEntrada> event) throws Exception {
		 mostrarData(((BandejaEntrada) event.getObject()));		
		this.listaBandejaDetalle = serviceBandejaEntrada.listarEmpresaTramiteTodos(empresa, ((BandejaEntrada) event.getObject()).getNum_tramite());
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('wdgTra').show();");	
	}

	public void onRowUnselectTramite(UnselectEvent<BandejaEntrada> event) {
		mostrarData(((BandejaEntrada) event.getObject()));
		this.listaBandejaDetalle = serviceBandejaEntrada.listarEmpresaTramiteTodos(empresa, ((BandejaEntrada) event.getObject()).getNum_tramite());
		PrimeFaces current = PrimeFaces.current();
		current.executeScript("PF('wdgTra').show();");	
	}
	
	public void mostrarData(BandejaEntrada i) {
		this.bandejaEntrada = i;
	}

}
