package com.emag.model.dto.userdto;

import com.emag.model.dto.addressdto.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    private String phoneNumber;
    @NotNull
    private String birthDate;
    @Valid
    private AddressDTO address;

}
