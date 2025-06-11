package com.dossantosh.springfirstproject.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user/usersRest")
public class Users2Controller {
    @GetMapping
    public String showUsersRest() {
        return "user/usersRest";
    }
    
}

    