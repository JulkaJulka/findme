package com.findme.service;

import com.findme.BadRequestException;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RelationshipService {

    @Autowired
    private RelationShipFrndsDAOImpl relationShipDAOImpl;

    @Transactional
    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException {

        if (userIdFrom == userIdTo)
            throw new BadRequestException("You can not add myself");

        RelationShipFrnds relationShipFind = relationShipDAOImpl.findRelByFromTo(userIdTo, userIdFrom);

        if (relationShipFind != null) {

            if (relationShipFind.getStatus() == RelationShipFriends.PENDING)
                throw new BadRequestException("You have to accept request from you friend " + userIdTo);

            if (relationShipFind.getStatus() == RelationShipFriends.ACCEPT)
                throw new BadRequestException("You have been already friends with " + userIdTo);

            relationShipFind.setUserFrom(userIdFrom);
            relationShipFind.setUserTo(userIdTo);
            relationShipFind.setStatus(RelationShipFriends.PENDING);

            relationShipDAOImpl.update(relationShipFind);
        } else{

        relationShipFind = relationShipDAOImpl.findRelByFromTo(userIdFrom, userIdTo);

        if (relationShipFind != null) {

            if (relationShipFind.getStatus() == RelationShipFriends.PENDING)
                throw new BadRequestException(userIdTo + " must accept your request that you had sent early");

            if (relationShipFind.getStatus() == RelationShipFriends.ACCEPT)
                throw new BadRequestException("You have been already friends with " + userIdTo);

            relationShipFind.setStatus(RelationShipFriends.PENDING);

            relationShipDAOImpl.update(relationShipFind);

        } else {

            RelationShipFrnds relationShipFrnds = new RelationShipFrnds();
            relationShipFrnds.setUserFrom(userIdFrom);
            relationShipFrnds.setUserTo(userIdTo);
            relationShipFrnds.setStatus(RelationShipFriends.PENDING);

            relationShipDAOImpl.save(relationShipFrnds);
        }}
    }


    @Transactional
    public List<Long> getIncomeRequests(String userId) {
        return relationShipDAOImpl.getIncomeRequests(userId);
    }

    @Transactional
    public List<Long> getOutcomeRequests(String userId) {
        return relationShipDAOImpl.getOutcomeRequests(userId);
    }

    @Transactional
    public RelationShipFrnds updateRelationship(Long userIdFrom, Long userIdTo, RelationShipFriends status) {
        return relationShipDAOImpl.updateRelationship(userIdFrom, userIdTo, status);
    }

    public List<RelationShipFrnds> findRelByIdAnsw(Long id) {
        return relationShipDAOImpl.findRelsByIdAnsw(id);
    }


}
