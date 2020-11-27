package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Controller
@EnableWebMvc
public class UserController {

    private UserService userService;

    private final static Logger logger = Logger.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;

    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session, Model model, @PathVariable String userId) throws Exception {

        Long idLong = Long.valueOf(userId);
        model.addAttribute("userId", idLong);

        User user = userService.findOne(idLong);
        model.addAttribute("user", user);

        logger.info("SessionId: " + session.getId() + "; /user/" + userId);
        return "profile";

    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> registerUser(HttpSession session, @ModelAttribute User user) throws Exception {

        user.setDateRegistrated(new Date());
        user.setLastDateActivited(new Date());

        logger.info("SessionId: " + session.getId() + "; Saving new user");
        userService.save(user);
        logger.info("SessionId: " + session.getId() + "; User registered. UserId: " + user.getId());

        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> loginUser(HttpServletRequest request, @RequestParam("emailLog") String email,
                                            @RequestParam("passwordLog") String password) throws Exception {

        HttpSession session = request.getSession();

        User userFound = userService.checkExistanceUserInDB(email, password);

        if (userFound == null) {
            logger.warn("SessionId: " + session.getId() + "; User with email: " + email + " password: " + password + " not found");
            return new ResponseEntity<>("Wrong password or email. Try again please.", HttpStatus.FORBIDDEN);
        } else {
            session.setAttribute("user", userFound);
            logger.info("SessionId: " + session.getId() + "; User successfully logged. UserId: " + userFound.getId());
            return new ResponseEntity<>("User successfully log in to FindMe", HttpStatus.OK);
        }

    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logoutUser(HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String id = session.getAttribute("id").toString();
        session.removeAttribute("email");
        session.removeAttribute("id");

        logger.info("SessionId: " + session.getId() + "; User successfully logout. UserId: " + id);
        return new ResponseEntity<>("User logout successfully", HttpStatus.OK);

    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndex() {
        return "index";
    }

    @RequestMapping(path = "/profile", method = RequestMethod.GET)
    public String getProfile() {
        return "profile";
    }


    @RequestMapping(method = RequestMethod.POST, value = "/User/save", produces = "application/json")
    public @ResponseBody
    String saveUser(HttpSession session, @RequestBody User user) throws Exception {

        logger.info("SessionId: " + session.getId() + "; Saving new user");
        User user1 = userService.save(user);
        logger.info("SessionId: " + session.getId() + "; User has been saved. UserId: " + user.getId());

        return user1 + "was saving successful";

    }

    @RequestMapping(method = RequestMethod.PUT, value = "/User/update", produces = "application/json")
    public @ResponseBody
    String updateUser(HttpSession session, @RequestBody User user) throws Exception {

        logger.info("SessionId: " + session.getId() + "; Update user Id: " + user.getId());
        userService.update(user);
        logger.info("SessionId: " + session.getId() + "; User has updated. UserId: " + user.getId());
        return user.toString() + "was updating successful";
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/User/delete", produces = "application/json")
    public @ResponseBody
    String deleteUser(HttpServletRequest req) throws Exception {


        String id = req.getParameter("idUser");
        Long idUser = Long.parseLong(id);

        logger.info("SessionId: " + req.getSession().getId() + "; Delete user Id: " + id);
        userService.delete(idUser);
        logger.info("SessionId: " + req.getSession().getId() + "; User has successfully deleted. UserId: " + id);
        return "OK" + "User id " + idUser + " was deleted successfully";


    }


}
