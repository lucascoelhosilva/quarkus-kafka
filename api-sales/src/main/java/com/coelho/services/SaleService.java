package com.coelho.services;

import com.coelho.models.Sale;
import java.util.List;
import java.util.UUID;

public interface SaleService {

    Sale findById(UUID id);

    List<Sale> findAll();

    Sale create(Sale sale);

    Sale update(Sale sale);

    void delete(UUID id);
}