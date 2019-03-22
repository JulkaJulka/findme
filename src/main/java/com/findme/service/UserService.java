package com.findme.service;

import com.findme.BadRequestException;
import com.findme.NotFoundException;
import com.findme.Utils;
import com.findme.dao.GenericDAOImpl;
import com.findme.dao.UserDAO;
import com.findme.dao.UserDAOImpl;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
@Autowired
    private GenericDAOImpl<User> userDAO;

    public User findOne(Long id) throws NotFoundException, BadRequestException {
        Utils.checkWordOnDigts(id.toString());
        User user = userDAO.findOne( id);
        if (user == null)
            throw new NotFoundException("User id " + id + " not found in DB");
        return userDAO.findOne( id);
    }

    public User save(User entity) {
        return userDAO.save(entity);
    }

    public void delete(Long id) throws NotFoundException, BadRequestException {
        Utils.checkWordOnDigts(id.toString());
        checkExistenceEntityInDB(id);
        userDAO.delete(id);
    }

    public User update(User entity) throws BadRequestException, NotFoundException  {
        Utils.checkWordOnDigts(entity.getId().toString());
        checkExistenceEntityInDB(entity.getId());
        return  userDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException, NotFoundException{
        User findEntity = findOne( id);
        if(findEntity == null) throw new NotFoundException(  " with id " + id + "doesn't exist in DB" );
        return true;
    }
}
