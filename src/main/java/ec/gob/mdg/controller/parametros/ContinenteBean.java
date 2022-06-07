package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Continente;
import ec.gob.mdg.control.ejb.service.IContinenteService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ContinenteBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IContinenteService serviceContinente;

	private List<Continente> listaContinente = new ArrayList<Continente>();

	private Continente continente = new Continente();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarContinente();
		this.tipoDialog = "Nuevo";
	}

	public void listarContinente() {
		try {
			this.listaContinente = this.serviceContinente.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void operar(String accion) {
		try {
			if (accion.equalsIgnoreCase("R")) {
				this.serviceContinente.registrar(continente);
			} else if (accion.equalsIgnoreCase("M")) {
				this.serviceContinente.modificar(continente);
			}
			this.listarContinente();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Continente i) {
		this.continente = i;
		this.tipoDialog = "Modificar Continente";
	}

	public void limpiarControles() {
		this.continente = new Continente();
		this.tipoDialog = "Nuevo Continente";
	}
	
}
