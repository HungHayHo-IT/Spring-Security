package com.example.spring_security.config;

import com.example.spring_security.model.Customer;
import com.example.spring_security.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EazyBankUserDetailsService implements UserDetailsService { // Thêm implements UserDetailsService

    // Không cần @Autowired vì đã có @RequiredArgsConstructor của Lombok
    private final CustomerRepository customerRepository;


    @Override // Khai báo chuẩn phương thức của interface
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException { // Viết thường chữ 'l' và đổi throws
        Customer customer = customerRepository.findByEmail(username).orElseThrow(
                ()->new UsernameNotFoundException("User not found: " + username)
        );
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(customer.getRole()));
        return new User(customer.getEmail() , customer.getPwd() , authorities);
    }
}