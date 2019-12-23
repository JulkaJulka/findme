package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.dao.UserDAOImpl;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
    public User save(User entity) throws BadRequestException, InternalServerError {
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

    public User update(User entity) throws BadRequestException, InternalServerError, NotFoundException {
        checkExistenceEntityInDB(entity.getId());
        return userDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException, NotFoundException {
        User findEntity = findOne(id);
        if (findEntity == null) throw new NotFoundException(" with id " + id + "doesn't exist in DB");
        return true;
    }


    public User checkExistanceUserInDB(String email, String password) throws InternalServerError {
        User findUser = userDAO.checkExistUsDB(email, password);
        if(findUser == null)
            return null;
        return findUser;
    }


}
