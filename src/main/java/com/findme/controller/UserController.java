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
    private RelationshipService relationshipService;
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;


    private final static Logger logger = Logger.getLogger(UserController.class);

    //  @Autowired
    public UserController(UserService userService, RelationshipService relationshipService, RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.userService = userService;
        this.relationshipService = relationshipService;
        this.relationShipFrndsDAO = relationShipFrndsDAO;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(HttpSession session,Model model, @PathVariable String userId) {


        try {
            Long idLong = Long.valueOf(userId);
            model.addAttribute("userId", idLong);

            User user = userService.findOne(idLong);
            model.addAttribute("user", user);

            logger.info("SessionId: " + session.getId() + "; /user/" + userId );
            return "profile";

        } catch (NumberFormatException e) {
            logger.warn("User id has wrong format, enter only numbers. Please, try again.");
            return "bad-request-exception";

        } catch (NotFoundException e) {
            logger.warn("SessionId: " + session.getId() + "; User Id: " + userId + " does not exist in DB");
            return "not-found-exception";

        }

    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> registerUser(HttpSession session, @ModelAttribute User user) {
        try {
            user.setDateRegistrated(new Date());
            user.setLastDateActivited(new Date());

            logger.info("SessionId: " + session.getId() + "; Saving new user");
            userService.save(user);
            logger.info("SessionId: " + session.getId() + "; User registered. UserId: " + user.getId());

            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

        } catch (BadRequestException e) {
            logger.warn("User with email: " + user.getEmail() + " or phone " + user.getPhone() + " already exist in DB. Please, try again.");
            System.out.println("bad");
            return new ResponseEntity<>("User can not registered in DB", HttpStatus.BAD_REQUEST);

        } catch (InternalServerError e) {
            logger.error("SessionId: " + session.getId() + "; Unexpected error during user login");
            logger.error(e);
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> loginUser(HttpServletRequest request, @RequestParam("emailLog") String email,
                                            @RequestParam("passwordLog") String password) {
        try {

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

        } catch (InternalServerError e) {
            logger.error("SessionId: " + request.getSession().getId() + "; Unexpected error during user login");
            logger.error(e);
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {

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
    String saveUser(HttpSession session, @RequestBody User user) {

        try {
            logger.info("SessionId: " + session.getId() + "; Saving new user");
            User user1 = userService.save(user);
            logger.info("SessionId: " + session.getId() + "; User has been saved. UserId: " + user.getId());

            return user1 + "was saving successful";

        } catch (BadRequestException e) {
            logger.warn("SessionId: " + session.getId() + "; User with email: " + user.getEmail() + " already exist in DB");
            return "Save unsuccessful " + e.getMessage();

        } catch (InternalServerError e) {
            logger.error("SessionId: " + session.getId() + "; Unexpected error during user login");
            logger.error(e);
            return "Something went wrong" + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/User/update", produces = "application/json")
    public @ResponseBody
    String updateUser(HttpSession session, @RequestBody User user) {

        try {
            logger.info("SessionId: " + session.getId() + "; Update user Id: " + user.getId());
            userService.update(user);
            logger.info("SessionId: " + session.getId() + "; User has updated. UserId: " + user.getId());
            return user.toString() + "was updating successful";

        } catch (NotFoundException e) {
            logger.warn("SessionId: " + session.getId() + "; User update unsuccessfully. UserId: " + user.getId() + " does not exist in DB");
            return "Update unsuccessful " + e.getMessage();

        } catch (InternalServerError e) {
            logger.error("SessionId: " + session.getId() + "; Unexpected error during user login");
            logger.error(e);
            return "Something went wrong" + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/User/delete", produces = "application/json")
    public @ResponseBody
    String deleteUser(HttpServletRequest req) {


        String id = req.getParameter("idUser");
        Long idUser = Long.parseLong(id);

        try {
            logger.info("SessionId: " + req.getSession().getId() + "; Delete user Id: " + id);
            userService.delete(idUser);
            logger.info("SessionId: " + req.getSession().getId() + "; User has successfully deleted. UserId: " + id);
            return "OK" + "User id " + idUser + " was deleted successfully";

        } catch (NotFoundException e) {
            logger.warn("SessionId: " + req.getSession().getId() + "; User deleted unsuccessfully. UserId: " + id + " does not exist in DB");
            // e.printStackTrace();
            return "Deleting unsuccessfully " + e.getMessage();
        }
    }


}
