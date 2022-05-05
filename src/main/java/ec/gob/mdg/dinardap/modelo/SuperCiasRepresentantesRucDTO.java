package ec.gob.mdg.dinardap.modelo;

import java.io.Serializable;

import lombok.Data;
import lombok.experimental.Accessors;
@Data
@Accessors(chain = true)
public class SuperCiasRepresentantesRucDTO  implements Serializable {
private static final long serialVersionUID = 1L;
	
	private boolean conDatos = false;
	private String ruc;
	private String cargo;
	private String fecha_nombramiento;
	private String fecha_termino;
	private String periodo;
	private String fecha_registro_mercantil;
	private String numero_registro_mercantil;
	private String cedula_administrador;
	private String nombre;
	private String representante_legal;
	private String resolucion;
	private String resolucion_salida;
}
