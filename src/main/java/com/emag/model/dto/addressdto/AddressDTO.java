package com.emag.model.dto.addressdto;

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
}
