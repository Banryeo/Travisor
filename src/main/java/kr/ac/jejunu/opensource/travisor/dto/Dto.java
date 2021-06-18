package kr.ac.jejunu.opensource.travisor.dto;


import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Dto {
    private String culture;
    private String startDate;
    private String endDate;
    private String location;
}
