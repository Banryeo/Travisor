package kr.ac.jejunu.opensource.travisor.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.jejunu.opensource.travisor.model.Model;
import kr.ac.jejunu.opensource.travisor.repository.Repository;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
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
    Repository repository;

    @Transactional
    public HashMap<String, Object> getInfo(Map<String, Object> params) throws ParseException, java.text.ParseException, JsonProcessingException {

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


        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date=format.parse(startDate+" 00:00:00");
        Long startDateTime=date.getTime()/1000;


        date=format.parse(endDate+" 00:00:00");
        Long endDateTime=date.getTime()/1000;
        System.out.println("날짜 데이터"+startDateTime+"\n"+endDateTime);

//        List<Model> listItem = repository.search(startDateTime,endDateTime);
//
//        ArrayList<Model> selectList=new ArrayList<>();
//        switch (location){
//            case "북쪽":
//                Collections.sort(listItem,new LocationComparator("제주시"));
//               for(int i=0;i<listItem.size();i++){
//                   if(!listItem.get(i).getLocation().contains("제주시")){
//                       break;
//                   }
//                   selectList.add(listItem.get(i));
//               }
//                break;
//            case "남쪽":
//                Collections.sort(listItem,new LocationComparator("서귀포시"));
//                for(int i=0;i<listItem.size();i++){
//                    if(!listItem.get(i).getLocation().contains("서귀포시")){
//                        break;
//                    }
//                    selectList.add(listItem.get(i));
//                }
//                break;
//            case "동쪽":
//                for(int i=0;i<listItem.size();i++){
//                    if(Double.parseDouble(getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString())>126.524841479094){
//                        selectList.add(listItem.get(i));
//                    }
//                }
//                break;
//            case "서쪽":
//                for(int i=0;i<listItem.size();i++) {
//                    if (Double.parseDouble(getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString()) < 126.524841479094) {
//                        selectList.add(listItem.get(i));
//                    }
//                }
//                break;
//        }
//
//        List<HashMap<String,Object>> outputs = new ArrayList<>();
//        HashMap<String,Object> template = new HashMap<>();
//        HashMap<String,Object> carousel = new HashMap<>();
//        HashMap<String,Object> type = new HashMap<>();
//        List<HashMap<String,Object>> items = new ArrayList<>();
//
//
//        for(int i=0; i<selectList.size(); i++){
//            HashMap<String,Object> item = addItem(selectList.get(i).getCultureName(),
//                    selectList.get(i).getExplanation(), selectList.get(i).getImageUrl(),
//                    selectList.get(i).getCulture(), "https://www.naver.com/",
//                    selectList.get(i).getStartDate().toString(), selectList.get(i).getEndDate().toString());
//            items.add(item);
//        }
//
//        type.put("type", "basicCard");
//        type.put("items", items);
//
//        carousel.put("carousel", type);
//
//        outputs.add(carousel);
//
//        template.put("outputs",outputs);
//
//        resultJson.put("version","2.0");
//        resultJson.put("template",template);
//
//
//        //아래 로직은 api에서 위도경도를 주소로 검색해서 받아와야 할때 사용
//        //getLonAndLat(getKakaoApiGeocoding());
        return null;
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
        group2.put("label", "열어보기");
        group2.put("messageText", itemMessage);

        group3.put("action", "webLink");
        group3.put("label", "구경하기");
        group3.put("webLinkUrl", itemWebUrl);

        buttons.add(group2);
        buttons.add(group3);

        group1.put("title", itemTitle+"["+startDate+"]"+"["+endDate+"]");
        group1.put("description", itemDescription);
        imageUrl.put("imageUrl", itemImageUrl);
        group1.put("thumbnail",imageUrl);
        group1.put("buttons", buttons);
        return group1;
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
