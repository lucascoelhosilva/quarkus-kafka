package com.coelho.mappers;

import com.coelho.dtos.SaleDTO;
import com.coelho.models.Sale;
import com.coelho.models.User;
import java.util.stream.Collectors;

public class SaleMapper {
    public static SaleDTO toDTO(Sale sale) {
        return new SaleDTO(
                sale.getId(),
                sale.getDate(),
                sale.getPrice(),
                sale.getUser().getId(),
                sale.getProducts()
                        .stream()
                        .map(ProductMapper::toDTO)
                        .collect(Collectors.toList())
        );
    }

    public static Sale toModel(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setDate(saleDTO.getDate());
        sale.setUser(new User(saleDTO.getUserId()));
        sale.setPrice(saleDTO.getPrice());

        if (!saleDTO.getProducts().isEmpty()) {
            sale.setProducts(
                    saleDTO.getProducts()
                            .stream()
                            .map(ProductMapper::toModel)
                            .collect(Collectors.toList())
            );
        }

        return sale;
    }
}