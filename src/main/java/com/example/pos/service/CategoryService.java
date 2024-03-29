package com.example.pos.service;

import com.example.pos.constant.JavaValidation;
import com.example.pos.entity.Category;
import com.example.pos.repository.CategoryRepository;
import com.example.pos.util.exception.customeException.JavaNotFoundByIdGiven;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository repo;
    @Autowired
    private HttpSession httpSession;

    public Category saveCategory(Category c) {
        boolean catNameKh = repo.existsByCatNameKh(c.getCatNameKh());
        boolean catNameEn = repo.existsByCatNameEn(c.getCatNameEn());

        JavaValidation.checkDataAlreadyExists(catNameKh); // check catName already exists or not
        JavaValidation.checkDataAlreadyExists(catNameEn); // check catName already exists or not

        Object id = httpSession.getAttribute("idUser");

        int count = repo.countLengthRow();
        count++;

        Category obj = new Category();
        obj.setCatNameKh(c.getCatNameKh());
        obj.setCatNameEn(c.getCatNameEn());
        obj.setCreateBy((Integer)id);
        obj.setParentId(c.getParentId());
        obj.setMovePosition(count);
        repo.save(obj);
        return obj;
    }

    public ArrayList<Category> getCategory(int parentId) {
        return repo.getCategory(parentId);
    }

    public Category updateCategory(int id, Category c) {
        Optional<Category> data = repo.findById(id);
        Category obj = data.get();

        if (!Objects.equals(obj.getCatNameKh(), c.getCatNameKh())) {
            boolean isExist = repo.existsByCatNameKh(c.getCatNameKh());
            JavaValidation.checkDataAlreadyExists(isExist);
        }

        if (!Objects.equals(obj.getCatNameEn(), c.getCatNameEn())) {
            boolean isExist = repo.existsByCatNameEn(c.getCatNameEn());
            JavaValidation.checkDataAlreadyExists(isExist);
        }

        obj.setCatNameKh(c.getCatNameKh());
        obj.setCatNameEn(c.getCatNameEn());
        obj.setParentId(c.getParentId());
        obj.setMovePosition(c.getMovePosition());
        repo.save(obj);
        return obj;
    }

    public Category getCategoryById(int id) {
        Category c = repo.getCategoryById(id);
        if (c == null)
            throw new JavaNotFoundByIdGiven();
        return c;
    }

    public void deleteCategory(int id,Category c){
        Optional<Category> data = repo.findById(id);
        Category obj = data.get();
        obj.setDeleted(c.isDeleted());
        obj.setStatus(c.isStatus());
        repo.save(obj);
    }


}
