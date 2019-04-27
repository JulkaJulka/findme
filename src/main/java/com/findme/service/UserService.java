package com.findme.service;

import com.findme.BadRequestException;
import com.findme.NotFoundException;
import com.findme.dao.GenericDAO;
import com.findme.dao.GenericDAOImpl;
import com.findme.dao.UserDAO;
import com.findme.dao.UserDAOImpl;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserDAOImpl userDAO;

    public User findOne(Long id) throws NotFoundException {
        User user = userDAO.findOne(id);
        if (user == null)
            throw new NotFoundException("User id " + id + " not found in DB");
        return user;
    }

    @Transactional
    public User save(User entity) throws BadRequestException {
        if (userDAO.findByPhoneOrEmail(entity) != null)
            throw new BadRequestException("User with email " + entity.getEmail() + " or phone " + entity.getPhone() + " already exist in DB. Try again, please");

      //  System.out.println(userDAO.findByPhoneOrEmail(entity).toString());
        User saveUser = userDAO.save(entity);
        return saveUser;


    }

    public void delete(Long id) throws NotFoundException, BadRequestException {
        checkExistenceEntityInDB(id);
        userDAO.delete(id);
    }

    public User update(User entity) throws BadRequestException, NotFoundException {
        checkExistenceEntityInDB(entity.getId());
        return userDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException, NotFoundException {
        User findEntity = findOne(id);
        if (findEntity == null) throw new NotFoundException(" with id " + id + "doesn't exist in DB");
        return true;
    }
}
