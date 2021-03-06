package com.findme.dao;

import com.findme.exception.InternalServerError;
import org.springframework.core.GenericTypeResolver;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;


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
    public T findOne(Long id)  {
        return entityManager.find(type, id);
    }

    @Override
    @Transactional
    public T save(Object entity) throws InternalServerError {
        entityManager.persist(entity);
        return (T)entity;
    }

    @Override
   @Transactional
    public void delete(Long id) {
        entityManager.remove(findOne(id));
    }

    @Transactional
    @Override
    public T update(Object entity) throws InternalServerError  {
        return entityManager.merge( (T)entity);
    }
}
