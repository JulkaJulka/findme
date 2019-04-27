package com.findme.dao;

import com.findme.model.User;
import org.hibernate.query.NativeQuery;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO{
   private static String FIND_USER_BY_PHONE_EMAIL = "FROM User WHERE email = :email OR phone = :phone";
   //private static String FIND_USER_BY_PHONE_EMAIL = " SELECT * FROM USER_FM WHERE EMAIL =? OR PHONE = ?";

    public List<User> findByPhoneOrEmail(User user){
     // NativeQuery<User> query = (NativeQuery<User>) getEntityManager().createNativeQuery(FIND_USER_BY_PHONE_EMAIL, User.class);

      //  user = query.setParameter(1, user.getPhone()).setParameter(2, user.getEmail()).uniqueResult();

        Query query = getEntityManager().createQuery(FIND_USER_BY_PHONE_EMAIL);
       query.setParameter("email", user.getEmail());
        query.setParameter("phone", user.getPhone());
       // System.out.println(user.toString());
        if(query.getResultList().isEmpty())
            return null;
        System.out.println(query.getResultList().toString());
        return query.getResultList();
    }
}
