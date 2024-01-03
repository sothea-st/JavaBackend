package com.example.pos.repository;

import com.example.pos.entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailsRepository extends JpaRepository<SaleDetail,Integer> {

}
