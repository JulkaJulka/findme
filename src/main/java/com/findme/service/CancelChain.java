package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CancelChain extends ChainGeneral {



@Autowired
    public CancelChain(RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
    }



    @Override
    public void dispense(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed {
       // validateUserIds(userIdFrom, userIdTo);

       // RelationShipFrnds relationShipFrnds = getRelationShipDAOImpl().findRelByFromTo(userIdFrom, userIdTo);
       // setRelationShipFrnds(relationShipFrnds);
        if (relationShipFrnds == null)
            throw new BadRequestException("You have to add friends " + relationShipFrnds.getUserTo());

        if (status == RelationShipFriends.CANCEL && relationShipFrnds.getStatus() == RelationShipFriends.PENDING) {
            relationShipFrnds.setStatus(status);
            relationShipFrnds.setDate_status(new Date());
            getRelationShipDAOImpl().update(relationShipFrnds);


    } else {
            this.chain.dispense(status,relationShipFrnds);
        }
    }
}
