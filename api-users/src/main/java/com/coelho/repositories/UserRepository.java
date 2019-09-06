package com.coelho.repositories;

import com.coelho.models.User;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@ApplicationScoped
public class UserRepository implements GenericRepository<User, UUID> {

    @Inject
    EntityManager em;

    @Override
    public Class<User> getClazz() {
        return User.class;
    }

    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}