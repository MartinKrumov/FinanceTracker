package com.tracker.mapper;

import com.tracker.domain.User;
import com.tracker.rest.dto.user.UserInfoDTO;
import com.tracker.rest.dto.user.UserRegisterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDTO userToUserInfoDTO(User user);

    List<UserInfoDTO> usersToUserInfoDTOs(List<User> users);

    User toUser(UserRegisterDTO userRegisterDTO);

    @Mapping(target = "password", ignore = true)
    UserRegisterDTO toDtoIgnorePassword(User user);
}
