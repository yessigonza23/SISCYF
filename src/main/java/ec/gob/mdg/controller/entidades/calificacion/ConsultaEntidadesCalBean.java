package ec.gob.mdg.controller.entidades.calificacion;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import ec.gob.mdg.control.ejb.modelo.BanTipoTramite;
import ec.gob.mdg.control.ejb.modelo.BandejaEntrada;
import ec.gob.mdg.control.ejb.modelo.Empresa;
import ec.gob.mdg.control.ejb.service.IBanTipoTramiteService;
import ec.gob.mdg.control.ejb.service.IEmpresaService;
import ec.gob.mdg.control.ejb.utils.Utilitario;
import lombok.Data;

@Data
@Named
@ViewScoped
public class ConsultaEntidadesCalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private IEmpresaService serviceEmpresa;
	
	@Inject
	private IBanTipoTramiteService serviceBanTipoTramite;

	private Empresa empresa = new Empresa();
	private BandejaEntrada bandejaEntrada = new BandejaEntrada();
	private BanTipoTramite banTipoTramite = new BanTipoTramite();

	String empresaS;
	Integer empresaId;
	Boolean render_n = false;
	Boolean render_j = false;
	Boolean render_o = false;
	Boolean render_p = false;
	Boolean render = false;
	String siglasTramite;
	Date fecha_fin;
	Date fecha_inicio;
	
	@PostConstruct
	public void init() {
		try {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			siglasTramite = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("tramite");
			banTipoTramite=serviceBanTipoTramite.muestraPorSiglas(siglasTramite);
			try {
				fecha_inicio = formato.parse(
						(String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaInicio"));
				fecha_fin = formato
						.parse((String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("fechaFin"));
				cargarDatos();
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			cargarDatos();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getParam() {
		return (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
	}

	/// DATOS DE LA EMPRESA DATOS GENERALES PRIMERA PESTAÑA
	public void cargarDatos() {
		render_n = false;
		render_o = false;
		render_j = false;
		render_p = false;
		render = false;
		if (empresa != null) {
			empresaS = (String) FacesContext.getCurrentInstance().getExternalContext().getFlash().get("empresa");
		}
		if (empresaS != null) {
			empresaId = Integer.parseInt(empresaS);
			if (empresaId != null) {
				render = true;
				empresa = serviceEmpresa.listarEmpresaPorId(empresaId);
				if (empresa.getTipo_empresa().equals("n")) {
					render_n = true;
				} else if (empresa.getTipo_empresa().equals("j")) {
					render_j = true;
				} else if (empresa.getTipo_empresa().equals("o")) {
					render_o = true;
				} else if (empresa.getTipo_empresa().equals("p")) {
					render_p = true;
				}
			}
		}		
		
	}

	/// Ir a detalle calificaciones
	public void irCalRen() {
		empresaS = String.valueOf(empresa.getId());
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", empresaS);
		Utilitario.irAPagina("/pg/cal/calrenconsultacal");
	}
	
	/// Ir a representantes
	public void irRepresentantes() {		
		empresaS = String.valueOf(empresa.getId());
		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("empresa", empresaS);
		Utilitario.irAPagina("/pg/cal/representantescal");
	}
	
	public void regresar() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		String fecha_inicioS = dateFormat.format(fecha_inicio);
		String fecha_finS = dateFormat.format(fecha_fin);

		final FacesContext context = FacesContext.getCurrentInstance();
		final Flash flash = context.getExternalContext().getFlash();
		flash.put("tramite", siglasTramite);
		flash.put("fechaInicio", fecha_inicioS);
		flash.put("fechaFin", fecha_finS);

		Utilitario.irAPagina("/pg/ban/bandejaentradaestcalificaciondetusuarios");
	}

}
