package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("tipoIdentificacion")
public class TipoIdentificacion implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String tipoIdentificacion = "";	
		
		if (value != null) {
			tipoIdentificacion = (String) value;
			switch (tipoIdentificacion) {
				case "C":
					tipoIdentificacion = "Cédula";
					break;
				case "R":
					tipoIdentificacion = "RUC";
					break;
				case "P":
					tipoIdentificacion	= "Pasaporte";				
					break;
			
			}
		}
		return tipoIdentificacion;
	}

}
