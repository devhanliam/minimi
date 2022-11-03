package com.minimi.user.service;

import com.minimi.domain.user.entity.User;
import com.minimi.domain.user.exception.NotFoundUserException;
import com.minimi.domain.user.exception.NotMatchPasswordException;
import com.minimi.domain.user.repostory.UserRepository;
import com.minimi.domain.user.request.JoinForm;
import com.minimi.user.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username){
        return userRepository.findByEmail(username).orElseThrow(() -> new NotFoundUserException());
    }


}
