package ar.com.multiplecloudservices.service;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import ar.com.multiplecloudservices.dto.CtzGenerationRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;


import lombok.var;


@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QrService {

	public ResponseEntity<?> read(final MultipartFile file) throws IOException, NotFoundException {
		BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
		LuminanceSource luminanceSource = new BufferedImageLuminanceSource(bufferedImage);
		BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));
		Result result = new MultiFormatReader().decode(binaryBitmap);
		return ResponseEntity.ok(new ObjectMapper().readValue(result.getText(), CtzGenerationRequestDto.class));
	}

	public void generateData(final @Valid CtzGenerationRequestDto qrCodeGenerationRequestDto,
			final HttpServletResponse httpServletResponse) throws IOException, WriterException {
		httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=data-qr.png");

		final var outputStream = new BufferedOutputStream(httpServletResponse.getOutputStream());
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = writer.encode(new ObjectMapper().writeValueAsString(qrCodeGenerationRequestDto),
				BarcodeFormat.QR_CODE, 350, 350);
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		outputStream.flush();
	}

	public void qrCode(final String data, final HttpServletResponse httpServletResponse)
			throws IOException, WriterException {
		httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=data-qrCode.png");

		final var outputStream = new BufferedOutputStream(httpServletResponse.getOutputStream());
		QRCodeWriter writer = new QRCodeWriter();
		BitMatrix bitMatrix = writer.encode(new ObjectMapper().writeValueAsString(data), BarcodeFormat.QR_CODE, 350,
				350);
		MatrixToImageWriter.writeToStream(bitMatrix, "PNG", outputStream);
		outputStream.flush();
	}


}