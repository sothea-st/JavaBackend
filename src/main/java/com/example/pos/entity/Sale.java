package com.example.pos.entity;

import com.example.pos.constant.JavaMessage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "pos_sale")
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "emp_id")
    @Min(0)
    private int empId;

    @Column(name = "sale_date")
    @NotNull(message = JavaMessage.required)
    @NotBlank(message = JavaMessage.required)
    private String saleDate;

    @Column(name = "discount" , precision = 10, scale = 2)
    @NotNull(message = JavaMessage.required)
    private BigDecimal discount;

    @Column(name = "total" , precision = 10 , scale = 2)
    @NotNull(message = JavaMessage.required)
    private BigDecimal total;



    @OneToMany(cascade = CascadeType.ALL)
    private List<SaleDetail> dataSale;


    @Column(name = "create_by")
    @Min(0)
    private int createBy;

    @CreationTimestamp
    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "status")
    private boolean status=true;

    @Column(name = "is_delete")
    private boolean isDelete=false;

}