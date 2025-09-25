package com.kirylliuss.shop.mapper;

import com.kirylliuss.shop.dto.request.UserRequest;
import com.kirylliuss.shop.dto.respons.UserRespons;
import com.kirylliuss.shop.model.User;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-25T11:01:20+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.8 (Arch Linux)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUser(UserRequest userRequest) {
        if ( userRequest == null ) {
            return null;
        }

        User user = new User();

        user.setEmail( userRequest.getEmail() );
        user.setBirthDate( userRequest.getBirthDate() );
        user.setSurname( userRequest.getSurname() );
        user.setName( userRequest.getName() );

        return user;
    }

    @Override
    public UserRespons toUserResponse(User user) {
        if ( user == null ) {
            return null;
        }

        UserRespons userRespons = new UserRespons();

        userRespons.setEmail( user.getEmail() );
        userRespons.setBirthDate( user.getBirthDate() );
        userRespons.setSurname( user.getSurname() );
        userRespons.setName( user.getName() );
        userRespons.setId( user.getId() );

        return userRespons;
    }

    @Override
    public void updateUserFromUserRequest(User user, UserRequest userRequest) {
        if ( userRequest == null ) {
            return;
        }

        user.setEmail( userRequest.getEmail() );
        user.setBirthDate( userRequest.getBirthDate() );
        user.setSurname( userRequest.getSurname() );
        user.setName( userRequest.getName() );
    }
}
