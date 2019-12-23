package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class CancelChain extends ChainGeneral {



@Autowired
    public CancelChain(RelationShipFrndsDAOImpl relationShipFrndsDAO, UserDAOImpl userDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
        this.userDAO = userDAO;
    }



    @Override
    public void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, InternalServerError, LimitExceed {

        if (relationShipFrnds == null)
            throw new BadRequestException("You have to add friends " + relationShipFrnds.getUserTo());

        if (status == RelationShipFriends.CANCEL && relationShipFrnds.getStatus() == RelationShipFriends.PENDING) {
            relationShipFrnds.setStatus(status);
            relationShipFrnds.setDate_status(new Date());
            getRelationShipDAOImpl().update(relationShipFrnds);


    } else {
            this.chain.check(status,relationShipFrnds);
        }
    }
}
