package com.yunjun.store2.mappers;

import com.yunjun.store2.dtos.ChangePasswordRequest;
import com.yunjun.store2.dtos.RegisterUserRequest;
import com.yunjun.store2.dtos.UpdateUserRequest;
import com.yunjun.store2.dtos.UserDto;
import com.yunjun.store2.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Manual Mapping code can be repetitive and error-prone.
 *
 * Libraries that help us map between DTOs and Entities:
 * ModelMapper: uses Reflection, it's slower and harder to debug
 * MapStruct: generates mapping code at compile time, it's superfast and save, free of runtime overhead.
 */
@Mapper(componentModel = "spring"/*, unmappedTargetPolicy = ReportingPolicy.WARN*/)
public interface UserMapper {

    User toEntity(RegisterUserRequest request);
    User toEntity(@MappingTarget User user, UpdateUserRequest request);

    // @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);
}
