package com.tracker.mapper;

import com.tracker.domain.User;
import com.tracker.rest.dto.user.UserDetailsDTO;
import com.tracker.rest.dto.user.UserInfoDTO;
import com.tracker.rest.dto.user.UserRegisterDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Mapper(componentModel = "spring")
public interface UserMapper {
    //TODO: remove unused methods

    UserInfoDTO userToUserInfoDTO(User user);

    List<UserInfoDTO> usersToUserInfoDTOs(List<User> users);

    User toUser(UserRegisterDTO userRegisterDTO);

    @Mapping(target = "password", ignore = true)
    UserRegisterDTO toDtoIgnorePassword(User user);

    default UserDetailsDTO userToUserLoginDTO(User user) {
        if (isNull(user)) {
            return null;
        }

        return UserDetailsDTO.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .authorities(mapUserRoles(user))
                .build();
    }

    @Named("mapRoles")
    default Set<String> mapUserRoles(User user) {
        if (isNull(user.getRoles())) {
            return Collections.emptySet();
        }

        return user.getRoles().stream()
                .map(role -> role.getRole().name())
                .collect(Collectors.toSet());
    }
}
