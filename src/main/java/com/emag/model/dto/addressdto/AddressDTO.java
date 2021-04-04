package com.emag.model.dto.addressdto;

import com.emag.model.pojo.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {
    private String country;
    private String province;
    private String city;
    private String neighborhood;
    private String street;
    private String streetNumber;

    public AddressDTO(Address address){
        this.country = address.getCountry();
        this.province = address.getProvince();
        this.city = address.getCity();
        this.neighborhood = address.getNeighborhood();
        this.street = address.getStreet();
        this.streetNumber = address.getNumber();
    }

}
