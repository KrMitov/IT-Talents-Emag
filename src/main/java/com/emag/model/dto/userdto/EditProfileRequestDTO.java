package com.emag.model.dto.userdto;

import com.emag.model.dto.addressdto.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
public class EditProfileRequestDTO {
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
    private String phoneNumber;
    private Timestamp birthDate;
    private AddressDTO address;
}
