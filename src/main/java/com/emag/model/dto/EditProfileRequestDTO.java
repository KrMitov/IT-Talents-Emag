package com.emag.model.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

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
