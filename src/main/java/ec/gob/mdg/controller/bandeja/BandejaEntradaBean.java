package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	private List<BanTipoTramite> listaTramites = new ArrayList<BanTipoTramite>();
	
	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	Usuario u = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	String siglasTramite;

	@PostConstruct
	public void init() {
		listarTramites();
	}

	public void listarTramites() {
		try {
			this.listaTramites = serviceBanTipoTramite.listarTramites();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void onRowSelect(SelectEvent<BanTipoTramite> event) throws Exception {
		siglasTramite = ((BanTipoTramite) event.getObject()).getSiglas();
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("tramite", siglasTramite);
		if (siglasTramite.equals("C")) {
			Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificacion");
		}

	}

	public void onRowUnselect(UnselectEvent<BanTipoTramite> event) {
		siglasTramite = ((BanTipoTramite) event.getObject()).getSiglas();
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("tramite", siglasTramite);
		if (siglasTramite.equals("C")) {
			Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificacion");
		}
	}

}
