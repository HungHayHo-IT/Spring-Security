package com.example.spring_security.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
public class AccountController {

    @RequestMapping("/myAccount")
    public  String getAccountDetails (Model model , Authentication authentication) {
        if(authentication!=null){
            model.addAttribute("username",authentication.getName());
            model.addAttribute("roles",authentication.getAuthorities().toString());
        }
        return "account";
    }



}
