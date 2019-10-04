package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.service.ChainGeneral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class PendingChain extends ChainGeneral {

   @Autowired
    public PendingChain(RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
    }
    

    @Override
    public void dispense(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed {
        if (status == RelationShipFriends.PENDING) {

            List<Long> listPending = relationShipDAOImpl.getRelationsByStatus(relationShipFrnds.getUserFrom().toString(), RelationShipFriends.PENDING);
            if (listPending != null && listPending.size() >= MAX_AMOUNT_OUTCOME_REQUESTS)
                throw new LimitExceed("You have already exceed limit max value of outcome requests " + MAX_AMOUNT_OUTCOME_REQUESTS);

            if (relationShipFrnds.getStatus() == RelationShipFriends.PENDING || relationShipFrnds.getStatus() == RelationShipFriends.ACCEPT)
                throw new BadRequestException("Updating from status " + relationShipFrnds.getStatus() +
                        " to status " + status + " does not allowed");
            relationShipFrnds.setStatus(status);
            relationShipFrnds.setDate_status(new Date());
            getRelationShipDAOImpl().update(relationShipFrnds);

        } else {
            throw new BadRequestException("Updating from status " + relationShipFrnds.getStatus() +
                    " to status " + status + " does not allowed");
        }
    }
}
