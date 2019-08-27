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

    private static String FIND_RELATION_BY_ID = "from RelationShipFrnds WHERE USER_FROM = :idFrom AND USER_TO = :idTo ";
    private static String FIND_RELATION_BY_ID_ANSW = "SELECT r.userFrom FROM RelationShipFrnds r WHERE  r.userTo = :idAnsw AND r.status = :status ";
    private static String FIND_OUTCOME_BY_ID = "SELECT r.userTo FROM RelationShipFrnds r WHERE  r.userFrom = :idAnsw AND r.status = :status ";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void answerToRequestFriend(Long idReq, Long idAnsw) {

    }

    @Override
    public List<RelationShipFrnds> findRelsByIdAnsw(Long idAnsw) {
        Query query = getEntityManager().createQuery(FIND_RELATION_BY_ID_ANSW);
        query.setParameter("idAnsw", idAnsw);
        query.setParameter("status", RelationShipFriends.PENDING);

        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        return results;

    }

    @Override
    public List<Long> getIncomeRequests(String userId) {
        Long userIdL = Long.parseLong(userId);
        Query query = getEntityManager().createQuery(FIND_RELATION_BY_ID_ANSW);
        query.setParameter("idAnsw", userIdL);
        query.setParameter("status", RelationShipFriends.PENDING);

        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        return results;
    }

    @Override
    public List<Long> getOutcomeRequests(String userId) {
        Long userIdL = Long.parseLong(userId);
        Query query = getEntityManager().createQuery(FIND_OUTCOME_BY_ID);
        query.setParameter("idAnsw", userIdL);
        query.setParameter("status", RelationShipFriends.PENDING);

        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        return results;
    }

    /*@Override
    public RelationShipFrnds responseToRequest(Long userIdFrom, Long userIdTo, RelationShipFriends status) {
        RelationShipFrnds findRelFrnds = findRelByFromTo(userIdFrom, userIdTo);
        findRelFrnds.setStatus(status);
        return findRelFrnds;
    }*/

    public RelationShipFrnds updateRelationship(Long userIdFrom, Long userIdTo, RelationShipFriends status) {
        RelationShipFrnds findRelFrnds = findRelByFromTo(userIdFrom, userIdTo);
        findRelFrnds.setStatus(status);
        return update(findRelFrnds);
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
