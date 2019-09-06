package com.coelho.repositories;

import com.coelho.models.Sale;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class SaleRepository implements GenericRepository<Sale, UUID> {

    @Inject
    EntityManager em;

    @Override
    public Class<Sale> getClazz() {
        return Sale.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
