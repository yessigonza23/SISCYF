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
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import ec.gob.mdg.utils.UtilsArchivos;
import lombok.Data;

@Data
@Named
@ViewScoped
public class BandejaEntradaUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	private List<BanTipoTramite> listaTramites = new ArrayList<BanTipoTramite>();

	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	String siglasTramite;
	Date fecha_inicio;
	Date fecha_fin;
	Integer num_meses = 0;

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	@PostConstruct
	public void init() {

	}

	public void listarTramites() {
		if (fecha_inicio!= null &&  fecha_fin!=null) {
			num_meses = UtilsArchivos.calcularMesesAFecha(fecha_inicio, fecha_fin);
			System.out.println("num meses " + num_meses);
			
			if(num_meses>3) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
						"El periodo de tiempo es hasta 3 meses ", "Aviso"));
			}else {
				this.listaTramites = serviceBanTipoTramite.listarTramitesUsuario(usuario, fecha_inicio, fecha_fin);
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
		flash.put("tramite", ((BanTipoTramite) event.getObject()).getSiglas());
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondetusuarios");

	}

	public void onRowUnselect(UnselectEvent<BanTipoTramite> event) {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);

		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("tramite", ((BanTipoTramite) event.getObject()).getSiglas());
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondetusuarios");
	}

}
