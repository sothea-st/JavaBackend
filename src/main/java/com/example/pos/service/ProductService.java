package com.example.pos.service;

import com.example.pos.components.JavaStorage;
import com.example.pos.constant.JavaConstant;
import com.example.pos.constant.JavaValidation;
import com.example.pos.entity.FileStore;
import com.example.pos.entity.Product;
import com.example.pos.repository.FileStoreRepository;
import com.example.pos.repository.ProductRepository;
import com.example.pos.util.exception.customeException.JavaNotFoundByIdGiven;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repo;
    @Autowired
    private FileStoreRepository fileStore;
    @Autowired
    private HttpSession session;

    public Product addProduct(Product p, MultipartFile file , MultipartFile flagFile) throws IOException {
      
        boolean proNameKh = repo.existsByProNameKh(p.getProNameKh());
        JavaValidation.checkDataAlreadyExists(proNameKh);

        boolean proNameEn = repo.existsByProNameEn(p.getProNameEn());
        JavaValidation.checkDataAlreadyExists(proNameEn);

        Object idUser = session.getAttribute(JavaConstant.userId);

        Product pro = new Product();
        pro.setCatId(p.getCatId());
        pro.setUnitTypeId(p.getUnitTypeId());
        pro.setProNameKh(p.getProNameKh());
        pro.setProNameEn(p.getProNameEn());
        pro.setCostUsd(p.getCostUsd());
        pro.setPriceUsd(p.getPriceUsd());
        pro.setCostKhr(p.getCostKhr());
        pro.setPriceKhr(p.getPriceKhr());
        pro.setNote(p.getNote());
        pro.setCreateBy((Integer) idUser);
        pro.setWeight(p.getWeight());
        pro.setBarcode(p.getBarcode());
        pro.setDiscount(p.getDiscount());
        pro.setDiscountPercentag(p.getDiscountPercentag().isEmpty() ? "0" : p.getDiscountPercentag());
        pro.setProductStatus(p.getProductStatus());
        if (file == null || file.isEmpty()) {
            pro.setFileName(JavaConstant.defaultNameImage);
        } else {
            // JavaStorage.storeImage(file); for save image to path assests/product in project
            String fileName = JavaStorage.setFileName(file.getOriginalFilename());
 
            // save information image to table pos_file
            FileStore f = new FileStore(fileName, fileName, file.getContentType(), file.getBytes());
            fileStore.save(f);
            pro.setFileName(fileName);
        }

        if( flagFile == null || flagFile.isEmpty() ) {
            pro.setFlag(JavaConstant.defaultNameImage);
        } else {
            String flagName = JavaStorage.setFileName(flagFile.getOriginalFilename());
            pro.setFlag(flagName);
            FileStore f = new FileStore(flagName, flagName, flagFile.getContentType(), flagFile.getBytes());
            fileStore.save(f);
        }

        repo.save(pro);
        return pro;
    }

    public Product readData(int id) {
        Product data = repo.getProductById(id);
        if (data == null)
            throw new JavaNotFoundByIdGiven();
        return data;
    }

    public byte[] getImage(String imageName) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get("assets\\product\\" + imageName)); // convert imageName to byte[]
        String base64 = Base64.getEncoder().encodeToString(bytes); // encode byte[] to string base64
        return Base64.getDecoder().decode(base64); // decode string base64
    }

    public Map<String, Object> getProduct() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("count", repo.countRow());
        map.put("result", repo.getProduct());
        return map;
    }

    public Product editProduct(int id, Product editProduct, MultipartFile file , MultipartFile flag) throws IOException {
        Product previousPro = repo.findById(id).get();
        String fileName = previousPro.getFileName();
        String flagName = previousPro.getFlag();

        if (!Objects.equals(previousPro.getProNameKh(), editProduct.getProNameKh())) {
            boolean isExist = repo.existsByProNameKh(editProduct.getProNameKh());
            JavaValidation.checkDataAlreadyExists(isExist);
        }

        if (!Objects.equals(previousPro.getProNameEn(), editProduct.getProNameEn())) {
            boolean isExist = repo.existsByProNameEn(editProduct.getProNameEn());
            JavaValidation.checkDataAlreadyExists(isExist);
        }

        Object idUser = session.getAttribute(JavaConstant.userId);

        if (Objects.equals(fileName, JavaConstant.defaultNameImage))
            fileName = "";
        if (file != null && !file.isEmpty() ) {
            // save information image to table pos_file
            String fName = JavaStorage.setFileName(file.getOriginalFilename());
            FileStore f = new FileStore(fName, fName,file.getContentType(), file.getBytes());
            fileStore.save(f);
            previousPro.setFileName(fName);
        }

        if( Objects.equals(flagName,JavaConstant.defaultNameImage) ) flagName ="";

        if( flag != null && !flag.isEmpty() ) {
            String fName = JavaStorage.setFileName(flag.getOriginalFilename());
            FileStore f = new FileStore(fName, fName,flag.getContentType(), flag.getBytes());
            fileStore.save(f);
            previousPro.setFlag(fName);
        }


        previousPro.setProNameKh(editProduct.getProNameKh());
        previousPro.setProNameEn(editProduct.getProNameEn());
        previousPro.setCostKhr(editProduct.getCostKhr());
        previousPro.setCostUsd(editProduct.getCostUsd());
        previousPro.setPriceKhr(editProduct.getPriceKhr());
        previousPro.setPriceUsd(editProduct.getPriceUsd());
        previousPro.setWeight(editProduct.getWeight());
        previousPro.setBarcode(editProduct.getBarcode());
        previousPro.setDiscount(editProduct.getDiscount());
        previousPro.setProductStatus(editProduct.getProductStatus());
        previousPro.setUnitTypeId(editProduct.getUnitTypeId());
        previousPro.setCatId(editProduct.getCatId());
        previousPro.setNote(editProduct.getNote());
        previousPro.setDiscountPercentag(editProduct.getDiscountPercentag());
        // previousPro.setCreateBy((Integer) idUser);
        repo.save(previousPro);
        return previousPro;
    }

    public void deleteProduct(int id, Product p) {
        Optional<Product> data = repo.findById(id);
        Product obj = data.get();
        obj.setStatus(p.isStatus());
        obj.setDeleted(p.isDeleted());
        repo.save(obj);
    }

    public byte[] getFile(String id) throws IOException {
        Optional<FileStore> fileDB = fileStore.findById(id);
        return fileDB.get().getData();
    }

}
