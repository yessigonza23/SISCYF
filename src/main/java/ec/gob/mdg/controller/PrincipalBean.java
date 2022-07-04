package ec.gob.mdg.controller;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;

import ec.gob.mdg.control.ejb.modelo.BanCatalogoEstadosPorTramitePorAnioDTO;
import ec.gob.mdg.control.ejb.modelo.BanTipoTramitePorAnioDTO;
import ec.gob.mdg.control.ejb.modelo.Usuario;
import ec.gob.mdg.control.ejb.operaciones.OperacionesConBandejaEntrada;
import ec.gob.mdg.control.ejb.utils.UtilsDate;
import lombok.Data;

@Data
@Named
@RequestScoped
public class PrincipalBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private OperacionesConBandejaEntrada serviceOpeBandejaEntrada;

	private List<BanTipoTramitePorAnioDTO> listaBanTipoTramite = new ArrayList<>();
	private BanTipoTramitePorAnioDTO banTipoTramitePorAnioDTO = new BanTipoTramitePorAnioDTO();
	private List<BanCatalogoEstadosPorTramitePorAnioDTO> listaBanCatEstCal = new ArrayList<>();

	private List<BanTipoTramitePorAnioDTO> listaBanTipoTramiteCoo = new ArrayList<>();
	private BanTipoTramitePorAnioDTO banTipoTramitePorAnioDTOCoo = new BanTipoTramitePorAnioDTO();
	private List<BanCatalogoEstadosPorTramitePorAnioDTO> listaBanCatEstCalCoo = new ArrayList<>();

	private List<BanTipoTramitePorAnioDTO> listaBanTipoTramiteUsu = new ArrayList<>();
	private BanTipoTramitePorAnioDTO banTipoTramitePorAnioDTOUsu = new BanTipoTramitePorAnioDTO();
	private List<BanCatalogoEstadosPorTramitePorAnioDTO> listaBanCatEstCalUsu = new ArrayList<>();

	PieChartModel pieModel;
	PieChartModel pieModelTram;

	PieChartModel pieModelCoo;
	PieChartModel pieModelTramCoo;

	PieChartModel pieModelUsu;
	PieChartModel pieModelTramUsu;
	Boolean renderDir;
	Boolean renderCoo;
	Boolean renderUsu;
	Date fechaActual;
	String tramite;
	Integer anio;

	Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("usuario");

	@PostConstruct
	public void init() {
		fechaActual = UtilsDate.timestamp();
		SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
		anio = Integer.parseInt(getYearFormat.format(fechaActual));
		iniciar();
	}

	public void iniciar() {
		renderDir = false;
		renderCoo = false;
		renderUsu = false;
		if (usuario.getRol().equals("D")) {
			renderDir = true;
			listarTramites();
		} else if (usuario.getRol().equals("C")) {
			renderCoo = true;
			listarTramitesCoo();
		} else if (usuario.getRol().equals("T")) {
			renderUsu = true;
			listarTramitesUsu();
		}
	}

	public void listarTramites() {
		SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
		anio = Integer.parseInt(getYearFormat.format(fechaActual));
		if (anio != null) {
			listaBanTipoTramite = serviceOpeBandejaEntrada.listarTramitesPorAnio(anio);
			createPieModelTramites(listaBanTipoTramite);

			if (listaBanTipoTramite != null && !listaBanTipoTramite.isEmpty()) {
				banTipoTramitePorAnioDTO = listaBanTipoTramite.get(0);
				if (banTipoTramitePorAnioDTO != null) {
					tramite = banTipoTramitePorAnioDTO.getSiglas();
					listaBanCatEstCal = serviceOpeBandejaEntrada.listarTramitesPorEstadoPorAnio(anio, tramite);
					createPieModel(listaBanCatEstCal);
				}
			}
		}
	}

	private void createPieModelTramites(List<BanTipoTramitePorAnioDTO> lista) {
		pieModelTram = new PieChartModel();
		ChartData data = new ChartData();
		PieChartDataSet dataSet = new PieChartDataSet();
		List<Number> values = new ArrayList<>();
		for (BanTipoTramitePorAnioDTO b : lista) {
			values.add(b.getValor());
		}
		dataSet.setData(values);
		List<String> bgColor = new ArrayList<>();
		bgColor.add("rgba(255, 99, 0, 1)");
		bgColor.add("rgba(222, 71, 0, 1)");
		bgColor.add("rgba(190, 41, 0, 1)");
		bgColor.add("rgba(157, 0, 0, 1)");
		bgColor.add("rgba(125, 0, 0, 1)");
		bgColor.add("rgba(95, 0, 0, 1)");
		bgColor.add("rgba(66, 0, 2, 1)");
		bgColor.add("rgba(35, 0, 1, 1)");
		bgColor.add("rgba(0, 0, 0, 1)");
		bgColor.add("rgba(255, 99, 0, 1)");
		dataSet.setBackgroundColor(bgColor);
		data.addChartDataSet(dataSet);
		List<String> labels = new ArrayList<>();
		for (BanTipoTramitePorAnioDTO b : lista) {
			labels.add(b.getDescripcion());
		}
		data.setLabels(labels);
		pieModelTram.setData(data);
	}

	private void createPieModel(List<BanCatalogoEstadosPorTramitePorAnioDTO> lista) {

		pieModel = new PieChartModel();
		ChartData data = new ChartData();
		PieChartDataSet dataSet = new PieChartDataSet();

		List<Number> values = new ArrayList<>();
		for (BanCatalogoEstadosPorTramitePorAnioDTO b : lista) {
			values.add(b.getValor());
		}

		dataSet.setData(values);

		List<String> bgColor = new ArrayList<>();
		bgColor.add("rgba(255, 99, 0, 1)");
		bgColor.add("rgba(222, 71, 0, 1)");
		bgColor.add("rgba(190, 41, 0, 1)");
		bgColor.add("rgba(157, 0, 0, 1)");
		bgColor.add("rgba(125, 0, 0, 1)");
		bgColor.add("rgba(95, 0, 0, 1)");
		bgColor.add("rgba(66, 0, 2, 1)");
		bgColor.add("rgba(35, 0, 1, 1)");
		bgColor.add("rgba(0, 0, 0, 1)");
		bgColor.add("rgba(255, 99, 0, 1)");
		dataSet.setBackgroundColor(bgColor);

		data.addChartDataSet(dataSet);

		List<String> labels = new ArrayList<>();
		for (BanCatalogoEstadosPorTramitePorAnioDTO b : lista) {
			labels.add(b.getDescripcion());
		}

		data.setLabels(labels);

		pieModel.setData(data);
	}

	public void onRowSelect(SelectEvent<BanTipoTramitePorAnioDTO> event) {
		if (usuario.getRol().equals("D")) {
			listaBanCatEstCal = serviceOpeBandejaEntrada.listarTramitesPorEstadoPorAnio(anio,
					((BanTipoTramitePorAnioDTO) event.getObject()).getSiglas());
			createPieModel(listaBanCatEstCal);
		} else if (usuario.getRol().equals("C")) {
			listaBanCatEstCalCoo = serviceOpeBandejaEntrada.listarTraEstAniCoor(anio,
					((BanTipoTramitePorAnioDTO) event.getObject()).getSiglas(), usuario);
			createPieModel(listaBanCatEstCalCoo);
		} else if (usuario.getRol().equals("C")) {
			listaBanCatEstCalUsu = serviceOpeBandejaEntrada.listarTraEstAniUsu(anio,
					((BanTipoTramitePorAnioDTO) event.getObject()).getSiglas(), usuario);
			createPieModel(listaBanCatEstCalUsu);
		}
	}

	public void onRowUnselect(UnselectEvent<BanTipoTramitePorAnioDTO> event) {
		if (usuario.getRol().equals("D")) {
			listaBanCatEstCal = serviceOpeBandejaEntrada.listarTramitesPorEstadoPorAnio(anio,
					((BanTipoTramitePorAnioDTO) event.getObject()).getSiglas());
			createPieModel(listaBanCatEstCal);
		} else if (usuario.getRol().equals("C")) {
			listaBanCatEstCalCoo = serviceOpeBandejaEntrada.listarTraEstAniCoor(anio,
					((BanTipoTramitePorAnioDTO) event.getObject()).getSiglas(), usuario);
			createPieModel(listaBanCatEstCalCoo);
		} else if (usuario.getRol().equals("C")) {
			listaBanCatEstCalUsu = serviceOpeBandejaEntrada.listarTraEstAniUsu(anio,
					((BanTipoTramitePorAnioDTO) event.getObject()).getSiglas(), usuario);
			createPieModel(listaBanCatEstCalUsu);
		}
	}

	///////////////// PARA COORDINACION

	public void listarTramitesCoo() {
		SimpleDateFormat getYearFormat = new SimpleDateFormat("yyyy");
		anio = Integer.parseInt(getYearFormat.format(fechaActual));
		if (anio != null) {
			listaBanTipoTramiteCoo = serviceOpeBandejaEntrada.listarTraAnioCoo(anio, usuario);
			createPieModelTramites(listaBanTipoTramiteCoo);

			if (listaBanTipoTramiteCoo != null && !listaBanTipoTramiteCoo.isEmpty()) {
				banTipoTramitePorAnioDTOCoo = listaBanTipoTramiteCoo.get(0);
				if (banTipoTramitePorAnioDTOCoo != null) {
					tramite = banTipoTramitePorAnioDTOCoo.getSiglas();
					listaBanCatEstCalCoo = serviceOpeBandejaEntrada.listarTraEstAniCoor(anio, tramite, usuario);
					createPieModel(listaBanCatEstCalCoo);
				}
			}
		}
	}

	///////////////// PARA TECNICO - USUARIO

	public void listarTramitesUsu() {
		if (anio != null) {
			listaBanTipoTramiteUsu = serviceOpeBandejaEntrada.listarTraAnioUsu(anio, usuario);
			createPieModelTramites(listaBanTipoTramiteUsu);
			if (listaBanTipoTramiteUsu != null && !listaBanTipoTramiteUsu.isEmpty()) {
				banTipoTramitePorAnioDTOUsu = listaBanTipoTramiteUsu.get(0);
				if (banTipoTramitePorAnioDTOUsu != null) {
					tramite = banTipoTramitePorAnioDTOUsu.getSiglas();
					listaBanCatEstCalUsu = serviceOpeBandejaEntrada.listarTraEstAniUsu(anio, tramite, usuario);
					createPieModel(listaBanCatEstCalUsu);
				}
			}
		}
	}

}
