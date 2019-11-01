package com.findme.validator;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;

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
    public abstract void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed;

    public RelationShipFrnds getRelationShipFrnds() {
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
}
