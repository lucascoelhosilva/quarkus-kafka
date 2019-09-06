package com.coelho.services;

import com.coelho.models.Product;
import java.util.List;
import java.util.UUID;

public interface ProductService {

    Product findById(UUID id);

    List<Product> findAll();

    Product create(Product product);

    Product update(Product product);

    void delete(UUID id);
}