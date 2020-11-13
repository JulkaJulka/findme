package com.findme.controller;

import com.findme.dao.PostDAOImpl;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.model.Filter;
import com.findme.model.Post;
import com.findme.model.User;
import com.findme.service.PostService;
import com.findme.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@EnableWebMvc
public class PostController {

    private PostService postService;
    private UserService userService;

    private final static Logger logger = Logger.getLogger(PostController.class);

    public PostController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @RequestMapping(path = "/feed", method = RequestMethod.POST, produces = {"application/json", "application/x-www-form-urlencoded"})
    public @ResponseBody
    List<Post> listNewsFeed(@RequestParam("userId") String userId, @RequestParam("page") String page) {

        return postService.getPage(postService.listNewsFeed(userId), Integer.parseInt(page));
    }

    @RequestMapping(path = "/post-create", method = RequestMethod.POST, produces = {"application/json", "application/x-www-form-urlencoded"})
    public @ResponseBody
    String createPost(HttpServletRequest request, @RequestBody Post post, @RequestParam("userIdPagePosted") String userIdPagePosted) {
        try {
            HttpSession session = request.getSession();

            User userPosted = (User) session.getAttribute("user");

            Long idUserTo = Long.parseLong(userIdPagePosted);
            post.setUserPagePosted(userService.setUserPagePosted(userPosted.getId(), idUserTo));

            post.setDatePosted(new Date());
            post.setUserPosted(userPosted);

            post.setUsersTagged(postService.createTaggedUsersFromMessage(post.getMessage()));

            logger.info("SessionId: " + session.getId() + "; Creating post");
            postService.save(post, userPosted.getId(), idUserTo);
            logger.info("SessionId: " + session.getId() + "; Post has ctreated successfully. PostId: " + post.getId());
            return post.toString() + "Post is registered successfully";

        } catch (BadRequestException | NotFoundException e) {
            logger.warn("SessionId: " + request.getSession().getId() + "; Post with id: " + post.getId() + " has  not created");
            logger.warn(e);
            return "Save unsuccessful " + e.getMessage();

        } catch (InternalServerError e) {
            logger.error("SessionId: " + request.getSession().getId() + "; Unexpected error during creating Post");
            logger.error(e);
            return "Something went wrong..." + e.getMessage();
        }
    }

    @RequestMapping(path = "/posts-by-user-page", method = RequestMethod.POST, produces = {"application/json", "application/x-www-form-urlencoded"})
    public @ResponseBody
    List<Post> allPostsByUserPagedId(HttpSession session, @RequestBody Filter filter, @RequestParam("idUserPage") String idUserPage) {

        try {
            logger.info("SessionId: " + session.getId() + "; Creating post");
            List<Post> posts = postService.allPostsUserPaged(filter, idUserPage);
            logger.info("SessionId: " + session.getId() + "; List posts of User userId: " + idUserPage + " made successfully.");
            return posts;

        } catch (BadRequestException e) {
            logger.warn("SessionId: " + session.getId() + "; Choose only 1 filter's parametr");
        }

        return null;
    }
}


