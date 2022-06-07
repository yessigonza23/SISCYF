package ec.gob.mdg.controller.parametros;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.Continente;
import ec.gob.mdg.control.ejb.modelo.Pais;
import ec.gob.mdg.control.ejb.service.IContinenteService;
import ec.gob.mdg.control.ejb.service.IPaisService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class PaisBean implements Serializable {

	private static final long serialVersionUID = -2622304613361080515L;

	@Inject
	private IContinenteService serviceContinente;

	@Inject
	private IPaisService servicePais;
	
	private List<Continente> listaContinente = new ArrayList<>();
	private List<Pais> listaPais = new ArrayList<Pais>();

	private Continente continente = new Continente();
	private Pais pais = new Pais();

	private String tipoDialog = null;

	@PostConstruct
	public void init() {
		listarPais();
		listarContinente();
		this.tipoDialog = "Nuevo";
	}

	public void listarPais() {
		try {
			this.listaPais = this.servicePais.listar();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
				this.servicePais.registrar(pais);
			} else if (accion.equalsIgnoreCase("M")) {
				this.servicePais.modificar(pais);
			}
			this.listarPais();
		} catch (Exception e) {
			// System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void mostrarData(Pais i) {
		this.pais = i;
		this.tipoDialog = "Modificar Pais";
	}

	public void limpiarControles() {
		this.pais = new Pais();
		this.tipoDialog = "Nuevo Pais";
	}
	
	
}
