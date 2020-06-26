package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.model.Post;
import com.findme.model.User;
import com.findme.service.PostService;
import com.findme.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Controller
@EnableWebMvc
public class PostController {

    private PostService postService;
    private UserService userService;

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(path = "/post-create", method = RequestMethod.POST, produces = {"application/json", "application/x-www-form-urlencoded"})
    public @ResponseBody
    String createPost(HttpServletRequest request, @RequestParam("userIdPagePosted") String userIdPagePosted) {
        try {
            HttpSession session = request.getSession();

            User userPosted = (User) session.getAttribute("user");
            userService.findOne(userPosted.getId());

            Long idUserTo = Long.parseLong(userIdPagePosted);

            Post post = convertJSONStringToPost(request);

            post.setDatePosted(new Date());
            post.setUserPosted(userPosted);

            User userPagePosted = userService.findOne(idUserTo);

            post.setUsersTagged(postService.createTaggedUsersFromMessage(post.getMessage()));

            post.setUserPagePosted(userPagePosted);
            postService.save(post, userPosted.getId(), idUserTo);
            return post.toString() + "Post is registered successfully";

        } catch (BadRequestException | NotFoundException e) {
            return "Save unsuccessful " + e.getMessage();
        } catch (InternalServerError e) {
            return "Something went wrong..." + e.getMessage();
        }
    }

    @RequestMapping(path = "/listPost", method = RequestMethod.POST, produces = "application/json")
    public @ResponseBody
    List<Post> listPostByUserPagePostedId(@RequestParam String userId) {
        try {
            return postService.postListByUserPagePostedId(userId);
        } catch (BadRequestException e) {
            return null;
        }
    }


    private Post convertJSONStringToPost(HttpServletRequest req) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            Post post = mapper.readValue(is, Post.class);

            return post;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
