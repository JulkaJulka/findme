package com.findme.dao;

import com.findme.BadRequestException;
import org.springframework.core.GenericTypeResolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.io.Serializable;


public class GenericDAOImpl<T> implements GenericDAO<T>{

    @PersistenceContext
    private EntityManager entityManager;
    private final Class<T> type;

    public GenericDAOImpl() {
        this.type = (Class<T>) GenericTypeResolver.resolveTypeArgument(getClass(), GenericDAOImpl.class);;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }


    @Override
    public T findOne(Serializable id)  {
        return entityManager.find(type, id);
    }

    @Override
    @Transactional
    public T save(Object entity) {
        entityManager.persist(entity);
        return (T)entity;
    }

    @Override
   @Transactional
    public void delete(Serializable id) throws BadRequestException {
        entityManager.remove(findOne(id));
    }

    @Transactional
    @Override
    public T update(Object entity)  {
        return entityManager.merge( (T)entity);
    }
}
