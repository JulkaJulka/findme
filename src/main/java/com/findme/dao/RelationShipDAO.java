package com.findme.dao;

import com.findme.model.RelationShip;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationShipDAO extends GenericDAO<RelationShipFrnds>{
    void addRelationship(Long userIdFrom, Long userIdTo);
    void answerToRequestFriend(Long idReq, Long idAnsw);
    List<Long>getIncomeRequests(String userId);
    List<Long>getOutcomeRequests(String userId);
    List<RelationShipFrnds> findRelsByIdAnsw(Long id);
    RelationShipFrnds updateRelationship(Long userIdFrom, Long userIdTo, RelationShipFriends status);

}
