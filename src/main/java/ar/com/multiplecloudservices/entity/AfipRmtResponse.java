package ar.com.multiplecloudservices.entity;

import lombok.*;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class AfipRmtResponse {

    private String remito;
    private BigInteger number;
    private String cot;

}