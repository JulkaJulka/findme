package com.findme.dao;

import com.findme.model.Post;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PostDAOImpl extends GenericDAOImpl<Post> implements PostDAO {

    private static String FIND_ALL_EXIST_POSTS = " SELECT * FROM POST p JOIN(SELECT put.POST_ID, p1.ID_POST as p1_ID, put.USER_TAGGED_ID FROM POST_USER_TAGGED put RIGHT JOIN POST p1 ON p1.ID_POST = put.POST_ID ) un " +
            "ON p.ID_POST = un.p1_ID LEFT JOIN USER_FM ON un.USER_TAGGED_ID = ID_USER ORDER BY p.DATE_POSTED DESC";

private static String FIND_POSTS_BY_USER_PAGE_POSTED_ID =  " SELECT * FROM POST p JOIN(SELECT put.POST_ID, p1.ID_POST as p1_ID, put.USER_TAGGED_ID FROM POST_USER_TAGGED put RIGHT JOIN POST p1 ON p1.ID_POST = put.POST_ID " +
        "WHERE p1.USER_PAGE_POSTED_ID = ?) un ON p.ID_POST = un.p1_ID LEFT JOIN USER_FM ON un.USER_TAGGED_ID = ID_USER ORDER BY p.DATE_POSTED DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    @Override
    public List<Post> listPostByUserPagePostedId(String userId) {

        Query query = getEntityManager().createNativeQuery(FIND_POSTS_BY_USER_PAGE_POSTED_ID);
        query.setParameter(1, Long.parseLong(userId) );
        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        return results;
    }


    @Override
    public List<Post> listAllPost() {

        Query query = getEntityManager().createNativeQuery(FIND_ALL_EXIST_POSTS);

        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        return results;
    }


}
