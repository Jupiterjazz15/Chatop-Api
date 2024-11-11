package com.openclassroom.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {

    @GetMapping("/api/auth/me")
    public String getResource() {
        return "My homepage";
    }

}
