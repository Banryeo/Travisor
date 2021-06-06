package kr.ac.jejunu.opensource.travisor.service;

import kr.ac.jejunu.opensource.travisor.model.Model;
import kr.ac.jejunu.opensource.travisor.repository.Repository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    Repository repository;

    @Transactional
    public HashMap<String, Object> getInfo(Map<String, Object> params) throws ParseException, java.text.ParseException {

        HashMap<String, Object> resultJson = new HashMap<>();
        HashMap<String,Object> userRequest =  (HashMap<String,Object>) params.get("userRequest");
        String utter = userRequest.get("utterance").toString().replace("\n","");

        HashMap<String,Object> action =  (HashMap<String,Object>) params.get("action");
        HashMap<String,Object> days = (HashMap<String,Object>) action.get("params");
        JSONParser parser=new JSONParser();
        JSONObject firstDayJson= (JSONObject) parser.parse(days.get("startDate").toString());
        JSONObject afterDayJson=(JSONObject) parser.parse(days.get("endDate").toString());;

        String startDate=firstDayJson.get("value").toString();
        String endDate=afterDayJson.get("value").toString();
        String location=days.get("location").toString();


        ArrayList <HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        list = (ArrayList<HashMap<String, Object>>) params.get("contexts");
        HashMap<String,Object>  contexts=list.get(0);
        String name= contexts.get("name").toString();
//        HashMap<String,Object>  contextpram= (HashMap<String,Object>) param.get("contextpram");
//        String  value=  contextpram.get("value").toString();



        System.out.println("첫날은:"+startDate+"둘쨋날은:"+endDate+"여행 장소는: "+location);

        System.out.println("context="+name);

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=format.parse(startDate+" 00:00:00");
        Long startDateTime=date.getTime()/1000;


        date=format.parse(endDate+" 00:00:00");
        Long endDateTime=date.getTime()/1000;

        System.out.println(startDateTime+"and"+endDateTime);
        System.out.println(repository.search(startDateTime,endDateTime).size());
        List<Model> listItem = repository.search(startDateTime,endDateTime);


//        String rtnStr = "";
//        switch (utter){
//            case "뭐야" : rtnStr = "코딩32 챗봇입니다.";
//                break;
//            case "ㅋㅋ" : rtnStr = "저도 기분이 좋네요";
//                break;
//            default: rtnStr = "안녕하세요 코딩 32 챗봇입니다.";
//        }
        /* 발화 처리 끝*/

        List<HashMap<String,Object>> outputs = new ArrayList<>();
        HashMap<String,Object> template = new HashMap<>();
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();

        text.put("text",listItem.toString());
        simpleText.put("simpleText",text);
        outputs.add(simpleText);

        template.put("outputs",outputs);

        resultJson.put("version","2.0");
        resultJson.put("template",template);
        return resultJson;
    }
}
