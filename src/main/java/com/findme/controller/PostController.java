package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.model.Filter;
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
    String createPost(HttpServletRequest request,@RequestBody Post post, @RequestParam("userIdPagePosted") String userIdPagePosted){
        try {
            HttpSession session = request.getSession();

            User userPosted = (User) session.getAttribute("user");

            Long idUserTo = Long.parseLong(userIdPagePosted);
            post.setUserPagePosted(userService.setUserPagePosted(userPosted.getId(),idUserTo));

            post.setDatePosted(new Date());
            post.setUserPosted(userPosted);

            post.setUsersTagged(postService.createTaggedUsersFromMessage(post.getMessage()));

            postService.save(post, userPosted.getId(), idUserTo);
            return post.toString() + "Post is registered successfully";

        } catch (BadRequestException | NotFoundException e) {
            return "Save unsuccessful " + e.getMessage();
        } catch (InternalServerError e) {
            return "Something went wrong..." + e.getMessage();
        }}


    @RequestMapping(path = "/posts-by-user-page", method = RequestMethod.POST, produces = {"application/json", "application/x-www-form-urlencoded"})
        public  @ResponseBody List<Post> allPostsByUserPagedId(@RequestBody Filter filter, @RequestParam("idUserPage") String idUserPage) throws BadRequestException{

                return postService.allPostsUserPaged(filter, idUserPage);

        }

}
