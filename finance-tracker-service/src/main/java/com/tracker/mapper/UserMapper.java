package com.tracker.mapper;

import com.tracker.domain.User;
import com.tracker.dto.user.UserInfoDTO;
import com.tracker.dto.user.UserRegistrationModel;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDTO userToUserInfoDTO(User user);

    List<UserInfoDTO> usersToUserInfoDTOs(List<User> users);

    User convertToUser(UserRegistrationModel userRegistrationModel);
}
