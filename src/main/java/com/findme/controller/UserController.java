package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.model.User;
import org.springframework.stereotype.Controller;
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
