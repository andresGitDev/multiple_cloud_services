package ar.com.multiplecloudservices.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonaTo {
	private String idPersona;
	private String tipoClave;
	private String tipoDocumento;
	private String estadoClave;
	private String nombre;
	private String sexo;
	private String fechaNacimiento;
	private String direccion;
	private String localidad;
	private String codPostal;
	private String nombreProvincia;
	private String esResponsableInscripto;
	private String esMonotributo;
	private String esExento;
	private String esConsumidorFinal;

}
