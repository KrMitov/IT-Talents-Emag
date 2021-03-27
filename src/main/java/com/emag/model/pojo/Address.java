package com.emag.model.pojo;

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
        @ManyToMany(mappedBy = "addresses")
        private List<User> users;
    }


