package com.fragile.terra_home.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/public")
public class PublicController {

    @GetMapping("/home")
    public ResponseEntity<String> homePage(){
        return  new ResponseEntity<>("Home page", HttpStatus.OK);
    }
}
