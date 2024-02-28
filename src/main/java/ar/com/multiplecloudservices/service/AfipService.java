package ar.com.multiplecloudservices.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.multiplecloudservices.model.PersonaTo;
import ar.com.multiplecloudservices.shared.HttpRestConsuming;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AfipService {

	@Value("${afip.url}")
	private String afipUrl;
	@Value("${afip.cuitUrl}")
	private String afipCuitUrl;
	private final HttpRestConsuming restConsuming;

	public PersonaTo getData(String cuit) throws Exception {
		if (!verificado(cuit))
			throw new Exception("Cuit invalido (" + cuit + ").");
		Map<String, String> vars = new HashMap<String, String>();
		String endPoint = this.afipUrl.replace("{cuit}", cuit);
		vars.put("cuit", cuit);
		String response = restConsuming.getHttp(endPoint, String.class, vars);
		return personaMapper(response);
	}

	public String getCuit(String dni) throws Exception {
		Map<String, String> vars = new HashMap<String, String>();
		String endPoint = this.afipCuitUrl.replace("{dni}", dni);
		vars.put("dni", dni);
		String response = restConsuming.getHttp(endPoint, String.class, vars);
		return cuitMapper(response);
	}

	private String cuitMapper(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(body);

		JsonNode jsonData = jsonNode.get("data");
		String cuit = jsonData.get(0).asText();

		return cuit;
	}

	private PersonaTo personaMapper(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(body);

		JsonNode jsonContribuyente = jsonNode.get("Contribuyente");
		String idPersona = jsonContribuyente.get("idPersona").asText();
		String tipoClave = jsonContribuyente.get("tipoClave").asText();
		String estadoClave = jsonContribuyente.get("estadoClave").asText();
		String nombre = jsonContribuyente.get("nombre").asText();
		String sexo = jsonContribuyente.get("Sexo").asText();

		String esRI = jsonContribuyente.get("EsRI").asText();
		String esM = jsonContribuyente.get("EsMonotributo").asText();
		String esE = jsonContribuyente.get("EsExento").asText();
		String esCF = jsonContribuyente.get("EsConsumidorFinal").asText();

		JsonNode jsonDomicilio = jsonContribuyente.get("domicilioFiscal");
		String direccion = jsonDomicilio.get("direccion").asText();
		String localidad = jsonDomicilio.get("localidad").asText();
		String codPostal = jsonDomicilio.get("codPostal").asText();
		String nombreProvincia = jsonDomicilio.get("nombreProvincia").asText();

		return PersonaTo.builder().idPersona(idPersona).tipoClave(tipoClave).estadoClave(estadoClave).nombre(nombre)
				.sexo(sexo).direccion(direccion).localidad(localidad).codPostal(codPostal)
				.nombreProvincia(nombreProvincia).esResponsableInscripto(esRI).esMonotributo(esM).esExento(esE)
				.esConsumidorFinal(esCF).build();
	}

	public Boolean verificado(String number) {

		// Eliminamos todos los caracteres que no son números
		String cuit = number.replaceAll("[^\\d]", "");
		// Controlamos si son 11 números los que quedaron, si no es el caso, ya devuelve
		// falso
		if (cuit.length() != 11) {
			return false;
		}
		// Convertimos la cadena que quedó en una matriz de caracteres
		char[] cuitArray = cuit.toCharArray();
		// Inicializamos una matriz por la cual se multiplicarán cada uno de los dígitos
		Integer[] serie = { 5, 4, 3, 2, 7, 6, 5, 4, 3, 2 };
		// Creamos una variable auxiliar donde guardaremos los resultados del cálculo
		// del número validador
		Integer aux = 0;
		// Recorremos las matrices de forma simultánea, sumando los productos de la
		// serie por el número en la misma posición
		for (int i = 0; i < 10; i++) {
			aux += Character.getNumericValue(cuitArray[i]) * serie[i];
		}
		// Hacemos como se especifica: 11 menos el resto de de la división de la suma de
		// productos anterior por 11
		aux = 11 - (aux % 11);
		// Si el resultado anterior es 11 el código es 0
		if (aux == 11) {
			aux = 0;
		}
		// Si el resultado anterior es 10 el código no tiene que validar, cosa que de
		// todas formas pasa
		// en la siguiente comparación.
		// Devuelve verdadero si son iguales, falso si no lo son
		// (Esta forma esta dada para prevenir errores, en java 6 se puede usar: return
		// aux.equals(Character.getNumericValue(cuitArray[10]));)
		return Objects.equals(Character.getNumericValue(cuitArray[10]), aux);
	}
}