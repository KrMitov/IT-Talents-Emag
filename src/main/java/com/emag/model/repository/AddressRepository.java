package com.emag.model.repository;

import com.emag.model.pojo.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address,Integer> {
}
