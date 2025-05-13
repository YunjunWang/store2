package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.ChangePasswordRequest;
import com.yunjun.store2.dtos.RegisterUserRequest;
import com.yunjun.store2.dtos.UpdateUserRequest;
import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.mappers.UserMapper;
import com.yunjun.store2.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

/**
 * Use @RequestMapping to define the URL path in general purpose
 *
 * @Controller is used to return HTML views.
 * @RestController is a convenience annotation that returns JSON data directly.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    /**
     * Use @GetMapping to define the URL path for a GET request.
     * Use @PostMapping to define the URL path for a POST request.
     * Use @PutMapping to define the URL path for a PUT request.
     * Use @DeleteMapping to define the URL path for a DELETE request.
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsers(@RequestParam(name="sort", required = false, defaultValue = "") String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";
        return ResponseEntity.ok(userService.getAllUsers(sortBy));
    }

    /**
     *
     * Entity classes are not meant to be used outside the service layer, it is our Domain classes.
     * DTO classes are meant to be used outside the service layer for business logic and in/outputs of the API.
     * Use DTO classes to return the data in a more structured way and hide non-essential data
     *
     * Use Mapper classes to convert between Entity and DTO classes.
     *
     * Use ResponseEntity to return the status code and the data in the response body.
     * Use @PathVariable to extract the path variable from the URL.
     * Give the name of the path variable to the method parameter,
     * so the url can be /users/123 rather than /users?id=123.
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable("id") Long id) {
        var user = userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) throws IllegalArgumentException{
        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        var userDto = userService.updateUser(id, request);
        if (userDto == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.ok(userDto);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteUser(@PathVariable("id") Long id) {
        if (!userService.deleteUser(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build() ;
    }

    /**
     * use Put for updating the whole entity,
     * use Patch for updating only some of the entity's properties.
     * use Post for creating a new entity or any user actions e.g., change the password, submit an approval, etc.
     * @param id
     * @param request
     * @return
     */
    @PostMapping("/{id}/change-password")
    public ResponseEntity<Void> changePassword(@PathVariable("id") Long id, @Valid @RequestBody ChangePasswordRequest request) {
        try {
            var userDto = userService.changePassword(request, id);
            if (userDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.noContent().build();
        } catch (IllegalAccessException | IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
