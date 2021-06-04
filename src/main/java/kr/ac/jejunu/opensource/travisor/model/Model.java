package kr.ac.jejunu.opensource.travisor.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Model {

    @Id
    private Long id;
}
