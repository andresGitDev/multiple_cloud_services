package ar.com.multiplecloudservices.entity;

import java.math.BigDecimal;
import lombok.*;
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Dollar {

    private BigDecimal buys;
    private BigDecimal sale;
    private Integer agency;
    private String name;
    private Double variation;
    private Boolean saleZero;
    private Integer decimals;

}