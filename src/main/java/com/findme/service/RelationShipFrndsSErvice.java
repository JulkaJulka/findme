package com.findme.service;

import com.findme.BadRequestException;
import com.findme.dao.RelationShipDAO;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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

    @Transactional
    public List<Long> getIncomeRequests(String userId) {
        return relationShipDAOImpl.getIncomeRequests(userId);
    }

    @Transactional
    public List<Long> getOutcomeRequests(String userId){
        return relationShipDAOImpl.getOutcomeRequests(userId);
    }

    @Transactional
    public RelationShipFrnds updateRelationship(Long userIdFrom, Long userIdTo, RelationShipFriends status){
        return relationShipDAOImpl.updateRelationship(userIdFrom,userIdTo,status);
    }

    public List<RelationShipFrnds> findRelByIdAnsw(Long id){
      return   relationShipDAOImpl.findRelsByIdAnsw(id);
    }
}
