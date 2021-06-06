package kr.ac.jejunu.opensource.travisor.model;

import com.sun.istack.NotNull;
import lombok.Data;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;

@Entity
@Data
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    @NotNull
    private String culture;

    @Column
    @NotNull
    private String cultureName;

    @Column
    @NotNull
    private String startDate;

    @Column
    @NotNull
    private String endDate;

    @Column
    @NotNull
    private String location;

    @Column
    @NotNull
    private String explanation;



}
