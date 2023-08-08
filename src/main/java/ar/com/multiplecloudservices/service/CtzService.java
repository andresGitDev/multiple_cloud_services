package ar.com.multiplecloudservices.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.multiplecloudservices.entity.Dollar;
import ar.com.multiplecloudservices.entity.DollarTypes;
import ar.com.multiplecloudservices.repository.CtzRepository;

@Service
public class CtzService {

	@Value("${dolar.url}")
	private String dollarUrl;

	@Autowired
	private CtzRepository ctzRepository;

	public List<Dollar> getDollarPrices() {
		return ctzRepository.getPrices(dollarUrl);
	}

	public List<DollarTypes> getDollarTypes() {
		return ctzRepository.getTypes(dollarUrl);
	}

	public Dollar getDollarSymbol(String symbol) {
		return ctzRepository.getSymbol(dollarUrl, symbol);
	}

}
