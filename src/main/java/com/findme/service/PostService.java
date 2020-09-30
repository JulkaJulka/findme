package com.findme.service;

import com.findme.dao.PostDAOImpl;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.dao.UserDAOImpl;
import com.findme.exception.BadRequestException;
import com.findme.dao.GenericDAOImpl;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.model.Filter;
import com.findme.model.Post;
import com.findme.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PostService {
    @Autowired
    private PostDAOImpl postDAO;
    @Autowired
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;
    @Autowired
    private UserDAOImpl userDAO;
    @Autowired
    private UserService userService;

    public Post findOne(Long id) {
        return postDAO.findOne(id);
    }

    public Post save(Post entity, Long idFrom, Long idTo) throws InternalServerError, BadRequestException {
        validatePost(entity, idFrom, idTo);
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

    @Transactional
    public List<Post> allPostsUserPaged(Filter filter, String userPagedId) throws BadRequestException{
       return postDAO.listPostsOfUserPagedId(filter,userPagedId);
    }


    public Set<User> createTaggedUsersFromMessage(String message) throws NotFoundException{
        return usersTaggedList(taggedUsersId(message));
    }

    public Long[] taggedUsersId(String message) {
        if (message == null || message.isEmpty())
            System.out.println("write message,pls");
        String[] words = message.trim().split(" ");

        for (int i = 0; i <words.length ; i++) {

            if (words[i].length() > 4 && words[i].substring(0, 4).equals("Ids:")) {
                words[i] = words[i].substring(4, words[i].length());
                String[] idsS = words[i].split(",");
                Long[] idsL = new Long[idsS.length];

                for (int k = 0; k < idsS.length ; k++) {
                    idsL[k] = Long.parseLong(idsS[k]);
                }
                return idsL;

            }
        }
        return null;
    }

    public Set<User> usersTaggedList(Long[] mas) throws NotFoundException {
        if(mas == null || mas.length == 0)
            return null;
        Set<User> taggedUsers = new HashSet<>();
        for (int i = 0; i < mas.length; i++) {

            if(!userService.checkExistenceEntityInDB(mas[i]))
            throw new NotFoundException("User with Id " + mas[i] + " is not found in DB. Please, check the UserTagged list again.");

            taggedUsers.add(userService.findOne(mas[i]));
        }
        return taggedUsers;
    }




    public boolean checkExistenceEntityInDB(Long id) throws BadRequestException {
        Post findEntity = findOne(id);
        if (findEntity == null) throw new BadRequestException(" with id " + id + "doesn't exist in DB");
        return true;
    }

    private void validatePost(Post entity, Long idFrom, Long idTo) throws BadRequestException {
        if (entity != null && entity.getMessage() == null || entity.getMessage().isEmpty())
            throw new BadRequestException("Post doesn't be empty.Pls, write the text");

        if (entity.getMessage() != null && entity.getMessage().length() > 200)
            throw new BadRequestException("Max length of message must be 200 symbols");

        if (entity.getMessage().toLowerCase().contains("https://") || entity.getMessage().toLowerCase().contains("http://"))
            throw new BadRequestException("Message does not contain any references");

        if (idFrom != idTo) {
            if (!relationShipFrndsDAO.isBetweenUsersAccept(idFrom, idTo))
                throw new BadRequestException("You do not have permission. Add to your friends");
        }
    }

}
