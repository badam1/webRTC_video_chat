package com.bodansky.videochat.service;

import com.bodansky.videochat.domain.ChatUser;
import com.bodansky.videochat.repository.ChatUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private final ChatUserRepository chatUserRepository;

    @Autowired
    public UserDetailsServiceImpl(ChatUserRepository chatUserRepository) {
        this.chatUserRepository = chatUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ChatUser user = chatUserRepository.findByEmail(email);
        log.info("loadUserByUsername() {}",user);

        if (user == null) {
            throw new UsernameNotFoundException("No user found with username " + email);
        }

        Set<GrantedAuthority> authorities = new HashSet<>();
        SimpleGrantedAuthority authority = new
                SimpleGrantedAuthority(user.getRole().name());
        authorities.add(authority);
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}