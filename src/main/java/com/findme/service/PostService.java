package com.findme.service;

import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.exception.BadRequestException;
import com.findme.dao.GenericDAOImpl;
import com.findme.exception.InternalServerError;
import com.findme.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private GenericDAOImpl<Post> postDAO;
    @Autowired
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;

    public Post findOne(Long id) {
        return postDAO.findOne(id);
    }

    public Post save(Post entity, Long idFrom, Long idTo) throws InternalServerError, BadRequestException {
        if (entity == null)
            throw new BadRequestException("Post doesn't be empty.Pls, write the text");

        if (idFrom != idTo) {
            if (!relationShipFrndsDAO.isBetweenUsersAccept(idFrom, idTo))
                throw new BadRequestException("You do not have permission. Add to your friends");
        }
        return postDAO.save(entity);
    }

    public void delete(Long id) throws BadRequestException {
        checkExistenceEntityInDB(id);
        postDAO.delete(id);
    }

    public Post update(Post entity) throws BadRequestException, InternalServerError {
        checkExistenceEntityInDB(entity.getId());
        return postDAO.update(entity);

    }

    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException {
        Post findEntity = findOne(id);
        if (findEntity == null) throw new BadRequestException(" with id " + id + "doesn't exist in DB");
        return true;
    }

}
