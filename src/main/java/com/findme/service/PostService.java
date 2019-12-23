package com.findme.service;

import com.findme.exception.BadRequestException;
import com.findme.dao.GenericDAOImpl;
import com.findme.exception.InternalServerError;
import com.findme.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {
    @Autowired
    private GenericDAOImpl<Post> postDAO;

    public Post findOne(Long id) {
        return postDAO.findOne(id);
    }

    public Post save(Post entity) throws InternalServerError {
        return postDAO.save(entity);
    }

    public void delete(Long id) throws BadRequestException {
        checkExistenceEntityInDB(id);
        postDAO.delete(id);
    }

    public Post update(Post entity) throws BadRequestException, InternalServerError {
        checkExistenceEntityInDB(entity.getId());
        return  postDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException{
        Post findEntity = findOne( id);
        if(findEntity == null) throw new BadRequestException(  " with id " + id + "doesn't exist in DB" );
        return true;
    }
}
