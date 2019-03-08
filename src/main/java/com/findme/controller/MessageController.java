package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.model.Message;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

@Controller
@EnableWebMvc
public class MessageController {

    @RequestMapping(method = RequestMethod.POST, value = "/testMessage")
    public @ResponseBody
    String testMessage(HttpServletRequest req) {
        return convertJSONStringToUser(req).toString();
    }

    private Message convertJSONStringToUser(HttpServletRequest req) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            Message message = mapper.readValue(is, Message.class);

            return message;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
