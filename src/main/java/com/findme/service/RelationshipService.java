package com.findme.service;

import com.findme.BadRequestException;
import com.findme.NotFoundException;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShip;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class RelationshipService {

    @Autowired
    private RelationShipFrndsDAOImpl relationShipDAOImpl;
    @Autowired
    private UserDAOImpl userDAO;

    /*@Transactional
    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException {

        if (userIdFrom == userIdTo)
            throw new BadRequestException("You can not add myself");

        RelationShipFrnds relationShipFind = relationShipDAOImpl.findRelByFromTo(userIdFrom, userIdTo);

        if (relationShipFind == null) {

            RelationShipFrnds relationShipFrnds = new RelationShipFrnds();
            relationShipFrnds.setUserFrom(userIdFrom);
            relationShipFrnds.setUserTo(userIdTo);
            relationShipFrnds.setStatus(RelationShipFriends.PENDING);

            relationShipDAOImpl.save(relationShipFrnds);
        }

        if (relationShipFind != null) {

            if (relationShipFind.getStatus() == RelationShipFriends.PENDING)
                throw new BadRequestException("You have to accept request from you friend " + userIdTo);

            if (relationShipFind.getStatus() == RelationShipFriends.ACCEPT)
                throw new BadRequestException("You have been already friends with " + userIdTo);

            relationShipFind.setUserFrom(userIdFrom);
            relationShipFind.setUserTo(userIdTo);
            relationShipFind.setStatus(RelationShipFriends.PENDING);

            relationShipDAOImpl.update(relationShipFind);
        } else {

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
            }
        }
    }*/

    @Transactional
    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException {
        validateUserIds(userIdFrom, userIdTo);

        RelationShipFrnds relationShipFrndsFind = relationShipDAOImpl.findRelByFromTo(userIdFrom, userIdTo);

        if (relationShipFrndsFind == null) {
            RelationShipFrnds relationShipFrnds = new RelationShipFrnds();
            relationShipFrnds.setUserFrom(userIdFrom);
            relationShipFrnds.setUserTo(userIdTo);
            relationShipFrnds.setStatus(RelationShipFriends.PENDING);

            relationShipDAOImpl.save(relationShipFrnds);
        } else {
            throw new BadRequestException("You have had relationShip with id " + userIdTo + " already");
        }

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
    public RelationShipFrnds updateRelationshipStatus(Long userIdFrom, Long userIdTo, RelationShipFriends status) throws BadRequestException {
        validateUserIds(userIdFrom, userIdTo);

        RelationShipFrnds relationShipFrnds = relationShipDAOImpl.findRelByFromTo(userIdFrom, userIdTo);
        if (relationShipFrnds == null)
            throw new BadRequestException("You have to add friends " + userIdTo);

        if (status == RelationShipFriends.CANCEL && relationShipFrnds.getStatus() == RelationShipFriends.PENDING) {
            relationShipFrnds.setStatus(status);

        } else if (status == RelationShipFriends.DECLINE && relationShipFrnds.getStatus() == RelationShipFriends.PENDING) {
            relationShipFrnds.setStatus(status);

        } else if (status == RelationShipFriends.ACCEPT && relationShipFrnds.getStatus() == RelationShipFriends.PENDING) {
            relationShipFrnds.setStatus(status);

        } else if (status == RelationShipFriends.DELETE && relationShipFrnds.getStatus() == RelationShipFriends.ACCEPT) {
            relationShipFrnds.setStatus(status);

        } else if (status == RelationShipFriends.PENDING) {

            if (relationShipFrnds.getStatus() == RelationShipFriends.PENDING || relationShipFrnds.getStatus() == RelationShipFriends.ACCEPT)
                throw new BadRequestException("Updating from status " + relationShipFrnds.getStatus() +
                        " to status " + status + " does not allowed");
            relationShipFrnds.setStatus(status);

        } else {
            throw new BadRequestException("Updating from status " + relationShipFrnds.getStatus() +
                    " to status " + status + " does not allowed");
        }
        return relationShipDAOImpl.update(relationShipFrnds);
    }

    public boolean validateUserIds(Long userIdFrom, Long userIdTo) throws BadRequestException {
        if (userIdTo == null || userIdTo <= 0)
            throw new BadRequestException("Wrong userIdTo. Try again");
        if (userIdFrom == userIdTo)
            throw new BadRequestException("You can not send request  myself");

        User userFind = userDAO.findOne(userIdTo);

        if (userFind == null)
            throw new BadRequestException("User with id " + userIdTo + "does not exist in DB");
        return true;

    }

    public List<RelationShipFrnds> findRelByIdAnsw(Long id) {
        return relationShipDAOImpl.findRelsByIdAnsw(id);
    }


}
