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


@org.springframework.stereotype.Controller
public class Controller {

    @Autowired
    Service service;

    @ResponseBody
    @PostMapping("/test")
    public HashMap<String,Object> test(@RequestBody Map<String, Object> params, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, ParseException, java.text.ParseException {
        System.out.println(params);
        HashMap<String, Object> resultJson = service.getInfo(params);
        return resultJson;
    }

    @PostMapping("/test/v1")
    public JSONObject testV1(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject.toString());
        System.out.println("------------------------------------------------------------------------------");
        return jsonObject;
    }
}
