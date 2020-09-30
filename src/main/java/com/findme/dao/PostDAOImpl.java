package com.findme.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.exception.BadRequestException;
import com.findme.model.*;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.*;

@Repository
public class PostDAOImpl extends GenericDAOImpl<Post> implements PostDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Post> listPostsOfUserPagedId(Filter filter, String userPagedId) throws BadRequestException {

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> filterParams = objectMapper.convertValue(filter, Map.class);

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery cq = cb.createQuery(Post.class);
        Root<Post> rootPost = cq.from(Post.class);

        rootPost.fetch("usersTagged", JoinType.LEFT);
        rootPost.fetch("userPosted", JoinType.LEFT);
        rootPost.fetch("userPagePosted", JoinType.LEFT);

        Predicate p = cb.equal(rootPost.get("userPagePosted").get("id"), Long.parseLong(userPagedId));
        CriteriaQuery cq1 = cq.where(p);

        validFilterParam(filterParams);

        for (String param : filterParams.keySet()) {
            if (filterParams.get(param) != null) {

                if (param.equals("idUser")) {
                    cq1 = cq1.where(cb.and(p, cb.equal(
                            rootPost.get("userPosted").get("id"), filterParams.get(param))));
                }
                if (param.equals("id")) {
                    cq1 = cq1.where(cb.and(p, cb.notEqual(
                            rootPost.get("userPosted").get("id"), filterParams.get(param))));
                }
            }

        }
        return entityManager.createQuery(cq1).getResultList();
    }

    private void validFilterParam(Map<String, Object> filterParams) throws BadRequestException {
        int count = 0;
        for (Object v : filterParams.values()) {
            if (v != null) {
                count++;
            }
        }
        if (count > 1)
            throw new BadRequestException("input only 1 parametr");
    }


}
