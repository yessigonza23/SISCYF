package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Sustancias;
import ec.gob.mdg.control.ejb.modelo.TipoSustancia;
import ec.gob.mdg.control.ejb.service.ISustanciasService;
import ec.gob.mdg.control.ejb.service.ITipoSustanciaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class SustanciasBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private ISustanciasService serviceSustancias;
	
	@Inject
	private ITipoSustanciaService serviceTipoSustancia;

	private List<Sustancias> listaSustancias = new ArrayList<>();
	private List<TipoSustancia> listaTipoSustancia = new ArrayList<>();

	private TipoSustancia tipoSustancia = new TipoSustancia();
	private Sustancias sustancias = new Sustancias();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarSustancias();
		listarTipoSustancia();
		this.tipoDialog = "Nuevo";
	}

	public void listarSustancias() {
		try {
			this.listaSustancias = this.serviceSustancias.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
				this.serviceSustancias.registrar(sustancias);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceSustancias.modificar(sustancias);
			}
			this.listarSustancias();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Sustancias i) {
		this.sustancias = i;
		this.tipoDialog = "Modificar Sustancias";
	}

	public void limpiarControles() {
		this.sustancias = new Sustancias();
		this.tipoDialog = "Nueva Sustancia";
	}
	
}
