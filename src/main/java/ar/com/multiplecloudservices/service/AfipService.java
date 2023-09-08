package ar.com.multiplecloudservices.service;

import java.io.IOException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import ar.com.multiplecloudservices.model.PersonaTo;
import lombok.Getter;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Getter
@Service
public class AfipService {

	@Value("${afip.url}")
	private String afipUrl;

	public PersonaTo getData(String cuit) throws IOException {
		String endPoint = this.afipUrl.replace("{cuit}", cuit);
		OkHttpClient client = new OkHttpClient().newBuilder().build();
		MediaType mediaType = MediaType.parse("text/plain");
		RequestBody body = RequestBody.create(mediaType, "");
		Request request = new Request.Builder().url(endPoint).method("POST", body).addHeader("Cookie",
				"ARRAffinity=bcfe566c8e623f3a734e6c1260843f9c13b2ed089a23f8760b7ab7f8ecaeef54; ARRAffinitySameSite=bcfe566c8e623f3a734e6c1260843f9c13b2ed089a23f8760b7ab7f8ecaeef54")
				.build();
		Response response = client.newCall(request).execute();
		return personaMapper(response.body().string());
	}

	private PersonaTo personaMapper(String body) throws JsonMappingException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode = objectMapper.readTree(body);
		
		JsonNode jsonContribuyente = jsonNode.get("Contribuyente");
		String idPersona = jsonContribuyente.get("idPersona").asText();
		String tipoClave = jsonContribuyente.get("tipoClave").asText();
		String estadoClave = jsonContribuyente.get("estadoClave").asText();
		String nombre = jsonContribuyente.get("nombre").asText();
		
		JsonNode jsonDomicilio = jsonContribuyente.get("domicilioFiscal");
		String direccion = jsonDomicilio.get("direccion").asText();
		String localidad = jsonDomicilio.get("localidad").asText();
		String codPostal = jsonDomicilio.get("codPostal").asText();
		String nombreProvincia = jsonDomicilio.get("nombreProvincia").asText();

		return PersonaTo.builder().idPersona(idPersona).tipoClave(tipoClave).estadoClave(estadoClave).nombre(nombre)
				.direccion(direccion).localidad(localidad).codPostal(codPostal).nombreProvincia(nombreProvincia)
				.build();
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