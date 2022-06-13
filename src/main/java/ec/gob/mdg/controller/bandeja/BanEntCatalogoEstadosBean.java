package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstados;
import ec.gob.mdg.control.ejb.service.IBanCatalogoEstadosService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BanEntCatalogoEstadosBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanCatalogoEstadosService serviceBanTipTramite;
	
	private List<BanCatalogoEstados> listaBanCatalogoEstados = new ArrayList<BanCatalogoEstados>();
	
	private BanCatalogoEstados banCatalogoEstados = new BanCatalogoEstados();
	
	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarTipoTrámite();
		this.tipoDialog = "Nuevo";
	}

	public void listarTipoTrámite() {
		try {
			this.listaBanCatalogoEstados = this.serviceBanTipTramite.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceBanTipTramite.registrar(banCatalogoEstados);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceBanTipTramite.modificar(banCatalogoEstados);
			}
			this.listarTipoTrámite();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(BanCatalogoEstados i) {
		this.banCatalogoEstados = i;
		this.tipoDialog = "Modificar Catálogo";
	}

	public void limpiarControles() {
		this.banCatalogoEstados = new BanCatalogoEstados();
		this.tipoDialog = "Nuevo Catálogo";
	}


}
