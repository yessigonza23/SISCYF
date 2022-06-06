package ec.gob.mdg.dinardap.servicios;


import java.util.ArrayList;
import java.util.List;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ec.gob.mdg.dinardap.modelo.AntLicenciaDTO;
import ec.gob.mdg.dinardap.modelo.AntPlacaDTO;
import ec.gob.mdg.dinardap.modelo.RegistroCivilCedulaDTO;
import ec.gob.mdg.dinardap.modelo.SriRucDTO;
import ec.gob.mdg.dinardap.modelo.SriRucDetalleDTO;
import ec.gob.mdg.dinardap.modelo.SuperCiasRepresentantesRucDTO;
import ec.gob.mdg.dinardap.modelo.SuperCiasRucDTO;

public class ServiciosWeb {
	
	//private static final String REST_SERVICE_URL = "http://10.41.1.63:8051/servicios/";
	private static final String REST_SERVICE_URL = "http://186.46.148.233:8051/servicios/";
	//private static final String REST_SERVICE_URL = "http://localhost:8080/servicios/";
		
	static RegistroCivilCedulaDTO el_ciudadano = new RegistroCivilCedulaDTO();
	static SriRucDTO la_persona = new SriRucDTO();
	static List<SriRucDetalleDTO> los_establecimientos = new ArrayList<SriRucDetalleDTO>();
	static AntPlacaDTO el_vehiculo = new AntPlacaDTO();
	static AntLicenciaDTO la_licencia = new AntLicenciaDTO();
	static SuperCiasRucDTO la_persona_cias = new SuperCiasRucDTO();
	static List<SuperCiasRepresentantesRucDTO> los_representantes = new ArrayList<SuperCiasRepresentantesRucDTO>();
		
	
	
	public static RegistroCivilCedulaDTO consultarCiudadanoRegistroCivil(String cedula) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "registrocivil/cedula/{cedula}").resolveTemplate("cedula", cedula);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		el_ciudadano = g.fromJson(response, RegistroCivilCedulaDTO.class);
		return el_ciudadano;
	}
	
	public static RegistroCivilCedulaDTO consultarCiudadanoRegistroCivilFoto(String cedula) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "registrocivil/Imagenes/{cedula}").resolveTemplate("cedula", cedula);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		el_ciudadano = g.fromJson(response, RegistroCivilCedulaDTO.class);
		return el_ciudadano;
	}
	
	public static SriRucDTO consultarPersonaServicioRentasInternas(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "sri/ruc/v2/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		la_persona = g.fromJson(response, SriRucDTO.class);
		return la_persona;
	}
	
	public static List<SriRucDetalleDTO> consultarEstablecimientosPersonaServicioRentasInternas(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "sri/establecimientos/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		los_establecimientos = g.fromJson(response, new TypeToken<ArrayList<SriRucDetalleDTO>>() {}.getType());
		return los_establecimientos;
	}
	
	public static AntPlacaDTO consultarVehiculoPlacaAnt(String placa) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "ant/placa/{placa}").resolveTemplate("placa", placa);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		el_vehiculo = g.fromJson(response, AntPlacaDTO.class);
		return el_vehiculo;
	}
	
	public static AntLicenciaDTO consultarLicenciaAnt(String cedula) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "ant/licencia/{cedula}").resolveTemplate("cedula", cedula);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		la_licencia = g.fromJson(response, AntLicenciaDTO.class);
		return la_licencia;
	}
	
	public static SuperCiasRucDTO consultarDatosGeneralesSupercias(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "supercias/datos/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		la_persona_cias = g.fromJson(response, SuperCiasRucDTO.class);
		return la_persona_cias;
	}
	
	public static List<SuperCiasRepresentantesRucDTO> consultarRepresentantesSupercias(String ruc) {
		Client client = ClientBuilder.newClient();
		WebTarget target = client.target(REST_SERVICE_URL + "supercias/representantes/{ruc}").resolveTemplate("ruc", ruc);
		String response = target.request(MediaType.APPLICATION_JSON_TYPE).header("X-API-KEY","ministeriodegobierno.gob.ec_ecuador").get(String.class);
		
		Gson g = new Gson();
		los_representantes = g.fromJson(response, new TypeToken<ArrayList<SuperCiasRepresentantesRucDTO>>() {}.getType());
		return los_representantes;
	}
}
