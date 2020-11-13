package com.findme.controller;

import com.findme.exception.BadRequestException;
import com.findme.model.RelationShipFriends;
import com.findme.model.User;
import com.findme.service.RelationshipService;
import com.findme.exception.InternalServerError;
import org.apache.log4j.Logger;
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

    private final static Logger logger = Logger.getLogger(RelationshipController.class);

    @Autowired
    public RelationshipController(RelationshipService relationshipService) {

        this.relationshipService = relationshipService;
    }

    @RequestMapping(path = "/addRelationship", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> addRelationship(HttpSession session, @RequestParam("id_friend") String userIdTo) {
        try {
            Long userIdToL = Long.parseLong(userIdTo);

            User userFrom = (User) session.getAttribute("user");

            if (userFrom == null)
                return new ResponseEntity<>("You have to login", HttpStatus.UNAUTHORIZED);
            logger.info("SessionId: " + session.getId() + "; Adding RelationShip");
            relationshipService.addRelationship(userFrom.getId(), userIdToL);
            logger.info("SessionId: " + session.getId() + "; Relationship has added successfully between UserId: " + userFrom.getId() + " and UserId " + userIdToL);
            return new ResponseEntity<>("Request sent successfully", HttpStatus.OK);


        } catch (NumberFormatException | BadRequestException e) {
            logger.warn(e);
            return new ResponseEntity<>("Wrong friend's id. Try again.", HttpStatus.BAD_REQUEST);

        } catch (InternalServerError e) {
            logger.error("SessionId: " + session.getId() + "; Unexpected error during user login");
            logger.error(e);
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @RequestMapping(path = "/updateRelationship", method = RequestMethod.POST)
    public ResponseEntity<String> updateRelationship(HttpSession session, @RequestParam("userIdFrom") String userIdFrom, @RequestParam("userIdTo") String userIdTo, @RequestParam("status") String status) {
        try {
            Long userIdToL = Long.parseLong(userIdTo);
            Long userIdFromL = Long.parseLong(userIdFrom);

            User userFrom = (User) session.getAttribute("user");

            if (userFrom == null){
                logger.warn("User Id " + userIdFrom + " has to login");
                return new ResponseEntity<>("You have to login", HttpStatus.UNAUTHORIZED);}

            logger.info("SessionId: " + session.getId() + "; Updating RelationShip ");
            relationshipService.updateRelationshipStatus(userIdFromL, userIdToL, RelationShipFriends.valueOf(status));
            logger.info("SessionId: " + session.getId() + "; Successfully updating  RelationShip from User Id " + userIdFrom + " to User Id " + userIdTo);
            return new ResponseEntity<>("Update status sent successfully", HttpStatus.OK);

        } catch (NumberFormatException | BadRequestException e) {
            logger.info(e);
            return new ResponseEntity<>("Wrong friend's id. Try again.", HttpStatus.BAD_REQUEST);

        }  catch (InternalServerError e) {
            logger.error("SessionId: " + session.getId() + "; Unexpected error during user login");
            logger.error(e);
            return new ResponseEntity<>("Something went wrong...", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
