package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;

public abstract class ChainGeneral implements Chain {


    public Chain chain;
    public RelationShipFrndsDAOImpl relationShipDAOImpl;
    public RelationShipFrnds relationShipFrnds;
    public UserDAOImpl userDAO;



   public ChainGeneral() {
    }
    public ChainGeneral(RelationShipFrndsDAOImpl relationShipDAOImpl) {
        this.relationShipDAOImpl = new RelationShipFrndsDAOImpl();
    }

    public ChainGeneral(RelationShipFrndsDAOImpl relationShipDAOImpl, UserDAOImpl userDAO) {
        this.relationShipDAOImpl = new RelationShipFrndsDAOImpl();
        this.userDAO = new UserDAOImpl();
    }
    public RelationShipFrndsDAOImpl getRelationShipDAOImpl() {
        return relationShipDAOImpl;
    }

    @Override
    public void setNextChain(Chain nextChain) {
        this.chain = nextChain;
    }

    @Override
    public abstract void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, InternalServerError, LimitExceed;

    public RelationShipFrnds getRelationShipFrnds() {
        return relationShipFrnds;
    }


}
