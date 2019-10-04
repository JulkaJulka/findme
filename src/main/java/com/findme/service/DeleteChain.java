package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Component
public class DeleteChain extends ChainGeneral {


    @Autowired
    public DeleteChain(RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.relationShipDAOImpl = relationShipFrndsDAO;
    }

    @Override
    public void dispense(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed {
        if (status == RelationShipFriends.DELETE && relationShipFrnds.getStatus() == RelationShipFriends.ACCEPT) {
            Calendar calMax = Calendar.getInstance();
            Calendar dateStatus = new GregorianCalendar();
            dateStatus.setTime(relationShipFrnds.getDate_status());

            calMax.add(Calendar.DATE, -3);
            Date dateMaxForCompare = calMax.getTime();


            if (relationShipFrnds.getStatus() == RelationShipFriends.ACCEPT) {
                if (dateMaxForCompare.compareTo(dateStatus.getTime()) >= 0) {
                    relationShipFrnds.setStatus(status);
                    relationShipFrnds.setDate_status(new Date());
                    getRelationShipDAOImpl().update(relationShipFrnds);
                } else {
                    throw new BadRequestException("You have not permission to delete id " + relationShipFrnds.getUserTo());
                }
            }

        } else

        {
        this.chain.dispense(status, relationShipFrnds);
        }
}
}
