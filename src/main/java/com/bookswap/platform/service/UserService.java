package com.bookswap.platform.service;

import com.bookswap.platform.dto.RegisterRequest;
import com.bookswap.platform.model.AppUser;

public interface UserService {
    AppUser register(RegisterRequest request);

    AppUser getUserById(Long id);

    AppUser getUserByEmail(String email);
}
