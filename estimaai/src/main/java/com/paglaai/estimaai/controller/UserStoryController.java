package com.paglaai.estimaai.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-stories")
public class UserStoryController {

    @PostMapping("/process")
    public ResponseEntity<Void> processStory(@RequestParam(required = false) String userStories, @RequestParam String exportType){
        return ResponseEntity.ok().build();
    }
}
