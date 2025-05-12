package com.yunjun.store2.controllers;

import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.entities.User;
import com.yunjun.store2.services.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Use @GetMapping to define the URL path for a GET request.
     * Use @PostMapping to define the URL path for a POST request.
     * Use @PutMapping to define the URL path for a PUT request.
     * Use @DeleteMapping to define the URL path for a DELETE request.
     *
     * @return
     */
    @GetMapping
    public List<UserDto> getUsers(@RequestParam(name="sort", required = false, defaultValue = "") String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";
        return userService.getAllUsers(sortBy);
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
}
