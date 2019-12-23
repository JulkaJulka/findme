package com.findme.validator;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.LimitExceed;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;

public interface Chain {

    void setNextChain(Chain nextChain);
    void check(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, InternalServerError, LimitExceed;

}
