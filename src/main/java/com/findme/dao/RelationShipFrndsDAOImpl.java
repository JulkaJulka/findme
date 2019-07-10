package com.findme.dao;

import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class RelationShipFrndsDAOImpl extends GenericDAOImpl<RelationShipFrnds> implements RelationShipDAO {

    private static String FIND_RELATION_BY_ID = "FROM RelationShipFrnds WHERE userFrom = :idFrom AND userTo = :idTo";
    private static String INSERT_RELATION = "INSERT INTO  RelationShipFrnds(USER_FROM, USER_TO, STATUS) VALUES(?,?,?)";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void addRelationship(Long userIdFrom, Long userIdTo) {
        RelationShipFrnds findRelFrnds = findRelByFromTo(userIdFrom, userIdTo);

        if (findRelFrnds == null) {

         RelationShipFrnds relationShipNewFrnds = new RelationShipFrnds();
         relationShipNewFrnds.setUserFrom(userIdFrom);
         relationShipNewFrnds.setUserTo(userIdTo);
         relationShipNewFrnds.setStatus(RelationShipFriends.REQUEST_SEND);

         save(relationShipNewFrnds);

        } else {

            findRelFrnds.setStatus(RelationShipFriends.REQUEST_SEND);
            update(findRelFrnds);
        }
    }


    public RelationShipFrnds findRelByFromTo(Long userFrom, Long userTo) {
        Query query = getEntityManager().createQuery(FIND_RELATION_BY_ID);
        query.setParameter("idFrom", userFrom);
        query.setParameter("idTo", userTo);

        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        else if (results.size() == 1)
            return (RelationShipFrnds) results.get(0);

        throw new NonUniqueResultException();
    }


}
