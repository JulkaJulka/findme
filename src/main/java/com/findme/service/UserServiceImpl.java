package com.findme.service;

import com.findme.dao.UserDAO;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, UserDAO> implements UserService {
@Autowired
    private UserDAO userDAO;
}
