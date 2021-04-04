package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dao.UserCartDAO;
import com.emag.model.dao.UserDAO;
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
import com.emag.util.UserUtility;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.io.*;
import java.nio.file.Files;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService extends AbstractService {

    private static final int USER_ROLE_ID = 1;
    @Autowired
    UserDAO userDAO;

    public RegisterResponseUserDTO register(RegisterRequestUserDTO dto) {
        String email = dto.getEmail();
        if (userRepository.findByEmail(email) != null) {
            throw new BadRequestException("Email already exists!");
        }
        if (!UserUtility.emailIsValid(email)) {
            throw new BadRequestException("Incorrect email");
        }
        String password = dto.getPassword();
        if (!UserUtility.passwordIsValid(password)) {
            throw new BadRequestException("Password is too weak!");
        }
        if (!UserUtility.passwordsMatch(dto)) {
            throw new BadRequestException("Passwords do not match!");
        }
        Role role = roleRepository.findById(USER_ROLE_ID).get();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        UserUtility.validateName(dto.getName());
        dto.setPassword(encoder.encode(password));
        User user = new User(dto);
        user.setRole(role);
        user = userRepository.save(user);
        return new RegisterResponseUserDTO(user);
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
        User user = getUserIfExists(id);
        return new UserWithoutPasswordDTO(user);
    }

    public UserWithoutPasswordDTO editUser(int id, EditProfileRequestDTO dto) {
        User user = getUserIfExists(id);
        if (dto.getOldPassword().length() > 0){
            String password = dto.getOldPassword();
            PasswordEncoder encoder = new BCryptPasswordEncoder();
            if (encoder.matches(password, user.getPassword())) {
                if (dto.getNewPassword().equals(dto.getConfirmNewPassword())) {
                    if(!UserUtility.passwordIsValid(dto.getNewPassword())){
                        throw new BadRequestException("Enter correct new password");
                    }
                    user.setPassword(encoder.encode(dto.getNewPassword()));
                    user = userRepository.save(user);
                } else {
                    throw new BadRequestException("New passwords do not match!");
                }
            } else {
                throw new BadRequestException("Wrong password!");
            }
        }
        if (dto.getPhoneNumber() != null && dto.getPhoneNumber().length()>0){
            String phoneNumber = dto.getPhoneNumber();
            UserUtility.validatePhoneNumber(phoneNumber);
            user.setPhoneNumber(phoneNumber);
            user = userRepository.save(user);
        }

        if (dto.getBirthDate() != null && dto.getBirthDate().length()>0){
            LocalDate birthDate = UserUtility.validateBirthDate(dto.getBirthDate());
            Date sqlDate = Date.valueOf(birthDate);
            user.setBirthDate(sqlDate);
            user = userRepository.save(user);
        }

        if (dto.getAddress() != null) {
            if(!UserUtility.addressIsEmpty(dto.getAddress())) {
                Address address = validateAddress(dto.getAddress());
                UserUtility.checkIfAddressExists(address, user);
                address = addressRepository.save(address);
                user.getAddresses().add(address);
                user = userRepository.save(user);
            }
        }
        return new UserWithoutPasswordDTO(user);
    }

    public LikedProductsForUserDTO getLikedProducts(int userId) {
        User user = getUserIfExists(userId);
        return new LikedProductsForUserDTO(user);
    }

    public UserOrdersDTO getOrders(int userId) {
        User user = getUserIfExists(userId);
        return new UserOrdersDTO(user);
    }

    public ProductsFromCartForUserDTO getProductsFromCart(int id) {
        User user = getUserIfExists(id);
        return new ProductsFromCartForUserDTO(user);
    }

    @Transactional
    public UserImage uploadImage(MultipartFile file, int userId) throws IOException {
        if (file.isEmpty()) {
            throw new BadRequestException("No image uploaded");
        }
        Tika tika = new Tika();
        String detectedType = tika.detect(file.getBytes());
        List<String> acceptedImageMimeTypes = Arrays.asList(ACCEPTED_IMAGE_MIME_TYPES);
        if (!acceptedImageMimeTypes.contains(detectedType)){
            throw new BadRequestException("Image type not supported!");
        }
        File physicalFile = new File(filePath + File.separator + System.nanoTime() + "." +
                detectedType.substring(detectedType.indexOf("/") + 1));
        UserImage userImage = new UserImage();
        try (OutputStream os = new FileOutputStream(physicalFile)){
            os.write(file.getBytes());
            userImage.setUrl(physicalFile.getAbsolutePath());
            Optional<User> userFromDb = userRepository.findById(userId);
            User user = userFromDb.get();
            userImage = userImageRepository.save(userImage);
            user.setImage(userImage);
            userRepository.save(user);
        }
        return userImage;
    }

    public byte[] downloadImage(int userId) throws IOException {
        User user = getUserIfExists(userId);
        if (user.getImage() == null) {
            throw new NotFoundException("User does not have a profile picture");
        }
        String imageUrl = user.getImage().getUrl();
        File physicalFile = new File(imageUrl);
        return Files.readAllBytes(physicalFile.toPath());
    }

    public UserReviewsDTO getReviews(int userId) {
        User user = getUserIfExists(userId);
        return new UserReviewsDTO(user);
    }

    private Address validateAddress(AddressDTO addressDTO) {
        if (addressDTO.getCountry() != null && addressDTO.getCountry().length() > 0) {
            checkForDigitsAndSymbols(addressDTO.getCountry(), "You entered a wrong country","digits and symbols");
        }
        if (addressDTO.getProvince() != null && addressDTO.getProvince().length() > 0) {
            checkForDigitsAndSymbols(addressDTO.getProvince(), "You entered a wrong province","digits and symbols");
        }
        if (addressDTO.getCity() != null && addressDTO.getCity().length() > 0) {
            checkForDigitsAndSymbols(addressDTO.getCity(), "You entered a wrong city","digits and symbols");
        }
        if(addressDTO.getNeighborhood() !=null && addressDTO.getNeighborhood().length() > 0){
            checkForDigitsAndSymbols(addressDTO.getNeighborhood(),"You entered a wrong neighborhood","symbols");
        }
        if (addressDTO.getStreet() !=null && addressDTO.getStreet().length() > 0) {
            if (addressDTO.getProvince() == null || addressDTO.getCity() == null) {
                throw new BadRequestException("You have to enter province and city values");
            } else {
                if (addressDTO.getProvince().length() < 4 || addressDTO.getCity().length() < 3) {
                    throw new BadRequestException("You have to enter correct province and city values");
                }
            }
            if (addressDTO.getStreet().length() < 5) {
                throw new BadRequestException("Enter correct street name");
            }
            checkForDigitsAndSymbols(addressDTO.getStreet(), "You entered wrong street", "digits and symbols");
        }
        if(addressDTO.getStreetNumber() != null){
            if(addressDTO.getProvince() == null && addressDTO.getCity() == null && addressDTO.getStreet() == null){
                throw new BadRequestException("Enter province , city and street");
            }else {
                if (addressDTO.getProvince().length() < 4 || addressDTO.getCity().length() < 3 || addressDTO.getStreet().length() < 5) {
                    throw new BadRequestException("Enter valid province,city and street");
                }
            }
            for (int i = 0; i < addressDTO.getStreetNumber().length(); i++) {
                if (!Character.isDigit(addressDTO.getStreetNumber().charAt(i))) {
                    throw new BadRequestException("You entered wrong street number");
                }
            }
        }
        return new Address(addressDTO);
    }

    private void checkForDigitsAndSymbols(String address, String message,String validateBy) {
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

    @Scheduled(cron = "0 * * * * *")
    public void printStatistics(){
      userDAO.getUsersWhoOrdered("2021-03-31");
      userDAO.getOrdersByCity("Burgas",2);
      userDAO.getUsersByEmailAndProducts("abv.bg",2,1000,3000);
      userDAO.printUsersWithMostOrders(5);
    }

}
