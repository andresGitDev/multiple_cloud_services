package ar.com.multiplecoudservices.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.com.multiplecoudservices.entity.Dollar;
import ar.com.multiplecoudservices.entity.DollarTypes;
import ar.com.multiplecoudservices.service.CtzService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class CtzController {

	@Autowired
	private final CtzService ctzService;

	@GetMapping(value = "/ctzDollarPrices")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a list with the information of the main quotes")
	public ResponseEntity<List<Dollar>> ctzGetDollarPrices() {
		return ResponseEntity.ok(ctzService.getDollarPrices());
	}

	@GetMapping(value = "/ctzDollarTypes")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a list with the information of the quote types that can be consulted")
	public ResponseEntity<List<DollarTypes>> ctzGetTypes() {
		return ResponseEntity.ok(ctzService.getDollarTypes());
	}

	@GetMapping(value = "/ctzDollarSymbol/{symbol}")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a value with the information of the queried symbol (see symbols with /ctzDollarTypes)")
	public ResponseEntity<Dollar> ctzGetDollarSymbol(@PathVariable String symbol) {
		return ResponseEntity.ok(ctzService.getDollarSymbol(symbol));
	}

}
