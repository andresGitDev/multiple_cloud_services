package ar.com.multiplecoudservices.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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