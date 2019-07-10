package com.findme.service;

import com.findme.BadRequestException;
import com.findme.dao.RelationShipDAO;
import com.findme.dao.RelationShipFrndsDAOImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RelationShipFrndsSErvice {

    @Autowired
    private RelationShipFrndsDAOImpl relationShipDAOImpl;

    @Transactional
    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException{

        if(userIdFrom == userIdTo){
            throw new BadRequestException("You can not add myself");

        } else {
            relationShipDAOImpl.addRelationship(userIdFrom, userIdTo);

        }
    };
}
