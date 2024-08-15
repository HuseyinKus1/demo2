package com.fmss.userservice.mapper;


import com.fmss.userservice.dto.request.UpdateUserRequest;
import com.fmss.userservice.dto.response.BaseUserResponse;
import com.fmss.userservice.dto.request.RegisterUserRequest;
import com.fmss.userservice.model.enums.Role;
import com.fmss.userservice.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

//@Mapper(implementationName = "UserMapperImpl", componentModel = "spring",uses = PasswordEncoderMapper.class)
@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordEncoderMapper passwordEncoderMapper;
    public User toUser(RegisterUserRequest registerUserRequest) {
        if ( registerUserRequest == null ) {
            return null;
        }
        User.UserBuilder user = User.builder();
        user.password(passwordEncoderMapper.encode(registerUserRequest.getPassword()));
        user.username( registerUserRequest.getUsername() );
        user.email( registerUserRequest.getEmail() );
        user.role(Role.ROLE_USER);
        return user.build();
    }
    public BaseUserResponse toBaseUserResponse(User user){
        BaseUserResponse.BaseUserResponseBuilder baseUserResponse = BaseUserResponse.builder();
        baseUserResponse.username(user.getUsername());
        baseUserResponse.email(user.getEmail());
        baseUserResponse.balance(user.getBalance());
        return baseUserResponse.build();
    }

    public User updateUser(User user, UpdateUserRequest updateUserRequest){
        user.setUsername(updateUserRequest.getUsername());
        user.setName(updateUserRequest.getName());
        user.setImageUrl(updateUserRequest.getImageUrl());
        user.setPassword(updateUserRequest.getPassword());
        user.setLastName(updateUserRequest.getLastName());
        return user;
    }
}
