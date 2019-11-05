package com.findme.validator;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;

public interface Chain {

    void setNextChain(Chain nextChain);
    void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed;
    boolean validateUserIds(Long userIdFrom, Long userIdTo) throws BadRequestException;

}
