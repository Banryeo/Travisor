package kr.ac.jejunu.opensource.travisor.service;

import kr.ac.jejunu.opensource.travisor.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;

@org.springframework.stereotype.Service
public class Service {

    @Autowired
    Repository repository;
}
