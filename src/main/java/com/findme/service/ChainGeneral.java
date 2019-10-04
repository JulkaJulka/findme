package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class ChainGeneral implements Chain {

    public Chain chain;

    public RelationShipFrndsDAOImpl relationShipDAOImpl;
    public RelationShipFrnds relationShipFrnds;
    public static long MAX_AMOUNT_OUTCOME_REQUESTS = 10;
    public static long LIMIT_FRIENDS = 100;


   public ChainGeneral() {
    }

    public ChainGeneral(RelationShipFrndsDAOImpl relationShipDAOImpl) {
        this.relationShipDAOImpl = new RelationShipFrndsDAOImpl();
    }
    public RelationShipFrndsDAOImpl getRelationShipDAOImpl() {
        return relationShipDAOImpl;
    }

    @Override
    public void setNextChain(Chain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public abstract void dispense(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed;

    public RelationShipFrnds getRelationShipFrnds() {
        return relationShipFrnds;
    }
}

/*




    public void setRelationShipFrnds(RelationShipFrnds relationShipFrnds) {
        this.relationShipFrnds = relationShipFrnds;
    }
}
*/