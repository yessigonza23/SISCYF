package ec.gob.mdg.utils;

import java.util.Map;
import org.primefaces.shaded.json.JSONObject;

public class GenerarJson {
	
	public static String generarJson(Map<String, Object> parametros) {
        JSONObject json = new JSONObject(parametros);
        return json.toString();
		
	}

}