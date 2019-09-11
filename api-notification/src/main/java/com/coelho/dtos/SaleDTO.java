package com.coelho.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SaleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private UUID id;
    private Date date;
    private BigDecimal price;
    private UUID customerId;
    private List<ProductDTO> products;
}