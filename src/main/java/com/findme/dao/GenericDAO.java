package com.findme.dao;

import com.findme.BadRequestException;

import java.io.Serializable;

public interface GenericDAO<T> {
    T findOne(Serializable id);

    T save(T t);

    void delete(Serializable id) throws BadRequestException;

    T update(T t);
}
