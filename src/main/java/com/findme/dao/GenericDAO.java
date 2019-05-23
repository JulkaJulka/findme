package com.findme.dao;

import com.findme.BadRequestException;

import java.io.Serializable;

public interface GenericDAO<T> {
    T findOne(Long id);

    T save(T t);

    void delete(Long id) throws BadRequestException;

    T update(T t) throws BadRequestException;
}
