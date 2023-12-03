package com.sushi.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/management/")
public class ManagementController {

    @GetMapping
    public String get() {
        return "GET:: manager controller";
    }
    @PostMapping
    public String post() {
        return "POST:: manager controller";
    }
    @PutMapping
    public String put() {
        return "PUT:: manager controller";
    }
    @DeleteMapping
    public String delete() {
        return "DELETE:: manager controller";
    }
}
