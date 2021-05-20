package kr.ac.jejunu.opensource.travisor.controller;

import kr.ac.jejunu.opensource.travisor.service.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Autowired
    private Service service;
}
