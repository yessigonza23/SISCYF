package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("tipoEmpleo")
public class TipoEmpleo implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String tipo_empleo = "";	
		
		if (value != null) {
			tipo_empleo = (String) value;
			switch (tipo_empleo) {
				case "E":
					tipo_empleo = "ELABORACIÓN DE PRODUCTOS";
					break;
				case "U":
					tipo_empleo = "USO DIRECTO";
					break;
				case "D":
					tipo_empleo = "ELABORACIÓN DE PRODUCTOS/USO DIRECTO";
					break;
				
			}
		}
		return tipo_empleo;
	}

}
