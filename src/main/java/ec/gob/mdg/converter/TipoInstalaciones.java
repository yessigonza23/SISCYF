package ec.gob.mdg.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("instalacion")
public class TipoInstalaciones implements Converter {
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String instalacion = "";

		if (value != null) {
			instalacion = (String) value;
			switch (instalacion) {
			case "P":
				instalacion = "PROPIAS";
				break;
			case "A":
				instalacion = "ARRENDADAS";
				break;
			case "O":
				instalacion = "OTROS";
				break;
			}
		}
		return instalacion;
	}

}
