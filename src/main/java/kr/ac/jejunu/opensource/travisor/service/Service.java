package kr.ac.jejunu.opensource.travisor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.ac.jejunu.opensource.travisor.context.Context;
import kr.ac.jejunu.opensource.travisor.model.Model;
import kr.ac.jejunu.opensource.travisor.repository.Repository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    private Context context;

    @Autowired
    public Repository repository;

    @Transactional
    public HashMap<String, Object> getInfo(Map<String, Object> params) throws ParseException, java.text.ParseException, JsonProcessingException {

        HashMap<String,Object> userRequest=new HashMap<>();
        HashMap<String,Object> days=new HashMap<>();

        HashMap<String,Object> action=new HashMap<>();
        JSONParser parser=new JSONParser();
        String utter=null;

        JSONObject firstDayJson=null;
        JSONObject afterDayJson=null;

        String startDate=null;
        String endDate=null;
        String location=null;
        String culture=null;

        userRequest =  (HashMap<String,Object>) params.get("userRequest");
        utter = userRequest.get("utterance").toString().replace("\n","");

        action =  (HashMap<String,Object>) params.get("action");
        days= (HashMap<String,Object>) action.get("params");

        firstDayJson= (JSONObject) parser.parse(days.get("startDate").toString());
        afterDayJson=(JSONObject) parser.parse(days.get("endDate").toString());;

        startDate=firstDayJson.get("value").toString();
        endDate=afterDayJson.get("value").toString();
        location=days.get("location").toString();


        culture = context.getCulture(params);

//        HashMap<String,Object>  contextpram= (HashMap<String,Object>) param.get("contextpram");
//        String  value=  contextpram.get("value").toString();

        Long startDateTime = context.getaUnixTime(startDate);

        Long endDateTime = context.getaUnixTime(endDate);

        List<Model> listItem = repository.search(startDateTime,endDateTime,culture);

        ArrayList<Model> selectList =new ArrayList<>();

        if(listItem.size()!=0){
            context.getLocation(location, listItem, selectList);
        }

        if(selectList.size()==0){
            throw new IllegalArgumentException(culture+" ????????? ????????????.");
        }

        HashMap<String, Object> resultJson = context.carouselResponse(selectList);

//
//        ?????? ????????? api?????? ??????????????? ????????? ???????????? ???????????? ?????? ??????
//        getLonAndLat(getKakaoApiGeocoding());
        return resultJson;
    }

    @Transactional
    public HashMap<String, Object> getHelp() {
        String helpMessasge = "????????? ???????????????? ????????? ??????, ??????, ?????? ????????? ???????????? ????????????. Travisor ?????? ????????? ????????? ?????? ?????? ????????? \"??????\"??? ????????? ?????? ?????? ????????? ?????????!";
        return context.simpleMessage(helpMessasge);
    }
}


