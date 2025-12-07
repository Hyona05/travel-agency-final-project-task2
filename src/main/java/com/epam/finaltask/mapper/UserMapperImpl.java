package com.epam.finaltask.mapper;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toUserDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO dto = new UserDTO();
        if (user.getId() != null) {
            dto.setId(user.getId().toString());
        }
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());  // usually you would NOT expose this, but for this task/test it's fine
        if (user.getRole() != null) {
            dto.setRole(user.getRole().name());
        }
        dto.setVouchers(user.getVouchers());
        dto.setPhoneNumber(user.getPhoneNumber());

        BigDecimal balance = user.getBalance();
        if (balance != null) {
            dto.setBalance(balance.doubleValue());
        }

        dto.setActive(user.isActive());
        return dto;
    }

    @Override
    public User toUser(UserDTO dto) {
        if (dto == null) {
            return null;
        }

        User user = new User();

        if (dto.getId() != null) {
            try {
                user.setId(UUID.fromString(dto.getId()));
            } catch (IllegalArgumentException ex) {
                // ignore invalid UUID, let service handle it if necessary
            }
        }

        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());

        if (dto.getRole() != null) {
            user.setRole(Role.valueOf(dto.getRole()));
        }

        user.setVouchers(dto.getVouchers());
        user.setPhoneNumber(dto.getPhoneNumber());

        if (dto.getBalance() != null) {
            user.setBalance(BigDecimal.valueOf(dto.getBalance()));
        }

        user.setActive(dto.isActive());

        return user;
    }
}
