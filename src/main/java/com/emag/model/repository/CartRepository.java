package com.emag.model.repository;

import com.emag.model.pojo.UserCart;
import com.emag.model.pojo.UserCartsKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<UserCart, UserCartsKey> {
}
