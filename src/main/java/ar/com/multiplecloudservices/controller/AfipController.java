package ar.com.multiplecloudservices.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.com.multiplecloudservices.model.PersonaTo;
import ar.com.multiplecloudservices.service.AfipService;
import lombok.AllArgsConstructor;


@RestController
@AllArgsConstructor
public class AfipController {

	private AfipService afipService;

	@RequestMapping(path = "/afip/{cuit}")
	public ResponseEntity<PersonaTo> afipData(@PathVariable String cuit) throws Exception {
		PersonaTo persona= new PersonaTo();
		if (!afipService.verificado(cuit)) {
			throw new Exception("Cuit invalido. ");
		}
		try {
			persona=afipService.getData(cuit);
		} catch (Exception e) {
			throw new Exception("Error al obtener data. " + e.getMessage());
		}

		return ResponseEntity.ok(persona) ;
	}

}