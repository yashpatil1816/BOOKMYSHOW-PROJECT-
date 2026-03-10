package com.cfs.Bookmyshow.DTO;

import com.cfs.Bookmyshow.model.roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {

    private Long id;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Set<roles> role =new HashSet<>();
}
