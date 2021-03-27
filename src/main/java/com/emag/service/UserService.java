package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.*;
import com.emag.model.dto.registerdto.RegisterRequestUserDTO;
import com.emag.model.dto.registerdto.RegisterResponseUserDTO;
import com.emag.model.dto.userdto.UserWithoutPasswordDTO;
import com.emag.model.pojo.Address;
import com.emag.model.pojo.Role;
import com.emag.model.pojo.User;
import com.emag.model.repository.AddressRepository;
import com.emag.model.repository.RoleRepository;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final int USER_ROLE_ID = 1;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AddressRepository addressRepository;

  public RegisterResponseUserDTO register(RegisterRequestUserDTO dto){
      String email = dto.getEmail();
      User userByEmail = userRepository.findByEmail(email);
      if(userByEmail!=null){
          throw new BadRequestException("email already exists");
      }

      Optional<Role> roleFromDb = roleRepository.findById(1);
      Role role = roleFromDb.get() ;
      RegisterResponseUserDTO response;
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      dto.setPassword(encoder.encode(dto.getPassword()));
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
      throw new NotFoundException("wrong email or password");
      }
      String passwordFromDb = userFromDb.getPassword();
      PasswordEncoder encoder = new BCryptPasswordEncoder();
      if(!encoder.matches(password,passwordFromDb)){
          throw new NotFoundException("wrong email or password");
      }
     return  new UserWithoutPasswordDTO(userFromDb);
  }

  public UserWithoutPasswordDTO findById(int id){
      Optional<User> userFromDb = userRepository.findById(id);
      if(userFromDb.isEmpty()){
          throw new NotFoundException("User does not exist");
      }
      UserWithoutPasswordDTO dto = new UserWithoutPasswordDTO(userFromDb.get());
      return dto;
  }

  public UserWithoutPasswordDTO editUser(int id, EditProfileRequestDTO dto) {
      Optional<User> userFromDb = userRepository.findById(id);
      if(userFromDb.isEmpty()){
          throw new NotFoundException("user not found");
      }else{
          User user = userFromDb.get();
          if(dto.getOldPassword().length()>5){
              String password = dto.getOldPassword();
              PasswordEncoder encoder = new BCryptPasswordEncoder();
              if(encoder.matches(password,user.getPassword())){
                  String newPassword = dto.getNewPassword();
                  String confirmNewPassword = dto.getConfirmNewPassword();
                  if(newPassword.equals(confirmNewPassword)){
                      user.setPassword(encoder.encode(newPassword));
                      user = userRepository.save(user);
                  }else{
                      throw new BadRequestException("new passwords do not match");
                  }
              }else{
                  throw new BadRequestException("wrong password");
              }
          }
          if(dto.getPhoneNumber().length()>0){
              if(dto.getPhoneNumber().length()==10) {
                  String phoneNumber = dto.getPhoneNumber();
                  user.setPhoneNumber(phoneNumber);
                  user = userRepository.save(user);
              }else{
                  throw new BadRequestException("wrong phone number");
              }
          }
          if(dto.getBirthDate().length()==10){
              String dtoDate = dto.getBirthDate();
//              DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MMM-dd");
//              LocalDate date = LocalDate.parse(dtoDate,formatter);
              user.setBirthDate(Timestamp.valueOf(dtoDate));
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

}
