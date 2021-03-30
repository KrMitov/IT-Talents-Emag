package com.emag.service;

import com.emag.exceptions.AuthenticationException;
import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
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
import com.emag.model.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.io.*;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final int USER_ROLE_ID = 1;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserImageRepository userImageRepository;
    @Value("${file.path}")
    private String filePath;

  public RegisterResponseUserDTO register(RegisterRequestUserDTO dto){
      String email = dto.getEmail();
      if(!emailIsValid(email)){
          throw new BadRequestException("Incorrect email");
      }
      User userByEmail = userRepository.findByEmail(email);
      if(userByEmail!=null){
          throw new BadRequestException("Email already exists!");
      }
      if(!passwordsMatch(dto)){
          throw new BadRequestException("Passwords do not match!");
      }
      Optional<Role> roleFromDb = roleRepository.findById(USER_ROLE_ID);
      Role role = roleFromDb.get() ;
      RegisterResponseUserDTO response;
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      String password = dto.getPassword();
      if(!passwordIsValid(password)){
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

  public UserWithoutPasswordDTO login(LoginRequestUserDTO dto){
      String email = dto.getEmail();
      String password = dto.getPassword();
      User userFromDb = userRepository.findByEmail(email);
      if(userFromDb == null){
      throw new NotFoundException("Wrong email or password!");
      }
      String passwordFromDb = userFromDb.getPassword();
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      if(!encoder.matches(password,passwordFromDb)){
          throw new NotFoundException("Wrong email or password!");
      }
     return  new UserWithoutPasswordDTO(userFromDb);
  }

  public UserWithoutPasswordDTO findById(int id){
      Optional<User> userFromDb = userRepository.findById(id);
      if(userFromDb.isEmpty()){
          throw new NotFoundException("User does not exist!");
      }
      UserWithoutPasswordDTO dto = new UserWithoutPasswordDTO(userFromDb.get());
      return dto;
  }

  public UserWithoutPasswordDTO editUser(int id, EditProfileRequestDTO dto,HttpSession session) {
      this.verifyUserId(id,session,"You can not edit another user's profile!");
      Optional<User> userFromDb = userRepository.findById(id);
      if(userFromDb.isEmpty()){
          throw new NotFoundException("User not found!");
      }else{
          User user = userFromDb.get();
          if(dto.getOldPassword().length()>5){
              String password = dto.getOldPassword();
              PasswordEncoder encoder = new BCryptPasswordEncoder();
              if(encoder.matches(password,user.getPassword())){
                  String newPassword = dto.getNewPassword();
                  String confirmNewPassword = dto.getConfirmNewPassword();
                  if(newPassword.equals(confirmNewPassword)){
                      this.passwordIsValid(newPassword);
                      user.setPassword(encoder.encode(newPassword));
                      user = userRepository.save(user);
                  }else{
                      throw new BadRequestException("New passwords do not match!");
                  }
              }else{
                  throw new BadRequestException("Wrong password!");
              }
          }
          if(dto.getPhoneNumber().length()>0){
                  String phoneNumber = dto.getPhoneNumber();
                  this.validatePhoneNumber(phoneNumber);
                  user.setPhoneNumber(phoneNumber);
                  user = userRepository.save(user);
          }

          if(dto.getBirthDate()!=null) {
              LocalDate birthDate = this.validateDate(dto.getBirthDate());
              java.sql.Date sqlDate = java.sql.Date.valueOf( birthDate );
              user.setBirthDate(sqlDate);
              user = userRepository.save(user);
          }

         if(dto.getAddress()!=null){
             Address address = new Address(dto.getAddress());
             if(containsAddress(address,user)){
                 throw new BadRequestException("Address is already added");
             }else{
                 address = addressRepository.save(address);
                 user.getAddresses().add(address);
                 user = userRepository.save(user);
             }
         }

         return  new UserWithoutPasswordDTO(user);
      }
  }

  public LikedProductsForUserDTO getLikedProducts(int userId,HttpSession session){
      this.verifyUserId(userId,session,"You can not get liked products for another user!");
      Optional<User> userFromDb = userRepository.findById(userId);
      User user = userFromDb.get();
      return new LikedProductsForUserDTO(user);
  }

  public ProductsFromCartForUserDTO getProductsFromCart(int id,HttpSession session){
      this.verifyUserId(id,session,"You can not get products from cart for another user!");
      Optional<User> userFromDb = userRepository.findById(id);
      User user = userFromDb.get();
      return new ProductsFromCartForUserDTO(user);
  }

  public UserReviewsDTO getReviews(int userId,HttpSession session){
      this.verifyUserId(userId,session,"You can not get reviews by  another user!");
      Optional<User> userFromDb = userRepository.findById(userId);
      if(userFromDb.isEmpty()){
          throw new NotFoundException("user not found");
      }
      User user = userFromDb.get();
      return new UserReviewsDTO(user);
  }

  private boolean containsAddress(Address address,User user){
      boolean result = false;
      List<Address> addresses = user.getAddresses();
      for (Address saved : addresses) {
          if(saved.getCity().equals(address.getCity()) && saved.getStreet().equals(address.getStreet())){
             result = true;
          }
      }
      return result;
  }

   @Transactional
   public UserImage uploadImage(MultipartFile file,int userId) throws IOException {
      if(file == null){
          throw new BadRequestException("You have to select an image");
      }
      File physicalFile = new File(filePath+File.separator+System.nanoTime()+".png");
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
      Optional<User> userFromDb = userRepository.findById(userId);
      if(userFromDb.isEmpty()){
          throw new NotFoundException("User does not exist");
      }
      User user = userFromDb.get();
      if(user.getImage() == null){
          throw new NotFoundException("User does not have a profile picture");
      }
      String imageUrl = user.getImage().getUrl();
      File physicalFile = new File(imageUrl);
      return Files.readAllBytes(physicalFile.toPath());
  }

   public UserOrdersDTO getOrders(int userId,HttpSession session){
       this.verifyUserId(userId,session,"You can not get orders for another user!");
       Optional<User> userFomDb = userRepository.findById(userId);
       if(userFomDb.isEmpty()){
           throw new NotFoundException("User not found");
       }
       User user = userFomDb.get();
       return new UserOrdersDTO(user);
   }

   private boolean emailIsValid(String email){
       boolean result = false;
       String specialCharacters = "#?!@$%^&*-:'{}+_()<>|[]";
       //letters and numbers and "_","." before "@" symbol
       String regex = "^[A-Za-z0-9+_.]+@(.+)$";
       if(email.matches(regex)){
           int startCharacter = 0;
           for (int i = 0; i < email.length(); i++) {
               if(email.charAt(i) == '@'){
                   startCharacter = i;
               }
           }
           for (int i = startCharacter+1; i <email.length() ; i++) {
               char character = email.charAt(i);
               if(!specialCharacters.contains(String.valueOf(character)) || !Character.isDigit(character)) {
                   result = true;
               }else{
                   result = false;
                   break;
               }
           }
       }
       return result;
   }

   private boolean passwordIsValid(String password){
      boolean result = false;
      //At least one upper case, one lower case,one digit,one specail character minimum eight characters
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
    private void verifyUserId(int userId, HttpSession session,String message){
        int loggedUserId = (int) session.getAttribute("LOGGED_USER_ID");
        if(userId!=loggedUserId){
            throw new AuthenticationException(message);
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
   private void validateName(String name){
      if(name.length()<=3){
          throw new BadRequestException("Enter correct name");
      }else{
          for (int i = 0; i < name.length(); i++) {
              char character = name.charAt(i);
              if(Character.isDigit(character)){
                  throw new BadRequestException("Enter correct name");
              }
          }
      }
   }

}
