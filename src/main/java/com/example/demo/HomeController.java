
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    // This method handles HTTP GET requests to "/"
    @GetMapping("/")
    public String home() {
        return "Welcome to the Home Page Allir í hbv502!";
    }
}
