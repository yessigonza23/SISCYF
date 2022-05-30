package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("origen")
public class Origen implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String origen = "";	
		
		if (value != null) {
			origen = (String) value;
			switch (origen) {
				case "N":
					origen = "Nacional";
					break;
				case "E":
					origen = "Extranjero";
					break;								
			}
		}
		return origen;
	}

}
