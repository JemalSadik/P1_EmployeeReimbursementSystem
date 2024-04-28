package com.revature.services;

import com.revature.daos.UserDAO;
import com.revature.models.CustomUserDetails;
import com.revature.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserDAO userDAO;

    @Autowired
    public CustomUserDetailsService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<SimpleGrantedAuthority> authorities = Stream.of(user.getRole())
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new CustomUserDetails(user.getUsername(), user.getPassword(), true, true, true, true, authorities);
    }
}
