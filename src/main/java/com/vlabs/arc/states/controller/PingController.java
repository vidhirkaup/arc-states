package com.vlabs.arc.states.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class PingController {

    @GetMapping("/ping")
    public String ping(){
        String msg = String.format("pinged arc-states @ %s", LocalDateTime.now());
        log.info(msg);
        return msg;
    }
}
