package com.minimi.domain.user.service;

import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.exception.DuplicatedEmailException;
import com.minimi.domain.user.exception.NotFoundUserException;
import com.minimi.domain.user.exception.NotMatchPasswordException;
import com.minimi.domain.user.repostory.UserRepository;
import com.minimi.domain.user.request.JoinForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;

    public boolean getDuplication(String email) {
        boolean result = userRepository.existsByEmail(email);
        if(result){
            throw new DuplicatedEmailException();
        }else{
            return true;
        }
    }
}
