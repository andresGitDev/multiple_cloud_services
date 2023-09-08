package ar.com.multiplecloudservices.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import ar.com.multiplecloudservices.dto.CtzGenerationRequestDto;
import ar.com.multiplecloudservices.service.QrService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class QrController {

	private final QrService qrService;

	@PostMapping(value = "/generateData")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a .png QR code with provided information decoded inside")
	public void qrCodeGenerationHandler(
			@Valid @RequestBody(required = true) final CtzGenerationRequestDto qrCodeGenerationRequestDto,
			final HttpServletResponse httpServletResponse) throws IOException, WriterException {
		qrService.generateData(qrCodeGenerationRequestDto, httpServletResponse);
	}

	@GetMapping(value = "/qrCode")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "Returns a .png QR code with provided information decoded inside")
	public void qrCodeGeneration(final HttpServletResponse httpServletResponse, String data)
			throws IOException, WriterException {
		qrService.qrCode(data, httpServletResponse);
	}

	@PutMapping(value = "/read", consumes = "multipart/form-data")
	@ResponseStatus(value = HttpStatus.OK)
	@Operation(summary = "returns decoded information inside provided QR code")
	public ResponseEntity<?> read(
			@Parameter(description = ".png image of QR code generated through this portal") @RequestParam(value = "file", required = true) MultipartFile file)
			throws IOException, NotFoundException {
		return qrService.read(file);
	}

}
