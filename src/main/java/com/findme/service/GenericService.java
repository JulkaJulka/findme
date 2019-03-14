package com.findme.service;

import com.findme.BadRequestException;
import org.springframework.stereotype.Service;

import java.io.Serializable;


public interface GenericService<T> {
    T findOne(Serializable id);

    T save(T t);

    void delete(Serializable id) throws BadRequestException;

    T update(T t) throws BadRequestException;
}
