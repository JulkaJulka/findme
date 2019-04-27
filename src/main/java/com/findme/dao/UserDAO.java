package com.findme.dao;

import com.findme.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDAO extends GenericDAO<User>{
     List<User> findByPhoneOrEmail(User user);
}
