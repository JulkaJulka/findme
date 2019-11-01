package com.findme.validator;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class AcceptChain extends ChainGeneral {

    private static long LIMIT_FRIENDS = 100;
@Autowired
    public AcceptChain(RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
    }

    @Override
    public void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed {
        if (status == RelationShipFriends.ACCEPT && relationShipFrnds.getStatus() == RelationShipFriends.PENDING) {
            List<Long> listAccept = relationShipDAOImpl.getRelationsByStatus(relationShipFrnds.getUserFrom().toString(), RelationShipFriends.ACCEPT);
            if (listAccept != null && listAccept.size() >= LIMIT_FRIENDS)
                throw new LimitExceed("You have already exceed limit of friends " + LIMIT_FRIENDS);
            relationShipFrnds.setStatus(status);
            relationShipFrnds.setDate_status(new Date());
            getRelationShipDAOImpl().update(relationShipFrnds);
        } else {
            this.chain.check(status, relationShipFrnds);
        }
    }
}
