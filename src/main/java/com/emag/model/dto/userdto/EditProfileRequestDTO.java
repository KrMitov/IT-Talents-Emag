package com.emag.model.dto.userdto;

import com.emag.model.dto.addressdto.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EditProfileRequestDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String phoneNumber;
    private String birthDate;
    private AddressDTO address;
}