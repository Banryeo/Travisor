package kr.ac.jejunu.opensource.travisor.context;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.ac.jejunu.opensource.travisor.model.Model;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Configuration
public class Context {

    private final String reponseBlockId="60c358e4a30146398b661ff9";


    public HashMap<String, Object> carouselResponse(ArrayList<Model> selectList) {
        HashMap<String, Object> resultJson = new HashMap<String, Object>();
        List<HashMap<String, Object>> outputs = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> template = new HashMap<String, Object>();
        HashMap<String, Object> carousel = new HashMap<String, Object>();
        HashMap<String, Object> type = new HashMap<String, Object>();
        List<HashMap<String, Object>> items = new ArrayList<HashMap<String, Object>>();


        for (int i = 0; i < selectList.size(); i++) {
            HashMap<String, Object> item = addItem(selectList.get(i).getId(), selectList.get(i).getCultureName(),
                    selectList.get(i).getExplanation(), selectList.get(i).getImageUrl(),
                    selectList.get(i).getExplanation(), "https://www.naver.com/",
                    selectList.get(i).getStartDate().toString(), selectList.get(i).getEndDate().toString());
            items.add(item);
        }

        type.put("type", "basicCard");
        type.put("items", items);

        carousel.put("carousel", type);

        outputs.add(carousel);

        template.put("outputs", outputs);

        resultJson.put("version", "2.0");
        resultJson.put("template", template);
        return resultJson;
    }


    public Long getaUnixTime(String startDate) throws ParseException {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date;
        date = format.parse(startDate + " 00:00:00");
        return date.getTime() / 1000;
    }


