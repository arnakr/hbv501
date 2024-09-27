
package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // This method handles HTTP GET requests to "/"
    @GetMapping("/")
    public String HomeController() {

        return "home";
    }
}
