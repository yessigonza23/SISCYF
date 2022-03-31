package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("titulos")
public class Titulos implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String titulo = "";	
		
		if (value != null) {
			titulo = (String) value;
			switch (titulo) {
				case "Eco.":
					titulo = "Economista";
					break;
				case "Lic.":
					titulo = "Licenciado/a";
					break;
				case "Ing.":
					titulo = "Ingeniero/a";
					break;
				case "Sra.":
					titulo = "Señora";
					break;	
				case "Sr.":
					titulo = "Señor";
					break;
				case "Srta.":
					titulo = "Señorita";
					break;
				case "Mgs.":
					titulo = "Magister";
					break;
				case "Dra.":
					titulo = "Doctora";
					break;
				case "Dr.":
					titulo = "Doctor";
					break;
				case "Quim.":
					titulo = "Químico/a";
					break;
					
			}
		}
		return titulo;
	}

}
