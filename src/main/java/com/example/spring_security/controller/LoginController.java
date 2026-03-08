package com.example.spring_security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login() {
        // Chỉ cần return tên file (không cần đuôi .html)
        // nếu bạn đã cài đặt Thymeleaf và để file trong thư mục templates/
        return "login";
    }
}
