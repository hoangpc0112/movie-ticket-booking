package com.example.theater.services;

import com.example.theater.entities.AppUser;
import com.example.theater.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        AppUser appUser = appUserRepository.findByUsername(username);

        if (appUser != null) {
            return User.withUsername(appUser.getUsername()).password(appUser.getPassword()).build();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }
}
