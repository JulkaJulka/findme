package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PendingChain extends ChainGeneral {

   @Autowired
    public PendingChain(RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
    }

    @Override
    public void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, InternalServerError, LimitExceed {
        if (status == RelationShipFriends.PENDING) {

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
