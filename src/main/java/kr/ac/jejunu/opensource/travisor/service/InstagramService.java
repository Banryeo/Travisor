package kr.ac.jejunu.opensource.travisor.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLConnection;
import java.net.URLEncoder;

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

            System.out.println(sbuf);
            isr.close();
            return sbuf.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return "에러";
    }




}
