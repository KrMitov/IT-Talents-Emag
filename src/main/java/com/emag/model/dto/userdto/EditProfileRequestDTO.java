package com.emag.model.dto.userdto;

import com.emag.model.dto.addressdto.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class EditProfileRequestDTO {
    @NotNull(message = "Password can not be null")
    private String oldPassword;
    @NotNull(message = "Password can not be null")
    private String newPassword;
    @NotNull(message = "Password can not be null")
    private String confirmNewPassword;
    @NotNull(message = "Phone number can not be null")
    @Size(min = 10,max = 10, message="Enter a correct phone number ")
    private String phoneNumber;
    @NotNull(message = "Birthday can not be null")
    private String birthDate;
    @NotNull(message = "Address can not be null")
    private AddressDTO address;

}
