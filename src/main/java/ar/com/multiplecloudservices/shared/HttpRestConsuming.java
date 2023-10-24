package ar.com.multiplecloudservices.shared;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.SSLContext;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HttpRestConsuming {
	private HttpComponentsClientHttpRequestFactory requestFactory;
	private RestTemplate restTemplate;

	public <T> T getHttp(String URL, Class<T> claseretorno, Map<String, String> vars) throws Exception {
		setSSLandRestT(true);
		try {
			T response = restTemplate.getForObject(URL, claseretorno, vars);
			return response;
		} catch (HttpClientErrorException e) {
			log.error("Error in request HTTP GET to {}", URL);
			throw e;
		} catch (Exception e) {
			log.error("Generic error in get HTTP:" + e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

	public <T> T getHttpEntity(String URL, Class<T> claseretorno, Map<String, String> vars) throws Exception {
		setSSLandRestT(true);
		try {
			ResponseEntity<T> response;
			if (vars != null)
				response = restTemplate.getForEntity(URL, claseretorno, vars);
			else
				response = restTemplate.getForEntity(URL, claseretorno);
			return response.getBody();
		} catch (HttpClientErrorException e) {
			log.error("Error in request HTTP GET to {}", URL);
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			log.error("Generic error in get HTTP:" + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	public <T> List<T> httpListEntity(String URL, ParameterizedTypeReference<List<T>> responseType, HttpMethod method,
			Map<String, String> vars) throws Exception {
		setSSLandRestT(true);
		try {
			ResponseEntity<List<T>> response;
			if (vars != null && vars.isEmpty()) {
				response = restTemplate.exchange(URL, method, null, responseType, vars);
			} else {
				response = restTemplate.exchange(URL, method, null, responseType, vars);
			}
			return response.getBody();
		} catch (RestClientException e) {
			log.error("falla en consumir Rest service:{} , con el methodo:{}", URL, method.name());
			throw new Exception(e.getMessage());
		} catch (Exception e) {
			log.error("Generic error in get HTTP:" + e.getMessage());
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 
	 * @param classe   recibe la classe para deserializar json
	 * @param response string que contiene tags json mescladas con xml
	 */
	public <T> T TrataResponse(Class<T> classe, String response) {
		Pattern jsonpattern = Pattern.compile("\\{+[^.}?]*[.}?]");
		Matcher m = jsonpattern.matcher(response);
		if (m.find()) {
			String json = m.group(0);
			return JsonToEntity(json, classe);
		}
		return null;
	}

	protected void setSSLandRestT(boolean activo)
			throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
		if (activo) {
			TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

			SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
					.loadTrustMaterial(null, acceptingTrustStrategy)// .setSecureRandom(new SecureRandom())
					.build();

			SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
			RequestConfig customizedRequestConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.IGNORE_COOKIES)
					.build();

			CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf)
					.setDefaultRequestConfig(customizedRequestConfig).setSSLHostnameVerifier(new NoopHostnameVerifier())
					.build();
			this.requestFactory = new HttpComponentsClientHttpRequestFactory();
			this.requestFactory.setHttpClient(httpClient);

			this.restTemplate = new RestTemplate(this.requestFactory);
		} else {
			this.restTemplate = new RestTemplate();
		}
	}

	protected HttpHeaders setHeadersParams(String User, String Password) {
		// Set the headers you need send
		String auth = User + ":" + Password;
		byte[] encodedAuth = Base64.encodeBase64(auth.getBytes(Charset.forName("US-ASCII")));
		String authHeader = "Basic " + new String(encodedAuth);
		final HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.set("Authorization", authHeader);
		headers.set("cache-control", "no-cache");
		headers.setContentType(MediaType.APPLICATION_JSON);
		return headers;
	}

	public <T> T JsonToEntity(String Json, Class<T> classe) {
		return new Gson().fromJson(Json, classe);
	}

	public <T> T JsonToEntity(JsonElement Json, Class<T> classe) {
		return new Gson().fromJson(Json, classe);
	}

	public <T> T JsonToEntityBYElement(String response, Class<T> classe, String... tags) {
		Gson gson = new Gson();
		JsonObject obj = gson.fromJson(response, JsonObject.class);
		int i = 0;
		JsonElement el = null;
		for (String tag : tags) {
			if (i < 1) {
				el = obj.get(tag);
			} else {
				JsonObject jsonObject = el.getAsJsonObject();
				el = jsonObject.get(tag);
			}
			i++;
		}
		return JsonToEntity(el, classe);
	}

}
