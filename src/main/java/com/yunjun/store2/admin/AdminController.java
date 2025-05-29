package com.yunjun.store2.admin;

import com.yunjun.store2.users.UserDto;
import com.yunjun.store2.users.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserService userService;

    /**
     * @return
     */
    @GetMapping("/users")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all users")
    public List<UserDto> getUsers(@RequestParam(name="sort", required = false, defaultValue = "") String sortBy) {
        if (!Set.of("name", "email").contains(sortBy))
            sortBy = "name";
        return userService.getAllUsers(sortBy);
    }
}
