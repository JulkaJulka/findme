package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.model.User;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO {
    private static String FIND_USER_BY_PHONE_EMAIL = "FROM User WHERE email = :email OR phone = :phone";
    private static String FIND_USER_BY_EMAIL_PSWD = "FROM User WHERE email = :email AND password = :password";

    @PersistenceContext
    private EntityManager entityManager;

    public List<User> findByPhoneOrEmail(User user) {

        Query query = getEntityManager().createQuery(FIND_USER_BY_PHONE_EMAIL);
        query.setParameter("email", user.getEmail());
        query.setParameter("phone", user.getPhone());

        if (query.getResultList().isEmpty())
            return null;
        System.out.println(query.getResultList().toString());
        return query.getResultList();
    }

    @Transactional
    @Override
    public User checkExistUsDB(String email, String password) throws NonUniqueResultException, InternalServerError {

        Query query = getEntityManager().createQuery(FIND_USER_BY_EMAIL_PSWD);
        query.setParameter("email", email);
        query.setParameter("password", password);

        List results = query.getResultList();

        if (results.isEmpty())
            return null;

        else if (results.size() == 1)
            return (User) results.get(0);

        throw new NonUniqueResultException();

    }


}
