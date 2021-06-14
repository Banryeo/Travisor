package kr.ac.jejunu.opensource.travisor.controller;

//import kr.ac.jejunu.opensource.travisor.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.jejunu.opensource.travisor.context.Context;
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

    @Autowired
    public Context context;


//    @ResponseBody
//    @ExceptionHandler(Exception.class)
//    public HashMap<String,Object> nullData(final Exception ex){
//        System.out.println(ex.getMessage());
//        return context.simpleMessage("실행중 오류가 발생하였습니다 다시 해주세요");
//    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public HashMap<String,Object> nullData(final IllegalArgumentException ex){
        return context.simpleMessage(ex.getMessage());
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

    @ResponseBody
    @GetMapping("/test/tests")
    public HashMap<String,Object> tests() {
        HashMap<String, Object> resultJson = context.simpleMessage("test!!");
        return resultJson;
    }

    @PostMapping("/test/v1")
    public JSONObject testV1(@RequestBody JSONObject jsonObject){
        System.out.println(jsonObject.toString());
        System.out.println("-------------------------------------------------------------------------------------------");
        return jsonObject;
    }
}
