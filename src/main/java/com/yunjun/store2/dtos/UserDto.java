package com.yunjun.store2.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Customizing Responses using
 * @JsonIgnore: for ignoring properties.
 * @JsonProperty: for renaming properties.
 * @JsonInclude: for excluding properties.
 * @JsonFormat: for datetime, currency, etc.
 */
@Getter
@AllArgsConstructor
public class UserDto {
//    @JsonProperty("user_id")
    private Long id;
    private String name;
    private String email;

    /*@JsonIgnore
    private String password;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String phoneNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;*/
}
