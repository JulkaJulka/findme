package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import com.findme.validator.DispenseChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RelationshipService {

    @Autowired
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private DispenseChain dispenseChain;

    public void addRelationship(Long userIdFrom, Long userIdTo) throws BadRequestException {
        dispenseChain.checkIds(userIdFrom, userIdTo);
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
        dispenseChain.checkIds(userIdFrom, userIdTo);
        RelationShipFrnds relationShipFrnds = relationShipFrndsDAO.findRelByFromTo(userIdFrom, userIdTo);
        dispenseChain.init(status, relationShipFrnds);
        return relationShipFrnds;
    }


}
