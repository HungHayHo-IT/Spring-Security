package com.example.spring_security.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {
//@PostAuthoriza : lay du lieu tren csdl roi moi kiem tra quyen
    @GetMapping("/myLoans")
//    @PreAuthorize("hasRole('USER')") // chi nguoi co vai tro user moiw truy cap duoc
    @PreAuthorize("hasAuthority('VIEWLOANS')")
    public  String getLoansDetails () {
        return "Here are the loans details from the DB";
    }

}
