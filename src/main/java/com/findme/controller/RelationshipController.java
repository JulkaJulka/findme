package com.findme.controller;

import com.findme.BadRequestException;
import com.findme.model.RelationShipFriends;
import com.findme.model.User;
import com.findme.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@EnableWebMvc
public class RelationshipController {
    private RelationshipService relationshipService;

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {

        this.relationshipService = relationshipService;
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

    @RequestMapping(path = "/deleteFriend", method = RequestMethod.POST)
    public ResponseEntity<String> deleteFriend(HttpServletRequest request, @RequestParam("userIdFromDelete") String userIdFrom, @RequestParam("userIdToDelete") String userIdTo) {
        try {
            Long userIdToL = Long.parseLong(userIdTo);
            Long userIdFromL = Long.parseLong(userIdFrom);

            HttpSession session = request.getSession();

            User userFrom = (User) session.getAttribute("user");

            if (userFrom == null)
                return new ResponseEntity<>("You have to login", HttpStatus.UNAUTHORIZED);

            relationshipService.updateRelationshipStatus(userIdFromL, userIdToL, RelationShipFriends.DELETE);
            return new ResponseEntity<>("Request sent successfully", HttpStatus.OK);

        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Wrong friend's id. Try again.", HttpStatus.BAD_REQUEST);
        } catch (BadRequestException e) {
            return new ResponseEntity<>("You can not add myself.", HttpStatus.BAD_REQUEST);
        } catch (HttpServerErrorException.InternalServerError e) {
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
