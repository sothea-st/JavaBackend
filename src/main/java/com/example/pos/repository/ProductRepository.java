package com.example.pos.repository;

import com.example.pos.entity.Product;
import com.example.pos.repository.productProjection.ProductProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

        boolean existsByProNameKh(String name);

        boolean existsByProNameEn(String name);

        @Query(nativeQuery = true, value = "select pc.id,pc.barcode,pc.cat_id ,pc.brand_id ,pc.flag ,pc.weight ,pc.pro_image_name ,\r\n"
                        + //
                        "pc.brand_id ,pc.pro_name_en ,pc.pro_name_kh ,pc.cost,pc.price ,\r\n" + //
                        "pc.product_status ,pc.discount ,pc.code_out_stock ,pc.code_expired \r\n" + //
                        "from pos_product pc\r\n" + //
                        "where pc.status=true and pc.is_deleted=false order by pc.id desc")
        List<ProductProjection> getProduct();

        @Query(nativeQuery = true, value = "select count(*) from pos_product where status=true and is_deleted=false")
        int countRow();

        @Query(nativeQuery = true, value = "select * from pos_product where status=true and is_deleted=false and id=?")
        Product getProductById(int id);

        @Query(nativeQuery = true, value = "select pc.id,pc.barcode,pc.cat_id ,pc.brand_id ,pc.flag ,pc.weight ,pc.pro_image_name , \r\n"
                        + //
                        "pc.brand_id ,pc.pro_name_en ,pc.pro_name_kh ,pc.cost,pc.price , \r\n" + //
                        "pc.product_status ,pc.discount ,pc.code_out_stock ,pc.code_expired  \r\n" + //
                        "from pos_product pc where pc.status=true and pc.is_deleted=false\r\n" + //
                        "and pc.cat_id = ? order by id desc limit ?")
        List<ProductProjection> getProductByCatId(int catId, int limit);

        @Query(nativeQuery = true, value = " select count(*) from pos_product pp where status = true and is_deleted = false and cat_id = ?")
        int countProduct(int catId);

        @Query(nativeQuery = true, value = " select count(*) from pos_product pp where status = true and is_deleted = false and brand_id = ?")
        int countProductByBrandId(int brandId);

        @Query(nativeQuery = true, value = "select pc.id,pc.barcode,pc.cat_id ,pc.brand_id ,pc.flag ,pc.weight ,pc.pro_image_name , \r\n"+//
                        "pc.brand_id ,pc.pro_name_en ,pc.pro_name_kh ,pc.cost,pc.price , \r\n" + //
                        "pc.product_status ,pc.discount ,pc.code_out_stock ,pc.code_expired  \r\n" + //
                        "from pos_product pc where pc.status=true and pc.is_deleted=false\r\n" + //
                        "and pc.brand_id = ? order by id desc limit ?")
        List<ProductProjection> getProductByBrandId(int brandId, int limit);

        @Query(nativeQuery = true, value = "select pc.id,pc.barcode,pc.cat_id ,pc.brand_id ,pc.flag ,pc.weight ,pc.pro_image_name , \r\n"
                        + //
                        " pc.brand_id ,pc.pro_name_en ,pc.pro_name_kh ,pc.cost,pc.price ,  \r\n" + //
                        " pc.product_status ,pc.discount ,pc.code_out_stock ,pc.code_expired    \r\n" + //
                        " from pos_product pc where pc.status=true and pc.is_deleted=false  \r\n" + //
                        " and pc.pro_name_en  ilike %?% limit 10")
        List<ProductProjection> searchProductByName(String proNameEn);

        @Query(nativeQuery = true, value = "select pc.id,pc.barcode,pc.cat_id ,pc.brand_id ,pc.flag ,pc.weight ,pc.pro_image_name ,  \r\n"
                        + //
                        " pc.brand_id ,pc.pro_name_en ,pc.pro_name_kh ,pc.cost,pc.price ,   \r\n" + //
                        " pc.product_status ,pc.discount ,pc.code_out_stock ,pc.code_expired    \r\n" + //
                        " from pos_product pc where pc.status=true and pc.is_deleted=false  \r\n" + //
                        " and pc.barcode like  %?%")
        List<ProductProjection> searchProductByBarcode(String barcode);

}
