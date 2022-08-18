package com.rafael.peteventos.domain.converter;

import com.rafael.peteventos.domain.dto.UserDto;
import com.rafael.peteventos.domain.entity.AuthenticatedUser;
import com.rafael.peteventos.domain.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserDTOConverter {

    public static UserDto convertUser(User user) {
        return UserDto.builder()
                .withName(user.getName())
                .withEmail(user.getEmail())
                .withRegistration(user.getRegistration())
                .withIdUser(user.getIdUser())
                .withUserType(user.getUserType())
                .build();
    }

    public static List<UserDto> convertUserList(List<AuthenticatedUser> users) {
        return users.stream().map(UserDTOConverter::convertUser).collect(Collectors.toList());
    }
}
