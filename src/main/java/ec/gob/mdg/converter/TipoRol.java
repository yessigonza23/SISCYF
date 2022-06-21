package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("tipoRol")
public class TipoRol implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String tipo = "";	
		
		if (value != null) {
			tipo = (String) value;
			switch (tipo) {
				case "D":
					tipo = "DIRECCIÓN";
					break;
				case "C":
					tipo = "COORDINACIÓN";
					break;
				case "T":
					tipo = "TÉCNICO";
					break;
				
			}
		}
		return tipo;
	}

}
