package com.emag.util;

import com.emag.exceptions.BadRequestException;
import com.emag.model.dto.addressdto.AddressDTO;
import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.emag.model.pojo.Address;
import com.emag.model.pojo.User;

import java.time.LocalDate;
import java.util.List;

public class UserUtility {


    public static boolean addressIsEmpty(AddressDTO addressDTO) {
        boolean result = false;
        if (addressDTO.getCountry().length() == 0 && addressDTO.getProvince().length() == 0 && addressDTO.getCity().length() == 0
                && addressDTO.getNeighborhood().length() == 0 && addressDTO.getStreet().length() == 0 && addressDTO.getStreetNumber().length() == 0) {
            result = true;
        }
        return result;
    }

    public static boolean emailIsValid(String email) {
        if (email == null) {
            throw new BadRequestException("You have to enter a valid email");
        }
        boolean result = false;
        boolean emailSymbolFound = false;
        String specialCharacters = "#?!@$%^&*-:'{}+_()<>|[]";
        //Letters, numbers and "_","." before "@" symbol
        String regex = "^[A-Za-z0-9+_.]+@(.+)$";
        if (email.matches(regex)) {
            int startCharacter = 0;
            for (int i = 0; i < email.length(); i++) {
                if (email.charAt(i) == '@') {
                    if (!emailSymbolFound) {
                        startCharacter = i;
                        emailSymbolFound = true;
                    }
                }
            }
            for (int i = startCharacter + 1; i < email.length(); i++) {
                char character = email.charAt(i);
                if (!specialCharacters.contains(String.valueOf(character)) && !Character.isDigit(character) && !Character.isSpaceChar(character)){
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    public static boolean passwordsMatch(RegisterRequestUserDTO dto) {
        String password = dto.getPassword().trim();
        String confirmPassword = dto.getConfirmPassword().trim();
        if (password == null || confirmPassword == null) {
            throw new BadRequestException("You have to enter a valid password");
        }
        return dto.getPassword().equals(dto.getConfirmPassword());
    }

    public static boolean passwordIsValid(String password) {
        if (password == null) {
            throw new BadRequestException("You have to enter a valid password");
        }
        boolean result = false;
        //At least one upper case, one lower case,one digit,one special character minimum eight characters
        String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
        if (password.matches(regex) && !password.contains(" ")) {
            result = true;
        }
        return result;
    }

    public static void validatePhoneNumber(String phoneNumber) {
        if (phoneNumber.length() > 0 && phoneNumber.length() < 10) {
            throw new BadRequestException("Wrong phone number");
        }
        for (int i = 0; i < phoneNumber.length(); i++) {
            if (!Character.isDigit(phoneNumber.charAt(i))) {
                throw new BadRequestException("Wrong phone number");
            }
        }
    }

    public static LocalDate validateBirthDate(String date) {
        try {
            LocalDate birthDate = LocalDate.parse(date);
            if (birthDate.isBefore(LocalDate.now().minusYears(10)) && birthDate.isAfter(LocalDate.now().minusYears(100))) {
                return birthDate;
            } else {
                throw new BadRequestException("Wrong birth date");
            }
        } catch (Exception e) {
            throw new BadRequestException("Enter a correct birth date");
        }
    }

    public static void validateName(String name) {
        if (name.length() < 3) {
            throw new BadRequestException("Enter correct name");
        }
        for (int i = 0; i < name.length(); i++) {
            char character = name.charAt(i);
            if (Character.isDigit(character)) {
                throw new BadRequestException("Enter correct name");
            }
        }
    }

    public static void checkIfAddressExists(Address address, User user) {
        if (address.getCity() != null & address.getStreet() != null) {
            List<Address> addresses = user.getAddresses();
            for (Address saved : addresses) {
                if (saved.getCity().equals(address.getCity()) && saved.getStreet().equals(address.getStreet())) {
                    throw new BadRequestException("Address is already added");
                }
            }
        }
    }

    public static void checkForDigitsAndSymbols(String address, String message,String validateBy) {
        String specialCharacters = "#?!@$%^&*-:'{}+_()<>|[]";
        if(validateBy.equals("digits and symbols")) {
            for (int i = 0; i < address.length(); i++) {
                if (Character.isDigit(address.charAt(i)) || specialCharacters.contains(String.valueOf(address.charAt(i)))) {
                    throw new BadRequestException(message);
                }
            }
        }else{
            if(validateBy.equals("symbols"))
                for (int i = 0; i < address.length(); i++) {
                    if (specialCharacters.contains(String.valueOf(address.charAt(i)))) {
                        throw new BadRequestException(message);
                    }
                }
        }
    }

}
