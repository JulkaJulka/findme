package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.exception.InternalServerError;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.stereotype.Component;

@Component
public class DispenseChain {

    private Chain chain;


    public DispenseChain(RelationShipFrndsDAOImpl relationShipFrndsDAO, UserDAOImpl userDAO) {
        this.chain = new CancelChain(relationShipFrndsDAO, userDAO);
        Chain c2 = new DeclineChain(relationShipFrndsDAO);
        Chain c3 = new AcceptChain(relationShipFrndsDAO);
        Chain c4 = new DeleteChain(relationShipFrndsDAO);
        Chain c5 = new PendingChain(relationShipFrndsDAO);

        chain.setNextChain(c2);
        c2.setNextChain(c3);
        c3.setNextChain(c4);
        c4.setNextChain(c5);
    }

    public void init( RelationShipFriends status, RelationShipFrnds relationship)throws BadRequestException, InternalServerError {
        chain.check(status, relationship);
    }
}
