package com.emag.model.pojo;

import com.emag.model.dto.addressDTO.AddressDTO;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @Table(name="addresses")
    public class Address {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        private String country;
        private String province;
        private String city;
        private String neighborhood;
        private String street;
        private String number;
        @ManyToMany(mappedBy = "addresses")
        @JsonBackReference
        private List<User> users;

        public Address(AddressDTO address) {
            this.country = address.getCountry();
            this.province = address.getProvince();
            this.city = address.getCity();
            this.neighborhood = address.getNeighborhood();
            this.street = address.getStreet();
            this.number = address.getStreetNumber();
        }
    }


