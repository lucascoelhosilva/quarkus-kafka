package com.coelho.repositories;

import com.coelho.models.Product;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class ProductRepository implements GenericRepository<Product, UUID> {

    @Inject
    EntityManager em;

    @Override
    public Class<Product> getClazz() {
        return Product.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}