package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("tipoProducto")
public class TipoProducto implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String tipo_producto = "";	
		
		if (value != null) {
			tipo_producto = (String) value;
			switch (tipo_producto) {
				case "I":
					tipo_producto = "PRODUCTO INTERMEDIO";
					break;
				case "T":
					tipo_producto = "PRODUCTO TERMINADO";
					break;
				case "D":
					tipo_producto = "DILUCIONES";
					break;
				
			}
		}
		return tipo_producto;
	}

}
