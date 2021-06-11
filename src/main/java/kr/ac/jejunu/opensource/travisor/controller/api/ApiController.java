package kr.ac.jejunu.opensource.travisor.controller.api;

import kr.ac.jejunu.opensource.travisor.model.Model;
import kr.ac.jejunu.opensource.travisor.repository.Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApiController {

    public final Repository repository;

    @GetMapping("/test/api/{modelId}")
    public Model getModel(@PathVariable("modelId") Integer id){
        System.out.println("웹 접속"+id);
        return repository.findById(id).get();
    }
}
