package com.findme.dao;

import com.findme.exception.BadRequestException;
import com.findme.model.Filter;
import com.findme.model.Post;
import com.findme.model.User;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO extends GenericDAO<Post>  {

    List<Post> listPostsOfUserPagedId(Filter filter,String userPagedId) throws BadRequestException;

}
