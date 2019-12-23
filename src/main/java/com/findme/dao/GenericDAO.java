package com.findme.dao;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;

public interface GenericDAO<T> {
    T findOne(Long id);

    T save(T t) throws InternalServerError;

    void delete(Long id) throws BadRequestException;

    T update(T t) throws BadRequestException, InternalServerError;
}
