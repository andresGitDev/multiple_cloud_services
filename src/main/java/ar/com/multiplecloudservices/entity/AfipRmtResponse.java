package ar.com.multiplecloudservices.entity;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AfipRmtResponse {

	private String remito;
	private BigInteger number;
	private String cot;

}