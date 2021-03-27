package com.emag.service;

import com.emag.exceptions.BadRequestException;
import com.emag.exceptions.NotFoundException;
import com.emag.model.dto.LoginRequestUserDTO;
import com.emag.model.dto.RegisterRequestUserDTO;
import com.emag.model.dto.RegisterResponseUserDTO;
import com.emag.model.dto.UserWithoutPasswordDTO;
import com.emag.model.pojo.Role;
import com.emag.model.pojo.User;
import com.emag.model.repository.RoleRepository;
import com.emag.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

  public RegisterResponseUserDTO register(RegisterRequestUserDTO dto){
      String email = dto.getEmail();
      User userByEmail = userRepository.findByEmail(email);
      if(userByEmail!=null){
          throw new BadRequestException("email already exists");
      }
      if(dto.getRole().getId() <1 || dto.getRole().getId()>2){
          throw new BadRequestException("wrong role credentials");
      }
      Optional<Role> roleFromDb = roleRepository.findById(dto.getRole().getId());
      Role role = new Role();
      RegisterResponseUserDTO response;
      if(roleFromDb.isEmpty()){
          role.setRole_type(dto.getRole().getRole_Type());
          role = roleRepository.save(role);
      }else {
          role = roleFromDb.get();
      }
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


}
