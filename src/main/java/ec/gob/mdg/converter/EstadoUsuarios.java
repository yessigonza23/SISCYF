package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("estadoUsuarios")
public class EstadoUsuarios implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String estado = "";	
		
		if (value != null) {
			estado = (String) value;
			switch (estado) {
				case "A":
					estado = "Activo";
					break;
				case "B":
					estado = "Bloqueado";
					break;
				case "N":
					estado = "No Activo";
					break;		
			}
		}
		return estado;
	}

}
