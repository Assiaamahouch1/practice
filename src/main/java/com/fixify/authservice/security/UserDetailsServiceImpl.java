package com.fixify.authservice.security;

import com.fixify.authservice.model.User;
import com.fixify.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@Transactional
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmailOrPhone(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email or phone: " + username));
        
        return UserPrincipal.create(user);
    }
    
    public UserDetails loadUserById(Long id) throws UsernameNotFoundException {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
        
        return UserPrincipal.create(user);
    }
    
    public static class UserPrincipal implements UserDetails {
        private Long id;
        private String username;
        private String email;
        private String phone;
        private String password;
        private Collection<? extends GrantedAuthority> authorities;
        private boolean enabled;
        
        public UserPrincipal(Long id, String username, String email, String phone, String password, 
                            Collection<? extends GrantedAuthority> authorities, boolean enabled) {
            this.id = id;
            this.username = username;
            this.email = email;
            this.phone = phone;
            this.password = password;
            this.authorities = authorities;
            this.enabled = enabled;
        }
        
        public static UserPrincipal create(User user) {
            GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().name());
            
            return new UserPrincipal(
                    user.getId(),
                    user.getLoginIdentifier(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getPassword(),
                    Collections.singletonList(authority),
                    user.isEmailVerified() || user.isPhoneVerified()
            );
        }
        
        public Long getId() {
            return id;
        }
        
        public String getEmail() {
            return email;
        }
        
        public String getPhone() {
            return phone;
        }
        
        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }
        
        @Override
        public String getPassword() {
            return password;
        }
        
        @Override
        public String getUsername() {
            return username;
        }
        
        @Override
        public boolean isAccountNonExpired() {
            return true;
        }
        
        @Override
        public boolean isAccountNonLocked() {
            return true;
        }
        
        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }
        
        @Override
        public boolean isEnabled() {
            return enabled;
        }
    }
}