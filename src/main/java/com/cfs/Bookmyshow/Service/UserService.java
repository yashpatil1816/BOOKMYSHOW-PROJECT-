package com.cfs.Bookmyshow.Service;

import com.cfs.Bookmyshow.DTO.UserDto;
import com.cfs.Bookmyshow.DTO.UserRequestDto;
import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
import com.cfs.Bookmyshow.Repository.UserRepo;
import com.cfs.Bookmyshow.model.User;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
    public UserDto createUser( UserRequestDto userRequestDto){
        User user=mapToEntity(userRequestDto);

        // pasword is remaning , bacuse of encoder and , security
        User saveuser = userRepo.save(user);
        return mapToDto(saveuser);


    }

    public UserDto getUserByID(Long Id){
        User user = userRepo.findById(Id)
                .orElseThrow(()->new ResouceNotFoundException("User not Found  with id : "+Id));
        return  mapToDto(user);
    }


    public List<UserDto> getALLUser(){
        List<User> users = userRepo.findAll();
     return users.stream()
             .map(this::mapToDto)
             .collect(Collectors.toList());
    }

    private UserDto mapToDto(User saveuser) {
        UserDto userDto = new UserDto();
         userDto.setId(saveuser.getId());
         userDto.setUsername(saveuser.getName());
         userDto.setPhoneNumber(saveuser.getPhonenumber());
         userDto.setEmail(saveuser.getEmail());
         return userDto;

    }

    private User mapToEntity(UserRequestDto userDto) {
          User user = new User();
//          user.setId(userDto.getId());  // not to map it at service layer , it is already autpgentrated at DB
          user.setName(userDto.getUsername());
          user.setEmail(userDto.getEmail());
          user.setPhonenumber(userDto.getPhoneNumber());
//          user.setPassword(passwordEncoder.encode(userDto.getPassword()));
          user.setRole(userDto.getRole());////----------------
          return user;
    }


    public UserDto updateUser(UserRequestDto userDto){
        User user = userRepo.findById(userDto.getId())
                .orElseThrow(()-> new ResouceNotFoundException("User not found "));
        user.setName(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPhonenumber(userDto.getPhoneNumber());
//        user.setPassword(passwordEncoder.encode(userDto.getPassword()));


        User saveuser = userRepo.save(user);
        return mapToDto(saveuser);
    }

    public void deleteUser(Long id){
        User user = userRepo.findById(id)
                .orElseThrow(()-> new ResouceNotFoundException("User not found "));

      userRepo.delete(user);

    }



}
