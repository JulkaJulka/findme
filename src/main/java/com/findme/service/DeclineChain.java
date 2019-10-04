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
public class DeclineChain extends ChainGeneral {

@Autowired
    public DeclineChain(RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
    }

    @Override
    public void dispense(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed {
        if (status == RelationShipFriends.DECLINE && getRelationShipFrnds().getStatus() == RelationShipFriends.PENDING) {
            getRelationShipFrnds().setStatus(status);
            relationShipFrnds.setDate_status(new Date());
           getRelationShipDAOImpl().update(getRelationShipFrnds());
    } else {
        this.chain.dispense(status,relationShipFrnds);}
    }
}
