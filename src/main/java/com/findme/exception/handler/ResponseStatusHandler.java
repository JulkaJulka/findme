package com.findme.exception.handler;

import com.findme.controller.UserController;
import com.findme.exception.BadRequestException;
import com.findme.exception.InternalServerError;
import com.findme.exception.NotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ResponseStatusHandler {

    @ExceptionHandler(value = NotFoundException.class)
    public ModelAndView testErrorHandler(HttpServletRequest request, Exception e) throws Exception {
       
       ModelAndView modelAndView = new ModelAndView("not-found-exception");

        return modelAndView;
    }

    @ExceptionHandler(value ={ BadRequestException.class, NumberFormatException.class})
    public ModelAndView testErrorHandler2(HttpServletRequest request, Exception e) throws Exception {

        ModelAndView modelAndView = new ModelAndView("bad-request-exception");

        return modelAndView;
    }

    @ExceptionHandler(value = InternalServerError.class)
    public ModelAndView testErrorHandler3(HttpServletRequest request, Exception e) throws Exception {

        ModelAndView modelAndView = new ModelAndView("system-error");

        return modelAndView;
    }

}
