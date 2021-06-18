package kr.ac.jejunu.opensource.travisor.service;

import org.junit.jupiter.api.BeforeAll;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class InstagramServiceTest extends TestCase {

    static InstagramService instagramService;

    @BeforeAll
    public void setup(){
        ApplicationContext applicationContext =
                new AnnotationConfigApplicationContext("kr.ac.jejunu.opensource");
        instagramService = applicationContext.getBean("instagramService", InstagramService.class);
    }

    @Test
    public void testGetInstagram() throws IOException {
        instagramService.getInstagram("제주");
    }
}