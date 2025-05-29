package com.yunjun.store2.users;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Use @RequestMapping to define the URL path in general purpose
 *
 * @Controller is used to return HTML views.
 * @RestController is a convenience annotation that returns JSON data directly.
 *
 * This class should be responsible for handling all requests related to users,
 * including registration, and user profile management, changing password.
 */
@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
@Tag(name = "users", description = "Operations about users")
public class UserController {
    private final UserService userService;

    /**
     * @return
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    public List<UserDto> getUsers(@RequestParam(name="sort", required = false, defaultValue = "") String sortBy) {
        return userService.getAllUsers(sortBy);
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder) throws IllegalArgumentException{
        var userDto = userService.registerUser(request);
        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();
        return ResponseEntity.created(uri).body(userDto);
    }

    /**
     * Use @GetMapping to define the URL path for a GET request.
     * Use @PostMapping to define the URL path for a POST request.
     * Use @PutMapping to define the URL path for a PUT request.
     * Use @DeleteMapping to define the URL path for a DELETE request.
     *
     */

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
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a user")
    public UserDto getUser(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Update a user")
    public UserDto updateUser(
            @PathVariable("id") Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        return userService.updateUser(id, request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a user")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Change the password of a user")
    public void changePassword(
            @PathVariable("id") Long id,
            @Valid @RequestBody ChangePasswordRequest request) throws IllegalAccessException {
            userService.changePassword(request, id);
    }
}
