package com.cfs.Bookmyshow.Controller;

import com.cfs.Bookmyshow.DTO.UserDto;
import com.cfs.Bookmyshow.DTO.UserRequestDto;
import com.cfs.Bookmyshow.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired private UserService userService;

    // POST /api/user/create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(userService.createUser(userRequestDto), HttpStatus.CREATED);
    }

    // GET /api/user/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserByID(id));
    }

    // GET /api/user/list
    @GetMapping("/list")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getALLUser());
    }

    // PUT /api/user/update  ← was MISSING — admin edit user calls this
    @PutMapping("/update")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserRequestDto userRequestDto) {
        return ResponseEntity.ok(userService.updateUser(userRequestDto));
    }

    // DELETE /api/user/delete/{id}  ← was MISSING — admin delete user calls this
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}