package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
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


    @Autowired
    public UserController(UserService userService, RelationshipService relationshipService, RelationShipFrndsDAOImpl relationShipFrndsDAO) {
        this.userService = userService;
        this.relationshipService = relationshipService;
        this.relationShipFrndsDAO = relationShipFrndsDAO;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String userId) {


        try {
            Long idLong = Long.valueOf(userId);
            model.addAttribute("userId", idLong);

            User user = userService.findOne(idLong);
            model.addAttribute("user", user);
            return "profile";

        } catch (NumberFormatException e) {
            return "bad-request-exception";

        } catch (NotFoundException e) {
            return "not-found-exception";

        } catch (Exception e) {
            return "system-error";
        }

    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> registerUser(HttpSession session, @ModelAttribute User user) {
        try {
            user.setDateRegistrated(new Date());
            user.setLastDateActivited(new Date());
            userService.save(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

        } catch (BadRequestException e) {
            System.out.println("bad");
            return new ResponseEntity<>("User can not registered in DB", HttpStatus.BAD_REQUEST);

        } catch (InternalServerError e) {
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
                return new ResponseEntity<>("Wrong password or email. Try again please.", HttpStatus.BAD_REQUEST);

            } else {

                session.setAttribute("user", userFound);

                return new ResponseEntity<>("User successfully log in to FindMe", HttpStatus.OK);
            }

        } catch (InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {

            HttpSession session = request.getSession();

            session.removeAttribute("email");
            session.removeAttribute("id");


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
    String saveUser(HttpServletRequest req) {
        User user = convertJSONStringToUser(req);

        try {
            return userService.save(user).toString() + "was saving successful";
        } catch (BadRequestException e) {
            return "Save unsuccessful " + e.getMessage();
        }
        catch (InternalServerError e){
            return "Something went wrong" + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/User/update", produces = "application/json")
    public @ResponseBody
    String updateUser(HttpServletRequest req) {
        User user = convertJSONStringToUser(req);

        try {
            userService.update(user);
            return user.toString() + "was updating successful";

        } catch (BadRequestException | NotFoundException e) {
            return "Update unsuccessful " + e.getMessage();
        } catch (InternalServerError e){
            return "Something went wrong" + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/User/delete", produces = "application/json")
    public @ResponseBody
    String deleteUser(HttpServletRequest req) {

        String id = req.getParameter("idUser");
        Long idUser = Long.parseLong(id);

        try {
            userService.delete(idUser);
            return "OK" + "User id " + idUser + " was deleted successfully";

        } catch (BadRequestException|NotFoundException e) {
            e.printStackTrace();
            return "Deleting unsuccessful " + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/testUser")
    public @ResponseBody
    String testUser(HttpServletRequest req) {
        return convertJSONStringToUser(req).toString();
    }

    private User convertJSONStringToUser(HttpServletRequest req) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            User user = mapper.readValue(is, User.class);

            return user;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
