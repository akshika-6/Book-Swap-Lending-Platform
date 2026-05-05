package com.bookswap.platform.service.impl;

import com.bookswap.platform.dto.RegisterRequest;
import com.bookswap.platform.exception.BusinessException;
import com.bookswap.platform.exception.ResourceNotFoundException;
import com.bookswap.platform.model.AppUser;
import com.bookswap.platform.model.Role;
import com.bookswap.platform.repository.UserRepository;
import com.bookswap.platform.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public AppUser register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessException("Email is already in use");
        }

        AppUser user = AppUser.builder()
                .email(request.email().toLowerCase())
                .password(passwordEncoder.encode(request.password()))
                .fullName(request.fullName())
                .role(Role.USER)
                .enabled(true)
                .build();

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public AppUser getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
