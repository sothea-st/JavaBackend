package com.example.pos.service.sourceDataService;

import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.pos.constant.JavaConstant;
import com.example.pos.entity.ImportDetail;
import com.example.pos.entity.Product;
import com.example.pos.entity.models.ProductModel;
import com.example.pos.entity.payment.Payment;
import com.example.pos.entity.sourceData.ReturnDetails;
import com.example.pos.entity.sourceData.ReturnProduct;
import com.example.pos.repository.ImportDetailRepository;
import com.example.pos.repository.paymentRepository.PaymentRepository;
import com.example.pos.repository.productProjection.ProductProjection;
import com.example.pos.repository.sourceDataRepository.ReturnDetailsRepository;
import com.example.pos.repository.sourceDataRepository.ReturnProductRepository;
import com.example.pos.service.ProductService;

import java.util.*;
import jakarta.servlet.http.HttpSession;

@Service
public class ReturnProductService {
    @Autowired
    private ReturnProductRepository repo;

    @Autowired
    private HttpSession session;

    @Autowired
    private ReturnDetailsRepository repoDetail;

    @Autowired
    private PaymentRepository repoPayment;

    @Autowired
    private ImportDetailRepository repoImport;
    @Autowired
    private ProductService proService;

    public void returnProduct(ReturnProduct re) {
        // var createBy = session.getAttribute(JavaConstant.userId);
        // int id = (Integer) createBy;
        String time = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss a").format(Calendar.getInstance().getTime());

        ReturnProduct r = new ReturnProduct();
        r.setCreateBy(re.getCreateBy());
        r.setPaymentNo(re.getPaymentNo());
        r.setReturnTime(time);
        r.setReturnDate(JavaConstant.currentDate);
        r.setReasonId(re.getReasonId());
        r.setReturnAmount(re.getReturnAmount());
        repo.save(r);

        // update payment is_return by payment no
        Optional<Payment> data = repoPayment.getDataPayment(re.getPaymentNo());
        Payment pay = data.get();
        pay.setIsReturn("returned");
        repoPayment.save(pay);

        List<ReturnDetails> listDetail = re.getDataDetails();

        for (int i = 0; i < listDetail.size(); i++) {
            int proId = listDetail.get(i).getProId();
            int qtyReturn = listDetail.get(i).getQty();
            ReturnDetails obj = new ReturnDetails();
            obj.setProId(listDetail.get(i).getProId());
            obj.setQty(listDetail.get(i).getQty());
            obj.setReturnId(r.getId());
            obj.setPrice(listDetail.get(i).getPrice());
            obj.setAmount(listDetail.get(i).getAmount());
            repoDetail.save(obj);

            // restock qty back
            Optional<ImportDetail> dataImp = repoImport.findByImpId(proId);
            ImportDetail impDetail = dataImp.get();
            int restockQty = qtyReturn + impDetail.getQtyOld();
            impDetail.setQtyOld(restockQty);
            repoImport.save(impDetail);
        }
    }

    public List<ProductModel> searchProdcutByBarcode(String barcode, String invoiceNo) {
        ProductProjection data = repo.getProductByBarcode(invoiceNo, barcode);
        List<ProductModel> list = new ArrayList<>();
        if( data == null ) return list;
        int qty = repo.getQty(data.getId());
        ProductModel p = proService.proModel(data, qty);
        list.add(p);
        return list;
    }
}
