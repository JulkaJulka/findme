package com.findme.dao;

import com.findme.exception.InternalServerError;
import com.findme.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends GenericDAO<User>{
     List<User> findByPhoneOrEmail(User user);
     User checkExistUsDB(String email, String password) throws InternalServerError;
}
