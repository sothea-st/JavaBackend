package com.example.pos.repository.sourceDataRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.example.pos.entity.sourceData.ReturnProduct;
import com.example.pos.repository.productProjection.ProductProjection;

@Repository
public interface ReturnProductRepository  extends JpaRepository<ReturnProduct,Integer>{
     @Query(nativeQuery = true , value = " select pp2.id,pp2.cat_id ,pp2.brand_id ,pp2.flag ,pp2.weight ,pp2.pro_image_name ,pp2.barcode ,pp2.pro_name_en \r\n" + //
                    " ,pp2.pro_name_kh ,pp2.cost,pp2.price,pp2.product_status ,pp2.discount from pos_payment pp\r\n" + //
                    " inner join pos_sale_details psd  on psd.sale_id  = pp.sale_id \r\n" + //
                    " inner join pos_product pp2 on pp2.id = psd.pro_id \r\n" + //
                    " where pp.payment_no = ? and pp2.barcode = ?")
     ProductProjection getProductByBarcode(String paymentNo,String barcode);


     @Query(nativeQuery = true , value = "select pid.qty_old  from pos_import_detail pid where pro_id = ? order by id desc limit  1")
     int getQty(int proId);



}
