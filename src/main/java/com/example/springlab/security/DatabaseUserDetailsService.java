package com.example.springlab.security;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseUserDetailsService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        List<UserDetails> users = jdbcTemplate.query(
                """
                SELECT username, password, enabled
                FROM app_users
                WHERE username = ?
                """,
                (rs, rowNum) -> {
                    String dbUsername = rs.getString("username");
                    String password = rs.getString("password");
                    boolean enabled = rs.getBoolean("enabled");

                    List<GrantedAuthority> authorities = loadAuthorities(dbUsername);

                    return User.withUsername(dbUsername)
                            .password(password)
                            .disabled(!enabled)
                            .authorities(authorities)
                            .build();
                },
                username
        );

        if (users.isEmpty()) {
            throw new UsernameNotFoundException(
                    "User not found: " + username
            );
        }

        return users.getFirst();
    }

    private List<GrantedAuthority> loadAuthorities(String username) {
        return jdbcTemplate.query(
                """
                SELECT authority
                FROM app_user_authorities
                WHERE username = ?
                """,
                (rs, rowNum) ->
                        new SimpleGrantedAuthority(rs.getString("authority")),
                username
        );
    }
}