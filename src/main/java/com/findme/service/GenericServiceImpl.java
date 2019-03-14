package com.findme.service;

import com.findme.BadRequestException;
import com.findme.dao.GenericDAO;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;

public class GenericServiceImpl<T, V extends GenericDAO<T>> implements GenericService<T> {

    @Autowired
    private V genericDAO;
    @Override
    public T findOne(Serializable id) {
        return genericDAO.findOne(id);
    }

    @Override
    public T save(T entity) {
        return genericDAO.save(entity);
    }

    @Override
    public void delete(Serializable id) throws BadRequestException {
        checkExistenceEntityInDB(id);
        //return genericDAO.delete(id);
        genericDAO.delete(id);
    }

    @Override
    public T update(T entity) throws BadRequestException {
        return  genericDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Serializable id) throws BadRequestException{
        T findEntity = (T)findOne( id);
        if(findEntity == null) throw new BadRequestException(  " with id " + id + "doesn't exist in DB" );
        return true;
    }
}
