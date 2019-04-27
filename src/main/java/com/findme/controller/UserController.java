package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.BadRequestException;
import com.findme.NotFoundException;
import com.findme.dao.UserDAOImpl;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Controller
@EnableWebMvc
public class UserController {
    private UserService userService;
    //private UserDAOImpl userDAO;

   /* public UserController(UserService userService, UserDAOImpl userDAO) {
        this.userService = userService;
        this.userDAO = userDAO;
    }*/

    public UserController(UserService userService) {
       this.userService = userService;
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
            return "badRequestExcp";

        } catch (NotFoundException e) {
            return "notFoundException";

        } catch (Exception e){
            return "systemError";
        }

    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
@ResponseBody
    public String  registerUser(@ModelAttribute User user)throws BadRequestException{
       try{
           user.setDateRegistrated(new Date());
           user.setLastDateActivited(new Date());

          return userService.save(user).toString();

       } catch (BadRequestException e){
           System.out.println("bad");
           throw new BadRequestException("already exist");

       }catch (Exception e){
           return "systemError";
       }

    }

    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public String profile1(Model model, @PathVariable String userId) {
        User user = new User();
        user.setFirstName("Julka");
        user.setCity("Kyiv");
        model.addAttribute("user", user);
        return "profile";
    }

    /*@RequestMapping(method = RequestMethod.POST, value = "/findUser", produces = "text/plain")
    public @ResponseBody
    String findUser(HttpServletRequest req) {
        String idUser = req.getParameter("idUser");
        Long id = Long.parseLong(idUser);

        User user= userService.findOne(id);

        if (user == null)
            return "User id " + id + " doesn't exist in DB";
        return user.toString();
    }*/

    @RequestMapping(method = RequestMethod.POST, value = "/User/save", produces = "application/json")
    public @ResponseBody
    String saveUser(HttpServletRequest req) {
        User user = convertJSONStringToUser(req);

        try {
            return userService.save(user).toString() + "was saving successful";
        } catch (BadRequestException e ){
            return "Save unsuccessful " + e.getMessage();
        }
    }

    @RequestMapping(method = RequestMethod.PUT, value = "/User/update", produces = "application/json")
    public @ResponseBody
    String updateUser(HttpServletRequest req) {
        User user = convertJSONStringToUser(req);

        try {
            userService.update(user);
            return user.toString() + "was updating successful";

        } catch (BadRequestException e) {
            return "Update unsuccessful " + e.getMessage();
        } catch (NotFoundException e) {
            return "Update unsuccessful " + e.getMessage();
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

        } catch (BadRequestException e) {
            e.printStackTrace();
            return "Deleting unsuccessful " + e.getMessage();
        } catch (NotFoundException e) {
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
