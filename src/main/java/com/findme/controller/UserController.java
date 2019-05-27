package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.BadRequestException;
import com.findme.InternalServerException;
import com.findme.NotFoundException;
import com.findme.dao.UserDAOImpl;
import com.findme.model.LoginStatus;
import com.findme.model.User;
import com.findme.service.UserService;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;

@Controller
@EnableWebMvc
public class UserController {
    private UserService userService;
    private UserDAOImpl userDAO;

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

        } catch (Exception e) {
            return "systemError";
        }

    }

    @RequestMapping(path = "/user-registration", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> registerUser(HttpSession session, @ModelAttribute User user) {
        try {
            session.setAttribute("product1", "iphone");
            user.setDateRegistrated(new Date());
            user.setLastDateActivited(new Date());
            userService.save(user);
            return new ResponseEntity<>("User registered successfully", HttpStatus.OK);

        } catch (BadRequestException e) {
            System.out.println("bad");
            return new ResponseEntity<>("User can not registered in DB", HttpStatus.BAD_REQUEST);

        } catch (HttpServerErrorException.InternalServerError ex) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> loginUser(HttpServletRequest request, HttpServletResponse response)  {
        try {
            String email = request.getParameter("emailLog");
            String password = request.getParameter("passwordLog");

            HttpSession session = request.getSession();
            session.setAttribute("email", email);

                User userFound = userService.checkExistanceUserInDB(email, password);

                if (userFound == null) {
                    return new ResponseEntity<>("Wrong password or email. Try again please.", HttpStatus.BAD_REQUEST);

                } else if(userFound.getLoginStatus() == LoginStatus.LOGOUT){

                userFound.setLoginStatus(LoginStatus.LOGIN);
                userService.update(userFound);
                    return new ResponseEntity<>("User successfully log in to FindMe", HttpStatus.OK);}

               return new ResponseEntity<>("User successfully log in to FindMe", HttpStatus.OK);

        } catch (BadRequestException e) {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logoutUser(HttpServletRequest request, HttpServletResponse response, @ModelAttribute User user)  {
        try {
            HttpSession session = request.getSession();

            user.setLoginStatus(LoginStatus.LOGOUT);
            userService.update(user);

            session.removeAttribute("email");
            session.invalidate();

            return new ResponseEntity<>("User logout successfully", HttpStatus.OK);

        } catch (BadRequestException e) {
            return new ResponseEntity<>("Badrequest", HttpStatus.BAD_REQUEST);

        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
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
        } catch (BadRequestException e) {
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
        } /*catch (NotFoundException e) {
            return "Update unsuccessful " + e.getMessage();
        }*/
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