    public ArrayList<Model> getLocation(String location, List<Model> listItem, ArrayList<Model> selectList) throws JsonProcessingException {
        switch (location) {
            case "??????":
                for (int i = 0; i < listItem.size(); i++) {
                    String locations=listItem.get(i).getLocation();
                    String region_depth_name=getNorthAndSouth(getKakaoApiGeocoding(locations)).get("region_depth_name").toString();
                    if(!region_depth_name.equals("non")){
                        if(region_depth_name.equals("?????????")){
                            selectList.add(listItem.get(i));
                        }

                    }
                }
                break;
            case "??????":
                for (int i = 0; i < listItem.size(); i++) {
                    String region_depth_name=getNorthAndSouth(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("region_depth_name").toString();
                    if(!region_depth_name.equals("non")){
                        if(region_depth_name.equals("????????????")){
                            selectList.add(listItem.get(i));
                        }
                    }
                }
                break;
            case "??????":
                for (int i = 0; i < listItem.size(); i++) {
                    String lon=getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString();
                    if(!lon.equals("non")){
                        if (Double.parseDouble(lon) > 126.524841479094) {
                            selectList.add(listItem.get(i));
                        }
                    }
                }
                break;
            case "??????":
                for (int i = 0; i < listItem.size(); i++) {
                    String lon=getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString();
                    if(!lon.equals("non")){
                        if (Double.parseDouble(lon) < 126.524841479094) {
                            selectList.add(listItem.get(i));
                        }
                    }
                }
                break;
        }
        return selectList;
    }


    public String getCulture(Map<String, Object> params) {
        ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
        list = (ArrayList<HashMap<String, Object>>) params.get("contexts");
        String culture = new String();
        if (list != null) {
            for (HashMap<String, Object> lists : list) {
                if (lists.get("lifespan").toString().equals("5")) {
                    culture = lists.get("name").toString();
                    break;
                }
            }
        }

        switch (culture) {
            case "festival":
                culture = "??????";
                break;
            case "performance":
                culture = "??????";
                break;
            case "exhibition":
                culture = "??????";
                break;
        }
        return culture;
    }


    public HashMap<String, Object> addItem(Integer itemId, String itemTitle, String itemDescription,
                                           String itemImageUrl, String itemMessage, String itemWebUrl,
                                           String startDate, String endDate) {

        HashMap<String, Object> group1 = new HashMap<String, Object>();
        HashMap<String, Object> imageUrl = new HashMap<String, Object>();
        List<HashMap<String, Object>> buttons = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> group2 = new HashMap<String, Object>();
        HashMap<String, Object> group3 = new HashMap<String, Object>();

        group2.put("action", "webLink");
        group2.put("label", "?????? ????????? ??????");
        group2.put("webLinkUrl", "http://3.35.24.12/index.html?id=" + itemId);

        group3.put("action", "webLink");
        group3.put("label", "?????? ????????????");
        group3.put("webLinkUrl", "https://www.google.com/search?ie=UTF-8&&query="+itemTitle);

        buttons.add(group2);
        buttons.add(group3);

        group1.put("title", itemTitle);
        group1.put("description", "[" + startDate + "]" + "~" + "[" + endDate + "]");
        imageUrl.put("imageUrl", itemImageUrl);
        group1.put("thumbnail", imageUrl);
        group1.put("buttons", buttons);
        return group1;
    }


    public HashMap<String, Object> simpleMessage(String rtnStr) {
        HashMap<String, Object> resultJson = new HashMap<String, Object>();
        List<HashMap<String, Object>> outputs = new ArrayList<HashMap<String, Object>>();
        HashMap<String, Object> template = new HashMap<String, Object>();
        HashMap<String, Object> simpleText = new HashMap<String, Object>();
        HashMap<String, Object> text = new HashMap<String, Object>();

        text.put("text", rtnStr);
        simpleText.put("simpleText", text);
        outputs.add(simpleText);

        template.put("outputs", outputs);

        resultJson.put("version", "2.0");
        resultJson.put("template", template);
        return resultJson;
    }


    public String getKakaoApiGeocoding(String query) {
        String apiKey = "da584fb56166b922cf227ce5be613b37";
        String apiUrl = "https://dapi.kakao.com/v2/local/search/address.json";
        String jsonString = null;
        BufferedReader rd = null;

        try {
            query = URLEncoder.encode(query, "UTF-8");

            String addr = apiUrl + "?query=" + query;

            URL url = new URL(addr);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Authorization", "KakaoAK " + apiKey);

            rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuffer docJson = new StringBuffer();

            String line;

            while ((line = rd.readLine()) != null) {
                docJson.append(line);
            }

            jsonString = docJson.toString();


        }finally {
            try{
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
    }

    public HashMap<String, Object>getNorthAndSouth(String geocodingString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> lonandlat = new HashMap<String, Object>();

        HashMap<String, Object> address=new HashMap<>();

        HashMap<String, Object> mappingData = mapper.readValue(geocodingString, HashMap.class);
        List<HashMap<String, Object>> documents = (ArrayList<HashMap<String, Object>>) mappingData.get("documents");
        int select=0;
        for(select=0;select<documents.size();select++){
            address= (HashMap<String, Object>) documents.get(select).get("address");
            boolean adressName=address.get("address_name").toString().contains("?????????????????????");

            if(adressName){
                break;
            }
        }
        if (documents.size()!=0){
            lonandlat.put("region_depth_name",address.get("region_2depth_name"));
        }
        else{
            lonandlat.put("region_depth_name","non");
        }
        return lonandlat;
    }

    public HashMap<String, Object> getLonAndLat(String geocodingString) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        HashMap<String, Object> mappingData = new HashMap<String, Object>();
        HashMap<String, Object> lonandlat = new HashMap<String, Object>();
        List<HashMap<String, Object>> documents = null;


        mappingData = mapper.readValue(geocodingString, HashMap.class);
        documents = (ArrayList<HashMap<String, Object>>) mappingData.get("documents");
        HashMap<String, Object> address=new HashMap<>();
        int select=0;
        for(select=0;select<documents.size();select++){
            address= (HashMap<String, Object>) documents.get(select).get("address");
            boolean adressName=address.get("address_name").toString().contains("?????????????????????");

            if(adressName){
                break;
            }
        }
        if (documents.size()!=0){
            lonandlat.put("lon", documents.get(select).get("x"));
            lonandlat.put("lat", documents.get(select).get("y"));
        }
        else{
            lonandlat.put("lon","non");
        }
        return lonandlat;
    }
}