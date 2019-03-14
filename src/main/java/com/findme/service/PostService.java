package com.findme.service;

import com.findme.dao.GenericDAO;
import com.findme.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface PostService extends GenericService<Post> {

}
