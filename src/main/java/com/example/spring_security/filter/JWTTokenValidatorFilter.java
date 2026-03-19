package com.example.spring_security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//🧠 Mục đích chính
//
//👉 Filter này dùng để:
//
//Lấy token từ header Authorization
//
//Kiểm tra token có hợp lệ không
//
//Nếu hợp lệ → set user vào SecurityContext
//
//Nếu không → từ chối request (401)
public class JWTTokenValidatorFilter extends OncePerRequestFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }

    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getServletPath().equals("/user");
    }
}
