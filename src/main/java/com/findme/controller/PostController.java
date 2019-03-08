package com.findme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.findme.model.Post;
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
public class PostController {

    @RequestMapping(method = RequestMethod.POST, value = "/testPost")
    public @ResponseBody
    String testPost(HttpServletRequest req) {
        return convertJSONStringToUser(req).toString();
    }

    private Post convertJSONStringToUser(HttpServletRequest req) {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = req.getInputStream()) {
            Post post = mapper.readValue(is, Post.class);

            return post;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
