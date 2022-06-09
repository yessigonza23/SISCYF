package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("tipoMenu")
public class TipoMenu implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String tipo_menu = "";	
		
		if (value != null) {
			tipo_menu = (String) value;
			switch (tipo_menu) {
				case "I":
					tipo_menu = "SUBMENU";
					break;
				case "S":
					tipo_menu = "PRINCIPAL";
					break;				
			}
		}
		return tipo_menu;
	}

}
