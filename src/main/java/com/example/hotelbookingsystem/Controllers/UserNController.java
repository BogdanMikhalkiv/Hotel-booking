package com.example.hotelbookingsystem.Controllers;

import com.example.hotelbookingsystem.Models.UserN;
import com.example.hotelbookingsystem.service.UserNService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/users")
@AllArgsConstructor
public class UserNController {

    private final UserNService userNService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public List<UserN> getGuests() {
        return userNService.getUserList();
    }

    @PutMapping("update_user")
    public String updateUser(@RequestBody UserN userN) {
        userNService.updateUser(userN);
        return "guest was updated";
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @PatchMapping("update_user_field/{id}")
    public String updateUserField(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        UserN userN = userNService.findByIdUser(id);
         if (userN == null) {
             return "нету такого гостя под таким id - " + id;
         } else {
            if (updates.containsKey("name")) {
                userN.setName(String.valueOf( updates.get("name")));
            }
            if (updates.containsKey("email")) {
                userN.setEmail(String.valueOf(updates.get("email")));
            }
            if (updates.containsKey("surname")) {
                userN.setSurname(String.valueOf(updates.get("surname")));
            }
            if (updates.containsKey("password")) {
                userN.setPassword(String.valueOf(updates.get("password")));
            }
             userNService.updateUser(userN);
             return "guest was updated";
         }
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    @DeleteMapping("delete_user/{id}")
    public String deleteUser(@PathVariable Long id) {
        userNService.deleteUser(id);
        return "Guest was deleted by id - " + id;
    }
}
