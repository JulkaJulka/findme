package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import com.findme.validator.Handler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RelationshipService {

    public long MAX_AMOUNT_OUTCOME_REQUESTS = 10;

    @Autowired
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private Handler handler;

    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException,InternalServerError {

        validateUserIds(userIdFrom, userIdTo);
        checkLimitOutRequests(userIdFrom);

        RelationShipFrnds relationShipFrndsFind = relationShipFrndsDAO.findRelByFromTo(userIdFrom, userIdTo);

        if (relationShipFrndsFind == null) {
            RelationShipFrnds relationShipFrnds = new RelationShipFrnds();
            relationShipFrnds.setUserFrom(userIdFrom);
            relationShipFrnds.setUserTo(userIdTo);
            relationShipFrnds.setStatus(RelationShipFriends.PENDING);
            relationShipFrnds.setDate_status(new Date());

            relationShipFrndsDAO.save(relationShipFrnds);

        }
        if (relationShipFrndsFind != null) {
            throw new BadRequestException("You have had relationShip with id " + userIdTo + " already");
        }
    }

    public RelationShipFrnds updateRelationshipStatus(Long userIdFrom, Long userIdTo, RelationShipFriends status) throws BadRequestException, InternalServerError {

        validateUserIds(userIdFrom, userIdTo);
        RelationShipFrnds relationShipFrnds = relationShipFrndsDAO.findRelByFromTo(userIdFrom, userIdTo);

        handler.init(status, relationShipFrnds);

        return relationShipFrnds;

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

    public void checkLimitOutRequests(Long userIdFrom) throws LimitExceed {
        List<Long> listPending = relationShipFrndsDAO.getRelationsByStatus(userIdFrom.toString(), RelationShipFriends.PENDING);

        if (listPending != null && listPending.size() >= MAX_AMOUNT_OUTCOME_REQUESTS)
            throw new LimitExceed("You have exceed limit of outcome requests");
    }


}
