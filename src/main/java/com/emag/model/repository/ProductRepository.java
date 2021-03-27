package com.emag.model.repository;

import com.emag.model.pojo.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByFullNameContainingOrDescriptionContaining(String keyword1,String keyword2);
}
