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
            case "북쪽":
                Collections.sort(listItem, new LocationComparator("제주시"));
                for (int i = 0; i < listItem.size(); i++) {
                    if (!listItem.get(i).getLocation().contains("제주시")) {
                        break;
                    }
                    selectList.add(listItem.get(i));
                }
                break;
            case "남쪽":
                Collections.sort(listItem, new LocationComparator("서귀포시"));
                for (int i = 0; i < listItem.size(); i++) {
                    if (!listItem.get(i).getLocation().contains("서귀포시")) {
                        break;
                    }
                    selectList.add(listItem.get(i));
                }
                break;
            case "동쪽":
                for (int i = 0; i < listItem.size(); i++) {
                    if (Double.parseDouble(getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString()) > 126.524841479094) {
                        selectList.add(listItem.get(i));
                    }
                }
                break;
            case "서쪽":
                for (int i = 0; i < listItem.size(); i++) {
                    if (Double.parseDouble(getLonAndLat(getKakaoApiGeocoding(listItem.get(i).getLocation())).get("lon").toString()) < 126.524841479094) {
                        selectList.add(listItem.get(i));
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
                culture = "행사";
                break;
            case "performance":
                culture = "공연";
                break;
            case "exhibition":
                culture = "전시";
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
        group2.put("label", "설명 자세히 보기");
        group2.put("webLinkUrl", "http://3.35.24.12/index.html?id=" + itemId);

        group3.put("action", "webLink");
        group3.put("label", "구글 검색하기");
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

            while ((line = rd.readLine()) != null) {
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
        HashMap<String, Object> mappingData = new HashMap<String, Object>();
        HashMap<String, Object> lonandlat = new HashMap<String, Object>();
        ArrayList<HashMap<String, Object>> documents = new ArrayList<HashMap<String, Object>>();


        mappingData = mapper.readValue(geocodingString, HashMap.class);
        documents = (ArrayList<HashMap<String, Object>>) mappingData.get("documents");
        for(int i=0;i<documents.size();i++){
            String adressName=documents.get(i).get("address_name").toString();
            System.out.println(adressName);
        }
        lonandlat.put("lon", documents.get(0).get("x"));
        lonandlat.put("lat", documents.get(0).get("y"));
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