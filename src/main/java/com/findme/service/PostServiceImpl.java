package com.findme.service;

import com.findme.dao.GenericDAO;
import com.findme.dao.PostDAO;
import com.findme.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostServiceImpl extends GenericServiceImpl<Post, PostDAO> implements PostService {
@Autowired
    private PostDAO postDAO;
}
