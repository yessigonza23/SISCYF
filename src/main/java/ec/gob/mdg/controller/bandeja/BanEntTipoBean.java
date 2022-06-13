package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BanEntTipoBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanTipoTramiteService serviceBanTipTramite;
	
	private List<BanTipoTramite> listaBanTipoTramite = new ArrayList<BanTipoTramite>();
	
	private BanTipoTramite banTipoTramite = new BanTipoTramite();
	
	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarTipoTrámite();
		this.tipoDialog = "Nuevo";
	}

	public void listarTipoTrámite() {
		try {
			this.listaBanTipoTramite = this.serviceBanTipTramite.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceBanTipTramite.registrar(banTipoTramite);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceBanTipTramite.modificar(banTipoTramite);
			}
			this.listarTipoTrámite();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(BanTipoTramite i) {
		this.banTipoTramite = i;
		this.tipoDialog = "Modificar Tipo de Trámite";
	}

	public void limpiarControles() {
		this.banTipoTramite = new BanTipoTramite();
		this.tipoDialog = "Nuevo Tipo de Trámite";
	}


}
