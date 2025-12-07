package com.epam.finaltask.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if (user == null) return null;

        String id = user.getId() != null ? user.getId().toString() : null;
        String role = user.getRole() != null ? user.getRole().name() : null;
        Double balance = user.getBalance() != null ? user.getBalance().doubleValue() : null;

        String password = user.getPassword();

        List<String> voucherIds = null;

        return new UserDTO(
                id,
                user.getUsername(),
                password,
                role,
                voucherIds,
                user.getPhoneNumber(),
                balance,
                user.isActive()
        );
    }

    @Override
    public User toUser(UserDTO dto) {
        if (dto == null) return null;

        User user = new User();

        if (dto.id() != null && !dto.id().isBlank()) {
            try {
                user.setId(UUID.fromString(dto.id()));
            } catch (IllegalArgumentException ignored) {
            }
        }

        user.setUsername(dto.username());
        user.setPassword(dto.password());

        if (dto.role() != null) {
            user.setRole(Role.valueOf(dto.role()));
        }

        user.setPhoneNumber(dto.phoneNumber());

        if (dto.balance() != null) {
            user.setBalance(BigDecimal.valueOf(dto.balance()));
        }

        user.setActive(dto.active());


        return user;
    }
}
