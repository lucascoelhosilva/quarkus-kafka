package com.coelho.repositories;

import java.util.List;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;

public interface GenericRepository<T, ID> {

    Class<T> getClazz();

    EntityManager getEntityManager();

    @Transactional
    default void persist(T entity) {
        getEntityManager().persist(entity);
    }

    default T findById(final ID id) {
        return getEntityManager().find(getClazz(), id);
    }

    default boolean contains(final ID id) {
        return findById(id) != null;
    }

    @Transactional
    default void delete(final ID id) {
        T entity = findById(id);
        getEntityManager().remove(entity);
    }

    default List<T> listAll() {
        return getEntityManager().createQuery("SELECT e FROM " + this.getClazz().getSimpleName() + " as e", getClazz())
                .getResultList();
    }

}