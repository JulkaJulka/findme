package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.BadRequestException;
import com.findme.model.User;
import com.findme.service.UserService;
import com.findme.service.UserServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@EnableWebMvc
public class UserController {
    private UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @RequestMapping(path = "/user/{userId}", method = RequestMethod.GET)
    public String profile(Model model, @PathVariable String userId){

        Long idLong = Long.parseLong(userId);

        try {
            User user = userService.findOne(idLong);
            if(user == null)
                return "notFound";
            model.addAttribute("user",user);
            return "profile";

        } catch (Exception e) {
            return "systemError";
        }

    }
    @RequestMapping(path = "/user", method = RequestMethod.GET)
    public String profile1(Model model, @PathVariable String userId){
        User user = new User();
        user.setFirstName("Julka");
        user.setCity("Kyiv");
        model.addAttribute("user",user);
        return "profile";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/findUser", produces = "text/plain")
    public @ResponseBody
    String findUser(HttpServletRequest req) {
        String idUser = req.getParameter("idUser");
        Long id = Long.parseLong(idUser);

        User user= userService.findOne(id);

        if (user == null)
            return "User id " + id + " doesn't exist in DB";
        return user.toString();
    }

    @RequestMapping(method = RequestMethod.POST, value = "/User/save", produces = "application/json")
    public @ResponseBody
    String saveUser(HttpServletRequest req) {
        User user = convertJSONStringToUser(req);

        return userService.save(user).toString() + "was saving successful";
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
