package com.example.spring_security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController{
    @GetMapping("/welcome")
    public String sayWelcome(){
        return "Welcome to spring application with security";
    }
    // Thêm đoạn này để xử lý lỗi invalid session
    @GetMapping("/invalidSession")
    public String invalidSession() {
        return "Phiên đăng nhập của bạn đã hết hạn hoặc không hợp lệ. Vui lòng đăng nhập lại!";
    }
}
