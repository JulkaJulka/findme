package com.findme.dao;

import com.findme.model.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDAO{
}
