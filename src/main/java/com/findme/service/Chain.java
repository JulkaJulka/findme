package com.findme.service;

import com.findme.BadRequestException;
import com.findme.LimitExceed;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;

public interface Chain {

    void setNextChain(Chain nextChain);
    void dispense(RelationShipFriends status, RelationShipFrnds relationShipFrnds) throws BadRequestException, LimitExceed;
}
