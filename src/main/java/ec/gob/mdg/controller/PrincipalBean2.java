package ec.gob.mdg.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.PieChartModel;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstadosPorTramitePorAnioDTO;
import ec.gob.mdg.control.ejb.modelo.BanTipoTramitePorAnioDTO;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConBandejaEntrada;
import ec.gob.mdg.control.ejb.utils.UtilsDate;
import lombok.Data;

@Data
@Named
@ViewScoped
public class PrincipalBean2 implements Serializable {

	private static final long serialVersionUID = 1L;	
	
	@Inject
	private OperacionesConBandejaEntrada serviceOpeBandejaEntrada;

	private List<BanTipoTramitePorAnioDTO> listaBanTipoTramite = new ArrayList<>();
	private List<BanCatalogoEstadosPorTramitePorAnioDTO> listaBanCatEstCal= new ArrayList<>();

	Usuario us = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	Date fechaActual;
	private PieChartModel pieModel;
	private PieChartModel pieModelCal;

	@PostConstruct
	public void init() {
		try {
			fechaActual = UtilsDate.timestamp();
			listarTramites() ;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void listarTramites() {
		SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");		
		Integer anio=Integer.parseInt(getYearFormat.format(fechaActual));
		listaBanTipoTramite=serviceOpeBandejaEntrada.listarTramitesPorAnio(anio);
		graficar(listaBanTipoTramite);
	String tramite ="C";
		listaBanCatEstCal = serviceOpeBandejaEntrada.listarTramitesPorEstadoPorAnio(anio, tramite);
		graficarCal(listaBanCatEstCal);

	}
	public void graficar(List<BanTipoTramitePorAnioDTO> lista) {
		pieModel = new PieChartModel();
		
		for(BanTipoTramitePorAnioDTO b:lista) {
			pieModel.set(b.getDescripcion(), b.getValor());
		}
		
		pieModel.setTitle("TRAMITES ATENDIDOS");
		pieModel.setLegendPosition("legend");
		pieModel.setFill(false);
		pieModel.setShowDataLabels(true);
		pieModel.setDiameter(150);
		
	}
	
	public void graficarCal(List<BanCatalogoEstadosPorTramitePorAnioDTO> lista) {
		pieModelCal = new PieChartModel();
		
		for(BanCatalogoEstadosPorTramitePorAnioDTO b:lista) {
			pieModelCal.set(b.getDescripcion(), b.getValor());
		}
		
		pieModelCal.setTitle("TRAMITE:CALIFICACION POR ESTADOS");
		pieModelCal.setLegendPosition("legend");
		pieModelCal.setFill(false);
		pieModelCal.setShowDataLabels(true);
		pieModelCal.setDiameter(150);
		
	}

}
