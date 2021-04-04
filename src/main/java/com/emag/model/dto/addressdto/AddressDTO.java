package com.emag.model.dto.addressdto;

import com.emag.model.pojo.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {
    @NotNull(message = "Country can not be null")
    private String country;
    @NotNull(message = "Province can not be null")
    private String province;
    @NotNull(message = "City can not be null")
    private String city;
    @NotNull(message = "Neighborhood can not be null")
    private String neighborhood;
    @NotNull(message = "Street can not be null")
    private String street;
    @NotNull(message = "Street nubmer can not be null")
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
