package ec.gob.mdg.controller.bandeja;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBandejaEntradaService;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaBean implements Serializable{

	private static final long serialVersionUID = 1L;

	@Inject
	private IBandejaEntradaService serviceBandejaEntrada;
		
	@SuppressWarnings("unused")
	private List<BandejaEntrada> lista=new ArrayList<BandejaEntrada>();
	private BandejaEntrada bandejaEntrada= new BandejaEntrada();
	Usuario u = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");
	
//	public List<BandejaEntrada> getLista() {
//		if (u!=null) {
//			return lista= this.serviceBandejaEntrada.listarTramitesEstado(u);
//		}else {
//			return null;
//		}
//		
//	}



}
