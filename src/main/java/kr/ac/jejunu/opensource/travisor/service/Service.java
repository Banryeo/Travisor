package kr.ac.jejunu.opensource.travisor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.jejunu.opensource.travisor.model.Model;
import kr.ac.jejunu.opensource.travisor.repository.Repository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    public Repository repository;

    @Transactional
    public HashMap<String, Object> getInfo(Map<String, Object> params) throws ParseException, java.text.ParseException, JsonProcessingException {

        HashMap<String, Object> resultJson = new HashMap<>();
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


        culture = getCulture(params);

//        HashMap<String,Object>  contextpram= (HashMap<String,Object>) param.get("contextpram");
//        String  value=  contextpram.get("value").toString();

        Long startDateTime = getaUnixTime(startDate);

        Long endDateTime = getaUnixTime(endDate);

        List<Model> listItem = repository.search(startDateTime,endDateTime,culture);
        System.out.println(listItem);

        if(listItem.size()==0){
            throw new IllegalArgumentException(culture+" 정보가 없습니다.");
        }

        ArrayList<Model> selectList = getLocation(location, listItem);

        List<HashMap<String,Object>> outputs = new ArrayList<>();
        HashMap<String,Object> template = new HashMap<>();
        HashMap<String,Object> carousel = new HashMap<>();
        HashMap<String,Object> type = new HashMap<>();
        List<HashMap<String,Object>> items = new ArrayList<>();


        for(int i=0; i<selectList.size(); i++){
            HashMap<String,Object> item = addItem(selectList.get(i).getCultureName(),
                    selectList.get(i).getExplanation(), selectList.get(i).getImageUrl(),
                    selectList.get(i).getExplanation(), "https://www.naver.com/",
                    selectList.get(i).getStartDate().toString(), selectList.get(i).getEndDate().toString());
            items.add(item);
        }

        type.put("type", "basicCard");
        type.put("items", items);

        carousel.put("carousel", type);

        outputs.add(carousel);

        template.put("outputs",outputs);

        resultJson.put("version","2.0");
        resultJson.put("template",template);

//
//        아래 로직은 api에서 위도경도를 주소로 검색해서 받아와야 할때 사용
//        getLonAndLat(getKakaoApiGeocoding());
        return resultJson;
    }

    private Long getaUnixTime(String startDate) throws java.text.ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        date = format.parse(startDate + " 00:00:00");
        return date.getTime() / 1000;
    }

    private ArrayList<Model> getLocation(String location, List<Model> listItem) throws JsonProcessingException {
        ArrayList<Model> selectList=new ArrayList<>();
        switch (location){
            case "북쪽":
                Collections.sort(listItem,new LocationComparator("제주시"));
               for(int i = 0; i< listItem.size(); i++){
                   if(!listItem.get(i).getLocation().contains("제주시")){
                       break;
                   }
                   selectList.add(listItem.get(i));
               }
                break;
            case "남쪽":
                Collections.sort(listItem,new LocationComparator("서귀포시"));
                for(int i = 0; i< listItem.size(); i++){
                    if(!listItem.get(i).getLocation().contains("서귀포시")){
                        break;
                    }
                    selectList.add(listItem.get(i));
                }
                break;
            case "동쪽":
                for(int i = 0; i< listItem.size(); i++){
                    if(Double.parseDouble(getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString())>126.524841479094){
                        selectList.add(listItem.get(i));
                    }
                }
                break;
            case "서쪽":
                for(int i = 0; i< listItem.size(); i++) {
                    if (Double.parseDouble(getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString()) < 126.524841479094) {
                        selectList.add(listItem.get(i));
                    }
                }
                break;
        }
        return selectList;
    }

    private String getCulture(Map<String, Object> params) {
        ArrayList <HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
        list = (ArrayList<HashMap<String, Object>>) params.get("contexts");
        String culture= new String();
        if(list!=null){
            for(HashMap<String, Object> lists:list){
                if(lists.get("lifespan").toString().equals("5")){
                    culture=lists.get("name").toString();
                    break;
                }
            }
        }

        switch (culture){
            case "festival":
                culture="행사";
                break;
            case "performance":
                culture="공연";
                break;
            case "exhibition":
                culture="전시";
                break;
        }
        return culture;
    }

    @Transactional
    public HashMap<String, Object> getHelp() {
        String helpMessasge = "도움이 필요하군요? 저희는 축제, 전시, 공연 정보를 제공하고 있답니다. Travisor 봇이 제대로 동작을 하고 있지 않다면 \"종료\"를 입력한 뒤에 다시 시도해 보세요!";
        return simpleMessage(helpMessasge);
    }

    public HashMap<String, Object> addItem(String itemTitle, String itemDescription,
                                          String itemImageUrl, String itemMessage, String itemWebUrl,
                                           String startDate, String endDate){

        HashMap<String, Object> group1 = new HashMap<>();
        HashMap<String, Object> imageUrl = new HashMap<>();
        List<HashMap<String, Object>> buttons = new ArrayList<>();
        HashMap<String, Object> group2 = new HashMap<>();
        HashMap<String, Object> group3= new HashMap<>();

        group2.put("action", "message");
        group2.put("label", "설명 자세히 보기");
        group2.put("messageText", ""+itemMessage);

        group3.put("action", "webLink");
        group3.put("label", "구경하기");
        //group3.put("webLinkUrl", itemWebUrl);

        buttons.add(group2);
        buttons.add(group3);

        group1.put("title", itemTitle);
        group1.put("description", "["+startDate+"]"+"~"+"["+endDate+"]");
        imageUrl.put("imageUrl", itemImageUrl);
        group1.put("thumbnail",imageUrl);
        group1.put("buttons", buttons);
        return group1;
    }

    public HashMap<String, Object> simpleMessage(String rtnStr){
        HashMap<String, Object> resultJson = new HashMap<>();
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


    public String getKakaoApiGeocoding(String query) {
        String apiKey = "da584fb56166b922cf227ce5be613b37";
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        String jsonString = null;

        try {
            query = URLEncoder.encode(query, "UTF-8");

            String addr = apiUrl + "?query=" + query;

            URL url = new URL(addr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            BufferedReader rd = null;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer docJson = new StringBuffer();

            String line;

            while ((line=rd.readLine()) != null) {
                docJson.append(line);
            }

            jsonString = docJson.toString();
            rd.close();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public HashMap<String, Object> getLonAndLat(String geocodingString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> mappingData=new HashMap<>();
        HashMap<String, Object> lonandlat = new HashMap<>();
        ArrayList <HashMap<String,Object>> documents = new ArrayList<>();


        mappingData = mapper.readValue(geocodingString, HashMap.class);
        documents= (ArrayList<HashMap<String,Object>>) mappingData.get("documents");
        lonandlat.put("lon",documents.get(0).get("x"));
        lonandlat.put("lat",documents.get(0).get("y"));
        return lonandlat;
    }

}

class LocationComparator implements Comparator<Model>{

    private String word;

    public LocationComparator(String word){
        this.word=word;
    }


    @Override
    public int compare(Model o1, Model o2) {
        if(o1.getLocation().contains(word)) return -1;
        return 0;
    }
}
