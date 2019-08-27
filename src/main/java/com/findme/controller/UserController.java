package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.BadRequestException;
import com.findme.NotFoundException;
import com.findme.dao.RelationShipFrndsDAOImpl;
import com.findme.model.RelationShipFriends;
import com.findme.model.RelationShipFrnds;
import com.findme.model.User;
import com.findme.service.RelationshipService;
import com.findme.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Controller
@EnableWebMvc
public class UserController {
    private UserService userService;
    private RelationshipService relationshipService;
    private RelationShipFrndsDAOImpl relationShipFrndsDAO;
    // private UserDAOImpl userDAO;


    /*public UserController(UserService userService, RelationShipFrndsSErvice relationShipFrndsSErvice) {
        this.userService = userService;
        this.relationShipFrndsSErvice = relationShipFrndsSErvice;
    }*/

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

        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addRelationship(HttpServletRequest request, @RequestParam("id_friend") String userIdTo) {
        try {
            Long userIdToL = Long.parseLong(userIdTo);

            HttpSession session = request.getSession();

            User userFrom = (User) session.getAttribute("user");

            if (userFrom == null)
                return new ResponseEntity<>("You have to login", HttpStatus.UNAUTHORIZED);

            relationshipService.addRelationship(userFrom.getId(), userIdToL);
            return new ResponseEntity<>("Request sent successfully", HttpStatus.OK);


        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Wrong friend's id. Try again.", HttpStatus.BAD_REQUEST);

        } catch (BadRequestException e) {

            return new ResponseEntity<>("You can not add myself.", HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path = "/updateRelationship", method = RequestMethod.POST)
    public ResponseEntity<String> updateRelationship(HttpServletRequest request, @RequestParam("userIdFrom") String userIdFrom, @RequestParam("userIdTo") String userIdTo, @RequestParam("status") String status) {
        try {
            Long userIdToL = Long.parseLong(userIdTo);
            Long userIdFromL = Long.parseLong(userIdFrom);

            HttpSession session = request.getSession();

            User userFrom = (User) session.getAttribute("user");

            if (userFrom == null)
                return new ResponseEntity<>("You have to login", HttpStatus.UNAUTHORIZED);

                relationshipService.updateRelationshipStatus(userIdFromL, userIdToL, RelationShipFriends.valueOf(status));

            return new ResponseEntity<>("Update status sent successfully", HttpStatus.OK);

        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Wrong friend's id. Try again.", HttpStatus.BAD_REQUEST);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("You can not add myself.", HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/user/reqflist", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getIncomeRequests(HttpSession session) throws BadRequestException {


        ModelAndView model = new ModelAndView("badRequestExcp");

        if (session.getAttribute("id") == null) {

            return model;
        }

        Long id = (Long) session.getAttribute("id");
        List<Long> list = relationshipService.getIncomeRequests(id.toString());

        model.setViewName("reqflist");
        model.addObject("lists", list);

        return model;
    }

    //  public RelationShipFrnds updateRelationShip(String userIdFrom, String userIdTo, RelationShipFriends status) {
    //   return relationshipService.updateRelationship(Long.parseLong(userIdFrom), Long.parseLong(userIdTo), status);
    // }

    @RequestMapping(value = "/user/outflist", method = RequestMethod.GET)
    @ResponseBody
    public ModelAndView getOutcomeRequests(HttpSession session) {

        ModelAndView model = new ModelAndView("badRequestExcp");

        if (session.getAttribute("id") == null) {

            return model;
        }

        Long id = (Long) session.getAttribute("id");
        List<Long> list = relationshipService.getOutcomeRequests(id.toString());

        model.setViewName("outflist");
        model.addObject("lists", list);

        return model;
    }


    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndex() {
        return "index";
    }

    @RequestMapping(path = "/logout", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<String> logoutUser(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession();

            session.removeAttribute("email");
            session.removeAttribute("id");


            return new ResponseEntity<>("User logout successfully", HttpStatus.OK);

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
