package com.tracker.area.user.mapper;

import com.tracker.area.user.domain.User;
import com.tracker.area.user.models.UserInfoDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDTO userToUserInfoDTO(User user);

    List<UserInfoDTO> usersToUserInfoDTOs(List<User> users);
}
