package com.bestseller.starbux.mapper;

import com.bestseller.starbux.domain.dto.UserDTO;
import com.bestseller.starbux.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper( UserMapper.class );

    UserDTO userToUserDTO(User user);
}
