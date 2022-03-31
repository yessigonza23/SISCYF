package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("estadoAprobados")
public class EstadoAprobado implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String aprobado = "";	
		
		if (value != null) {
			aprobado = (String) value;
			switch (aprobado) {
				case "S":
					aprobado = "Aprobado";
					break;
				case "N":
					aprobado = "No aprobado";
					break;
			}
		}
		return aprobado;
	}

}
