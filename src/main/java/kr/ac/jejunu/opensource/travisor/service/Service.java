package kr.ac.jejunu.opensource.travisor.service;

import kr.ac.jejunu.opensource.travisor.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    Repository repository;

    @Transactional
    public HashMap<String, Object> getInfo(Map<String, Object> params) {

        HashMap<String, Object> resultJson = new HashMap<>();
        HashMap<String,Object> userRequest =  (HashMap<String,Object>) params.get("userRequest");
        String utter = userRequest.get("utterance").toString().replace("\n","");

        HashMap<String,Object> action =  (HashMap<String,Object>) params.get("action");
        HashMap<String,Object> days = (HashMap<String,Object>) action.get("params");
        HashMap<String,Object> firstDayMap=(HashMap<String,Object>)days.get("startDate");
        HashMap<String,Object> afterDayMap=(HashMap<String,Object>)days.get("lateDate");

        String firstDay=firstDayMap.get("date").toString();
        String afterDay=afterDayMap.get("date").toString();
        String location=days.get("location").toString();


        ArrayList <HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        list = (ArrayList<HashMap<String, Object>>) params.get("contexts");
        HashMap<String,Object>  contexts=list.get(0);
        String name= contexts.get("name").toString();
//        HashMap<String,Object>  contextpram= (HashMap<String,Object>) param.get("contextpram");
//        String  value=  contextpram.get("value").toString();



        System.out.println("첫날은:"+firstDay+"둘쨋날은:"+afterDay+"여행 장소는: "+location);

        System.out.println("context="+name);
        String rtnStr = "";
        switch (utter){
            case "뭐야" : rtnStr = "코딩32 챗봇입니다.";
                break;
            case "ㅋㅋ" : rtnStr = "저도 기분이 좋네요";
                break;
            default: rtnStr = "안녕하세요 코딩 32 챗봇입니다.";
        }
        /* 발화 처리 끝*/

        List<HashMap<String,Object>> outputs = new ArrayList<>();
        HashMap<String,Object> template = new HashMap<>();
        HashMap<String, Object> simpleText = new HashMap<>();
        HashMap<String, Object> text = new HashMap<>();

        text.put("text",rtnStr);
        simpleText.put("simpleText",text);
        outputs.add(simpleText);

        template.put("outputs",outputs);

        resultJson.put("version","2.0");
        resultJson.put("template",template);
        return resultJson;
    }
}
