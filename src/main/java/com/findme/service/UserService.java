package com.findme.service;

import com.findme.BadRequestException;
import com.findme.NotFoundException;
import com.findme.dao.GenericDAOImpl;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
@Autowired
    private GenericDAOImpl<User> userDAO;

    public User findOne(Long id) throws NotFoundException {
        User user = userDAO.findOne( id);
        if (user == null)
            throw new NotFoundException("User id " + id + " not found in DB");
        return user;
    }

    public User save(User entity) {
        return userDAO.save(entity);
    }

    public void delete(Long id) throws NotFoundException, BadRequestException {
        checkExistenceEntityInDB(id);
        userDAO.delete(id);
    }

    public User update(User entity) throws BadRequestException, NotFoundException  {
        checkExistenceEntityInDB(entity.getId());
        return  userDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException, NotFoundException{
        User findEntity = findOne( id);
        if(findEntity == null) throw new NotFoundException(  " with id " + id + "doesn't exist in DB" );
        return true;
    }
}
