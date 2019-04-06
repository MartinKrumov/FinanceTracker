package com.tracker.mapper;

import com.tracker.domain.Authority;
import com.tracker.domain.User;
import com.tracker.dto.user.UserInfoDTO;
import com.tracker.dto.user.UserLoginDTO;
import com.tracker.dto.user.UserRegistrationModel;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserInfoDTO userToUserInfoDTO(User user);

    List<UserInfoDTO> usersToUserInfoDTOs(List<User> users);

    User convertToUser(UserRegistrationModel userRegistrationModel);

    default UserLoginDTO userToUserLoginDTO(User user) {
        if (isNull(user)) {
            return null;
        }

        return UserLoginDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(mapUserAuthorities(user))
                .build();
    }

    @Named("mapAuthorities")
    default Set<String> mapUserAuthorities(User user) {
        if (isNull(user.getAuthorities())) {
            return Collections.emptySet();
        }

        return user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toSet());
    }
}
