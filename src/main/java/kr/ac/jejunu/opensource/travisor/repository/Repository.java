package kr.ac.jejunu.opensource.travisor.repository;


import kr.ac.jejunu.opensource.travisor.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface Repository extends JpaRepository<Model, Integer> {
}
