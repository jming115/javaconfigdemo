package com.mfun.javaconfigdemo.controller;

import com.mfun.javaconfigdemo.model.User;
import com.mfun.javaconfigdemo.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * @Auther: JMing
 * @Date: 2019/11/26 17:05
 * @Description:
 */
@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return user;
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public User addUser(@RequestBody User user) {
        User newUser = null;
        try {
            newUser = userService.addUser(user);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return newUser;
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User userUpdated = null;
        try {
            userUpdated = userService.updateUser(id, user);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return userUpdated;
    }
}
