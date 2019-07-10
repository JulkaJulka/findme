package com.findme.dao;

import com.findme.model.RelationShipFrnds;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationShipDAO extends GenericDAO<RelationShipFrnds>{
    void addRelationship(Long userIdFrom, Long userIdTo);
}
