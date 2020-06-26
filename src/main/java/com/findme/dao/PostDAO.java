package com.findme.dao;

import com.findme.model.Post;
import com.findme.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostDAO extends GenericDAO<Post> {
    List<Post> listAllPost();
    List<Post> listPostByUserPagePostedId(String userId);
}
