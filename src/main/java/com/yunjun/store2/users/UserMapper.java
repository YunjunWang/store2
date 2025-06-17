package com.yunjun.store2.users;

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

    User update(RegisterUserRequest request);
    void update(@MappingTarget User user, UpdateUserRequest request);

    // @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    UserDto toDto(User user);

    User toEntity(UserDto userDto);
}
