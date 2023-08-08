package ar.com.multiplecloudservices.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import ar.com.multiplecloudservices.entity.AfipRmtRequest;
import ar.com.multiplecloudservices.entity.AfipRmtResponse;

@Service
public class AfipService {

	@Value("${afip.homo}")
	private String urlHomo;

	@Value("${afip.prod}")
	private String urlProd;

	public String getAfipHomo() {
		return this.urlHomo;
	}

	public String getAfipProd() {
		return this.urlProd;
	}

	public AfipRmtResponse send(AfipRmtRequest request) {
		AfipRmtResponse response =null;
		return response;
	}

}