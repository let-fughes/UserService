package com.kirylliuss.shop.mapper;

import com.kirylliuss.shop.dto.request.UserRequest;
import com.kirylliuss.shop.dto.respons.UserRespons;
import com.kirylliuss.shop.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // UserRequest -> User
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    User toUser(UserRequest userRequest);

    // User -> UserResponse
    @Mapping(target = "cards", ignore = true)
    UserRespons toUserResponse(User user);

    // Update User From UserRequest
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "cards", ignore = true)
    void updateUserFromUserRequest(@MappingTarget User user, UserRequest userRequest);
}
