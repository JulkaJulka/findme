package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;

public abstract class ChainGeneral implements Chain {

    public Chain chain;

    public RelationShipFrndsDAOImpl relationShipDAOImpl;
    public RelationShipFrnds relationShipFrnds;



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
    public abstract void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed;

    public RelationShipFrnds getRelationShipFrnds() {
        return relationShipFrnds;
    }
}
