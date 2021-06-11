package kr.ac.jejunu.opensource.travisor.controller.api;

import kr.ac.jejunu.opensource.travisor.service.InstagramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class InstagramAPiController {

    @Autowired
    InstagramService instagramService;

    @GetMapping("/api/instagram")
    public String getInstagram() throws IOException {
        return instagramService.getInstagram("Jeju");
    }
}
