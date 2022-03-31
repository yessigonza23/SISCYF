package ec.gob.mdg.converter;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

@SuppressWarnings("rawtypes")
@FacesConverter("tipoEmpresas")
public class TipoEmpresa implements Converter{
	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		return value;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		String tipo_empresa = "";	
		
		if (value != null) {
			tipo_empresa = (String) value;
			switch (tipo_empresa) {
				case "n":
					tipo_empresa = "Persona Natural";
					break;
				case "p":
					tipo_empresa = "Organismo/ Entidad del Sector Público";
					break;
				case "o":
					tipo_empresa = "ONG/Sociedades que no están bajo la supervisión de la Supercías";
					break;
				case "j":
					tipo_empresa = "Persona Jurídica/ Sociedad bajo supervisión de la Supercías.";
					break;	
				
			}
		}
		return tipo_empresa;
	}

}
