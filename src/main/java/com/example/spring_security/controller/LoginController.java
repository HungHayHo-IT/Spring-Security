package com.example.spring_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error" , required = false) String error,
            @RequestParam(value = "logout" , required = false) String logout
            ,Model model) {
        // Chỉ cần return tên file (không cần đuôi .html)
        // nếu bạn đã cài đặt Thymeleaf và để file trong thư mục templates/
        String errorMessge = null;
        if(null != error){
            errorMessge = "username or password is correct";
        }

        if(null!=logout){
            errorMessge = "successfully logout";
        }
        model.addAttribute("errorMessge",errorMessge);
        return "login";
    }
}
