package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.TipoSustancia;
import ec.gob.mdg.control.ejb.service.ITipoSustanciaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class TipoSustanciaBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private ITipoSustanciaService serviceTipoSustancia;

	private List<TipoSustancia> listaTipoSustancia = new ArrayList<>();

	private TipoSustancia tipoSustancia = new TipoSustancia();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarTipoSustancia();
		this.tipoDialog = "Nuevo";
	}

	public void listarTipoSustancia() {
		try {
			this.listaTipoSustancia = this.serviceTipoSustancia.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceTipoSustancia.registrar(tipoSustancia);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceTipoSustancia.modificar(tipoSustancia);
			}
			this.listarTipoSustancia();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(TipoSustancia i) {
		this.tipoSustancia = i;
		this.tipoDialog = "Modificar Tipo Sustancia";
	}

	public void limpiarControles() {
		this.tipoSustancia = new TipoSustancia();
		this.tipoDialog = "Nuevo Tipo Sustancia";
	}
	
}
