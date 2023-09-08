package ar.com.multiplecloudservices.repository;

import static org.springframework.http.HttpMethod.GET;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import ar.com.multiplecloudservices.component.CtzExtractor;
import ar.com.multiplecloudservices.entity.Dollar;
import ar.com.multiplecloudservices.entity.DollarTypes;

@Repository
public class CtzRepository {

	@Autowired
	private CtzExtractor ctzExtractor;

	public List<Dollar> getPrices(final String dollarUrl) {
		try {
			RestTemplate template = new RestTemplate();
			URI url = new URI(dollarUrl);
			RequestCallback callback = (ClientHttpRequest r) -> {
			};
			return template.execute(url, GET, callback, ctzExtractor);
		} catch (URISyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public List<DollarTypes> getTypes(final String dollarUrl) {
		try {
			List<Dollar> list = getPrices(dollarUrl);
			List<DollarTypes> response = new ArrayList<DollarTypes>();
			for (Dollar type : list) {
				String symbol = type.getName().replace(" ", "_");
				response.add(DollarTypes.builder().name(type.getName()).symbol(symbol.toUpperCase()).build());
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}

	public Dollar getSymbol(final String dollarUrl, String symbolFind) {
		try {
			List<Dollar> list = getPrices(dollarUrl);
			Dollar response = null;
			for (Dollar find : list) {
				String symbol = find.getName().replace(" ", "_").toUpperCase();
				if (symbol.equals(symbolFind.toUpperCase())) {
					response = Dollar.builder().name(find.getName()).agency(find.getAgency()).buys(find.getBuys())
							.sale(find.getSale()).variation(find.getVariation()).saleZero(find.getSaleZero())
							.decimals(find.getDecimals()).build();
				}
			}
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}