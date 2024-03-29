package com.example.pos.repository.sourceDataRepository;

 
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;
import com.example.pos.entity.sourceData.Reason;

@Repository
public interface ReasonRepository extends JpaRepository<Reason,Integer> {
     @Query(nativeQuery = true , value = "select * from pos_reason where status = true and is_deleted=false order by id desc")
     List<Reason> getReason();

     @Query(nativeQuery = true , value = "select * from pos_reason where status = true and is_deleted=false and id = ?")
     Reason getReasonById(int id);

     @Query(nativeQuery = true , value = "select * from pos_reason pr where code = ? order by id desc")
     List<Reason> getReasonByCode(String code);


}
