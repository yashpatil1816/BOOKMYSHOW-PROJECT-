//package com.cfs.Bookmyshow.Service;
//
//import com.cfs.Bookmyshow.Exception.ResouceNotFoundException;
//import com.cfs.Bookmyshow.Repository.UserRepo;
//import com.cfs.Bookmyshow.model.User;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomerUserdeatilService implements UserDetailsService {
//
//    @Autowired
//    private UserRepo userRepo;
//
//    @Override
// public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//     System.out.println("loaduserBynmae method called ");
//  User user = userRepo.findByEmail(username)
//          .orElseThrow(()-> new ResouceNotFoundException((" User Not found : "+username)));
////     System.out.println("Found username = "+user.getName() + " / " +user.getPassword());
//
//     return new org.springframework.security.core.userdetails.User(user.getEmail(),user.getPassword(),true,true,true,true,
//             //user.getRoles().stream().map(role->new SimpleGrantedAuthority(role.getName())).toList()
//             user.getRole().stream().map(role -> new SimpleGrantedAuthority("ROLE_" +role.getName())).toList());
// }
//}
