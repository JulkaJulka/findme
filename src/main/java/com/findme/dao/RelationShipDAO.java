package com.findme.dao;

import com.findme.model.RelationShipFrnds;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationShipDAO extends GenericDAO<RelationShipFrnds>{
    void answerToRequestFriend(Long idReq, Long idAnsw);
    List<Long>getIncomeRequests(String userId);
    List<Long>getOutcomeRequests(String userId);
    List<RelationShipFrnds> findRelsByIdAnsw(Long id);

}
