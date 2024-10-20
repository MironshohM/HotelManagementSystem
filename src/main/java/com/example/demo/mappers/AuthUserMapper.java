package com.example.demo.mappers;

import com.example.demo.dtos.auth.AuthUserCreateDTO;
import com.example.demo.entity.User;
import jakarta.annotation.Nonnull;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AuthUserMapper {

    User toEntity(@Nonnull AuthUserCreateDTO dto);
}
