package com.bank.loan.management.api.security;

import java.util.ArrayList;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class BasicAuthConfig {

  // User Creation
  @Bean
  public UserDetailsService userDetailsService(PasswordEncoder encoder) {

    List<GrantedAuthority> authoritiesAdmin = new ArrayList<>(2);
    authoritiesAdmin.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
    List<GrantedAuthority> authoritiesUser = new ArrayList<>(2);
    authoritiesAdmin.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
    UserDetails admin = new User("admin", encoder.encode("admin"), authoritiesAdmin);
    UserDetails user1 = new User("Tom", encoder.encode("123"), authoritiesUser);
    UserDetails user2 = new User("Jane", encoder.encode("123"), authoritiesUser);
    UserDetails user3 = new User("Michael", encoder.encode("123"), authoritiesUser);

    return new InMemoryUserDetailsManager(admin, user1, user2, user3);
  }

  // Configuring HttpSecurity
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable);
    http.httpBasic(Customizer.withDefaults());
    http.authorizeHttpRequests(auth -> auth.requestMatchers("/h2/**").permitAll());
    http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
    http.headers(headers -> headers.frameOptions(FrameOptionsConfig::disable));
    return http.build();

  }

  // Password Encoding
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
