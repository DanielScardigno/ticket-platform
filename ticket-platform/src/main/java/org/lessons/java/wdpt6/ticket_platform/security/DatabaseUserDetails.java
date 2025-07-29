package org.lessons.java.wdpt6.ticket_platform.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.lessons.java.wdpt6.ticket_platform.Models.Role;
import org.lessons.java.wdpt6.ticket_platform.Models.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class DatabaseUserDetails implements UserDetails{

    private final Integer id;
    private final String userName;
    private final String password;
    private final Set<GrantedAuthority> authorities;

    public DatabaseUserDetails(User user) {

        this.id = user.getId();
        this.userName = user.getUserName();
        this.password = user.getPassword();

        this.authorities = new HashSet<GrantedAuthority>();

        for (Role role : user.getRoles()) {
            this.authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }

    public Integer getId() {
        return this.id;
    }
}