package kr.ac.jejunu.opensource.travisor.controller;

//import kr.ac.jejunu.opensource.travisor.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.jejunu.opensource.travisor.service.Service;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@ControllerAdvice
@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    public Service service;

    @ExceptionHandler(IllegalArgumentException.class)
    public HashMap<String,Object> nullData(final IllegalArgumentException ex){
        return service.simpleMessage(ex.getMessage());
    }
    
    @ResponseBody
    @PostMapping("/test")
    public HashMap<String,Object> test(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, ParseException, java.text.ParseException {
        System.out.println(params);
        HashMap<String, Object> resultJson = service.getInfo(params);
        return resultJson;
    }

    @ResponseBody
    @PostMapping("/test/help")
    public HashMap<String,Object> help() {
        HashMap<String, Object> resultJson = service.getHelp();
        return resultJson;
    }

    @PostMapping("/test/v1")
    public JSONObject testV1(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject.toString());
        System.out.println("------------------------------------------------------------------------------");
        return jsonObject;
    }
}
