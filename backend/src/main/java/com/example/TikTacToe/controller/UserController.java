package com.example.TikTacToe.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/api/loggedIn")
    public ResponseEntity<?> isLoggedIn () {
        return new ResponseEntity<>("COOKIE_IS_VALID", HttpStatus.OK);
    }
}