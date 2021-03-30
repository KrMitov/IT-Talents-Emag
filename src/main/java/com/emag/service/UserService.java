package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.addressdto.AddressDTO;
import com.emag.model.dto.produtcdto.LikedProductsForUserDTO;
import com.emag.model.dto.produtcdto.ProductsFromCartForUserDTO;
import com.emag.model.dto.produtcdto.UserOrdersDTO;
import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.emag.model.dto.registerdto.RegisterResponseUserDTO;
import com.emag.model.dto.userdto.EditProfileRequestDTO;
import com.emag.model.dto.userdto.LoginRequestUserDTO;
import com.emag.model.dto.userdto.UserReviewsDTO;
import com.emag.model.dto.userdto.UserWithoutPasswordDTO;
import com.emag.model.pojo.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends AbstractService {

    private static final int USER_ROLE_ID = 1;

    public RegisterResponseUserDTO register(RegisterRequestUserDTO dto) {
        String email = dto.getEmail();
        User userByEmail = userRepository.findByEmail(email);
        if (userByEmail != null) {
            throw new BadRequestException("Email already exists!");
        }
        if (!emailIsValid(email)) {
            throw new BadRequestException("Incorrect email");
        }
        if (!passwordsMatch(dto)) {
            throw new BadRequestException("Passwords do not match!");
        }
        Optional<Role> roleFromDb = roleRepository.findById(USER_ROLE_ID);
        Role role = roleFromDb.get();
        RegisterResponseUserDTO response;
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = dto.getPassword();
        if (!passwordIsValid(password)) {
            throw new BadRequestException("Password is too weak!");
        }
        this.validateName(dto.getName());
        dto.setPassword(encoder.encode(password));
        User user = new User(dto);
        user.setRole(role);
        user = userRepository.save(user);
        response = new RegisterResponseUserDTO(user);
        return response;
    }

    public UserWithoutPasswordDTO login(LoginRequestUserDTO dto) {
        String email = dto.getEmail();
        String password = dto.getPassword();
        User userFromDb = userRepository.findByEmail(email);
        if (userFromDb == null) {
            throw new NotFoundException("Wrong email or password!");
        }
        String passwordFromDb = userFromDb.getPassword();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        if (!encoder.matches(password, passwordFromDb)) {
            throw new NotFoundException("Wrong email or password!");
        }
        return new UserWithoutPasswordDTO(userFromDb);
    }

    public UserWithoutPasswordDTO findById(int id) {
        User user = findUserById(id);
        UserWithoutPasswordDTO dto = new UserWithoutPasswordDTO(user);
        return dto;
    }

    public UserWithoutPasswordDTO editUser(int id, EditProfileRequestDTO dto) {
        User user = findUserById(id);
        if (dto.getOldPassword().length() >= 8) {
            String password = dto.getOldPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, user.getPassword())) {
                if (dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                    this.passwordIsValid(dto.getNewPassword());
                    user.setPassword(encoder.encode(dto.getNewPassword()));
                    user = userRepository.save(user);
                } else {
                    throw new BadRequestException("New passwords do not match!");
                }
            } else {
                throw new BadRequestException("Wrong password!");
            }
        }
        if (dto.getPhoneNumber().length() > 0) {
            String phoneNumber = dto.getPhoneNumber();
            this.validatePhoneNumber(phoneNumber);
            user.setPhoneNumber(phoneNumber);
            user = userRepository.save(user);
        }

        if (dto.getBirthDate() != null) {
            LocalDate birthDate = this.validateDate(dto.getBirthDate());
            java.sql.Date sqlDate = java.sql.Date.valueOf(birthDate);
            user.setBirthDate(sqlDate);
            user = userRepository.save(user);
        }

        if (dto.getAddress() != null) {
            Address address = validateAddress(dto.getAddress());
                checkIfAddressExists(address, user);
                address = addressRepository.save(address);
                user.getAddresses().add(address);
                user = userRepository.save(user);
        }
        return new UserWithoutPasswordDTO(user);
    }

    public LikedProductsForUserDTO getLikedProducts(int userId) {
        User user = findUserById(userId);
        return new LikedProductsForUserDTO(user);
    }

    public ProductsFromCartForUserDTO getProductsFromCart(int id) {
        Optional<User> userFromDb = userRepository.findById(id);
        User user = userFromDb.get();
        return new ProductsFromCartForUserDTO(user);
    }

    public UserReviewsDTO getReviews(int userId) {
        User user = findUserById(userId);
        return new UserReviewsDTO(user);
    }

    private void checkIfAddressExists(Address address, User user) {
        List<Address> addresses = user.getAddresses();
        for (Address saved : addresses) {
            if (saved.getCity().equals(address.getCity()) && saved.getStreet().equals(address.getStreet())) {
                throw new BadRequestException("Address is already added");
            }
        }
    }

    @Transactional
    public UserImage uploadImage(MultipartFile file, int userId) throws IOException {
        if (file == null) {
            throw new BadRequestException("You have to select an image");
        }
        File physicalFile = new File(filePath + File.separator + System.nanoTime() + ".png");
        UserImage userImage = new UserImage();
        OutputStream os = new FileOutputStream(physicalFile);
        os.write(file.getBytes());
        userImage.setUrl(physicalFile.getAbsolutePath());
        Optional<User> userFromDb = userRepository.findById(userId);
        User user = userFromDb.get();
        userImage = userImageRepository.save(userImage);
        user.setImage(userImage);
        userRepository.save(user);
        os.close();
        return userImage;

   }

    public byte[] downloadImage(int userId) throws IOException {
        User user = findUserById(userId);
        if (user.getImage() == null) {
            throw new NotFoundException("User does not have a profile picture");
        }
        String imageUrl = user.getImage().getUrl();
        File physicalFile = new File(imageUrl);
        return Files.readAllBytes(physicalFile.toPath());
    }

    public UserOrdersDTO getOrders(int userId) {
        User user = findUserById(userId);
        return new UserOrdersDTO(user);
    }

    private boolean emailIsValid(String email) {
        boolean result = false;
        String specialCharacters = "#?!@$%^&*-:'{}+_()<>|[]";
        //Letters, numbers and "_","." before "@" symbol
        String regex = "^[A-Za-z0-9+_.]+@(.+)$";
        if (email.matches(regex)) {
            int startCharacter = 0;
            for (int i = 0; i < email.length(); i++) {
                if (email.charAt(i) == '@') {
                    startCharacter = i;
                }
            }
            for (int i = startCharacter + 1; i < email.length(); i++) {
                char character = email.charAt(i);
                if (!specialCharacters.contains(String.valueOf(character)) || !Character.isDigit(character)) {
                    result = true;
                } else {
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

   private boolean passwordIsValid(String password){
      boolean result = false;
      //At least one upper case, one lower case,one digit,one special character minimum eight characters
      String regex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,}$";
      if(password.matches(regex)){
          result = true;
      }
      return  result;
   }

   private boolean passwordsMatch(RegisterRequestUserDTO dto){
      if(dto.getPassword().equals(dto.getConfirmPassword())){
          return  true;
      }else {
          return false;
      }
   }

    private void validatePhoneNumber(String phoneNumber){
        if(phoneNumber.length()!=10) {
            throw new BadRequestException("Wrong phone number");
        }
        for (int i = 0; i < phoneNumber.length(); i++) {
               if(!Character.isDigit(phoneNumber.charAt(i))){
                   throw new BadRequestException("Wrong phone number");
               }
        }
    }

    private LocalDate validateDate(Timestamp date){
         LocalDate birthDate = date.toLocalDateTime().toLocalDate();
         if(birthDate!=null){
             return birthDate;
         }else{
             throw new BadRequestException("Wrong birth date");
         }
    }

    private void validateName(String name) {
        if (name.length() <= 3) {
            throw new BadRequestException("Enter correct name");
        }
        for (int i = 0; i < name.length(); i++) {
            char character = name.charAt(i);
            if (Character.isDigit(character)) {
                throw new BadRequestException("Enter correct name");
            }
        }
    }

    private Address validateAddress(AddressDTO addressDTO) {
        if (addressDTO.getCountry().length() > 0) {
            checkForDigits(addressDTO.getCountry(), "You entered wrong country");
        }
        if (addressDTO.getProvince().length() > 0) {
            checkForDigits(addressDTO.getProvince(), "You entered wrong province");
        }
        if (addressDTO.getCity().length() > 0) {
            checkForDigits(addressDTO.getCity(), "You entered wrong city");
        }
        if (addressDTO.getStreet().length() > 0) {
            checkForDigits(addressDTO.getStreet(), "You entered wrong street");
        }
        for (int i = 0; i < addressDTO.getStreetNumber().length(); i++) {
            if (!Character.isDigit(addressDTO.getStreetNumber().charAt(i))) {
                throw new BadRequestException("You entered wrong street number");
            }
        }
        return new Address(addressDTO);
    }

    private void checkForDigits(String address, String message) {
        for (int i = 0; i < address.length(); i++) {
            if (Character.isDigit(address.charAt(i))) {
                throw new BadRequestException(message);
            }
        }
    }

    private User findUserById(int id) {
        if (userRepository.findById(id).isEmpty()) {
            throw new NotFoundException("User not found!");
        } else {
            Optional<User> userFromDb = userRepository.findById(id);
            return userFromDb.get();
        }
    }

}
