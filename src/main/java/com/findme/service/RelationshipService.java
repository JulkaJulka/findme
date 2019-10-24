package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.List;

@Service
public class RelationshipService {

    private static long MAX_AMOUNT_OUTCOME_REQUESTS = 10;

    @Autowired
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private DispenseChain dispenseChain;

    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException {
        validateUserIds(userIdFrom, userIdTo);

        List<Long> listPending = relationShipFrndsDAO.getRelationsByStatus(userIdFrom.toString(), RelationShipFriends.PENDING);

        if (listPending != null && listPending.size() >= MAX_AMOUNT_OUTCOME_REQUESTS)
            throw new LimitExceed("You have exceed limit of outcome requests");

        RelationShipFrnds relationShipFrndsFind = relationShipFrndsDAO.findRelByFromTo(userIdFrom, userIdTo);

        if (relationShipFrndsFind == null) {
            RelationShipFrnds relationShipFrnds = new RelationShipFrnds();
            relationShipFrnds.setUserFrom(userIdFrom);
            relationShipFrnds.setUserTo(userIdTo);
            relationShipFrnds.setStatus(RelationShipFriends.PENDING);

            relationShipFrndsDAO.save(relationShipFrnds);
        } else {
            throw new BadRequestException("You have had relationShip with id " + userIdTo + " already");
        }
    }

    public List<Long> getIncomeRequests(String userId) {
        return relationShipFrndsDAO.getIncomeRequests(userId);
    }

    public List<Long> getOutcomeRequests(String userId) {
        return relationShipFrndsDAO.getOutcomeRequests(userId);
    }

    public RelationShipFrnds updateRelationshipStatus(Long userIdFrom, Long userIdTo, RelationShipFriends status) throws BadRequestException {
        validateUserIds(userIdFrom, userIdTo);
        RelationShipFrnds relationShipFrnds = relationShipFrndsDAO.findRelByFromTo(userIdFrom, userIdTo);
        dispenseChain.init(status, relationShipFrnds);
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

    public List<RelationShipFrnds> findRelByIdAnsw(Long id) {
        return relationShipFrndsDAO.findRelsByIdAnsw(id);
    }


}
