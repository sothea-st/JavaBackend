package com.example.pos.repository.shiftRepository;
import java.util.Optional;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.pos.entity.OpenShift;
import java.util.*;
@Repository
public interface OpenShiftRepository extends JpaRepository<OpenShift,Integer> {

     @Query(nativeQuery = true , value = "select * from pos_open_shift where status = true and is_deleted = false and open_date = ?")
     Optional<List<OpenShift>> getPosIdByCurrentDate(String currentDate);

     @Query(nativeQuery = true , value = "select count(*) from pos_open_shift where status = true and is_deleted = false and open_date = ?")
     int countPosId(String currentDate);


     @Query(nativeQuery = true , value = "select * from pos_open_shift pos where status = true and is_deleted = false and user_id = ? and open_date = ?")
     OpenShift getDataOpenShift(int userId , String date);

     @Query(nativeQuery = true , value = "select * from pos_open_shift pos where status = true and is_deleted = false and user_id = ? and open_date = ?")
     Optional<OpenShift> getNumberOpenShift(int userId , String date);

     @Query(nativeQuery = true , value = "select * from pos_open_shift pos where user_id = ? and open_date  = ? order by id desc limit 1")
     OpenShift countOpenShift(int userId, String date);
     
}  