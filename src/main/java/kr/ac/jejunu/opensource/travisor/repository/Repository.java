//
package kr.ac.jejunu.opensource.travisor.repository;

import kr.ac.jejunu.opensource.travisor.model.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface Repository extends JpaRepository<Model, Integer> {

    @Query(value = "SELECT * FROM test WHERE  UNIX_TIMESTAMP(startDate)<=?1 and UNIX_TIMESTAMP(endDate)>=?2",nativeQuery = true)
    List<Model> search(long startDate, long endDate);
}
