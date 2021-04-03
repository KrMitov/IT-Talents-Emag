package com.emag.model.repository;

import com.emag.model.pojo.OrderedProduct;
import com.emag.model.pojo.OrderedProductsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedProductsRepository extends JpaRepository<OrderedProduct, OrderedProductsKey> {
}
