package kr.ac.jejunu.opensource.travisor.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

@Service
public class InstagramService {

    public String getInstagram(String tagName) throws IOException {
        String apiUrl = "https://www.instagram.com/explore/tags/";
        String suffix = "/?__a=1";

        //해시태그 검색어를 인코딩
        String spec = apiUrl + URLEncoder.encode(tagName, "UTF-8") + suffix;


        System.out.println(spec);

        StringBuffer sbuf = new StringBuffer();

        try {
            URL url = new URL(spec);
            URLConnection conn = url.openConnection();

            InputStream is = conn.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "UTF-8");
            BufferedReader br = new BufferedReader(isr);

            String str ;
            while((str=br.readLine()) != null) {

                sbuf.append(str + "\r\n");
            }

            JSONParser parser = new JSONParser();
            JSONObject object = (JSONObject)parser.parse(sbuf.toString());


            System.out.println(sbuf);

            isr.close();

            ArrayList<String> texts = new ArrayList<>();

            ObjectMapper mapper = new ObjectMapper();
            HashMap<String, Object> mappingData = mapper.readValue(sbuf.toString(), HashMap.class);
            HashMap<String, Object> graphql= (HashMap<String, Object>) mappingData.get("graphql");
            HashMap<String, Object> hashtag= (HashMap<String, Object>) graphql.get("hashtag");
            HashMap<String, Object> media= (HashMap<String, Object>) hashtag.get("edge_hashtag_to_media");
            ArrayList<HashMap<String,Object>> edges= (ArrayList<HashMap<String, Object>>) media.get("edges");

            for(HashMap<String, Object> edge : edges){
                HashMap<String, Object> node= (HashMap<String, Object>) edge.get("node");
                HashMap<String, Object> caption= (HashMap<String, Object>) node.get("edge_media_to_caption");
                ArrayList<HashMap<String,Object>>innerEdges= (ArrayList<HashMap<String, Object>>) caption.get("edges");
                HashMap<String, Object> innerNode= (HashMap<String, Object>) innerEdges.get(0).get("node");
                String text= (String) innerNode.get("text");
                texts.add(text);
            }
//            HashMap<String, Object> node= (HashMap<String, Object>) edges.get(0).get("node");
//            HashMap<String, Object> caption= (HashMap<String, Object>) node.get("edge_media_to_caption");
//            ArrayList<HashMap<String,Object>>innerEdges= (ArrayList<HashMap<String, Object>>) caption.get("edges");
//            HashMap<String, Object> innerNode= (HashMap<String, Object>) innerEdges.get(0).get("node");
//            String text= (String) innerNode.get("text");
//            System.out.println(text);
//            return sbuf.toString();
            return texts.toString();
        } catch (MalformedURLException | ParseException e) {
            e.printStackTrace();
        }

        return "에러";
    }




}
