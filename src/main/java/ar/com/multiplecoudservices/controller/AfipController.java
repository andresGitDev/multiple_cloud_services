package ar.com.multiplecoudservices.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ar.com.multiplecoudservices.entity.AfipRmtRequest;
import ar.com.multiplecoudservices.entity.AfipRmtResponse;
import ar.com.multiplecoudservices.service.AfipService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class AfipController {

	@Autowired
	private final AfipService afipService;

	@GetMapping(value = "/afipUrlHomo")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns url afip homologado")
	public ResponseEntity<String> getAfipUrlHomo() {
		return ResponseEntity.ok(afipService.getAfipHomo());
	}

	@GetMapping(value = "/afipUrlProd")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns url afip production")
	public ResponseEntity<String> getUrlAfipProd() {
		return ResponseEntity.ok(afipService.getAfipProd());
	}

	@GetMapping(value = "/afip/remito/{number}")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a value with the information of the queried remito")
	public ResponseEntity<AfipRmtResponse> send(@Valid @RequestBody(required = true) final AfipRmtRequest request) {
		return ResponseEntity.ok(afipService.send(request));
	}

}