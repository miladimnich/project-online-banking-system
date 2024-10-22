package com.example.project_online_banking_system.security;

import com.example.project_online_banking_system.authentication.CustomAuthenticationFailureHandler;
import com.example.project_online_banking_system.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


  @Autowired
  private MyUserDetailsService myUserDetailsService;

  public SecurityConfig(MyUserDetailsService myUserDetailsService) {
    this.myUserDetailsService = myUserDetailsService;
  }


  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorizeRequests -> {
          authorizeRequests
              .requestMatchers("/home/**").permitAll()
              .requestMatchers("/account/new").authenticated()
              .requestMatchers("/login/**").permitAll()
              .requestMatchers("/register/user/**").permitAll()
              .requestMatchers("/transactions/**").authenticated()
              .anyRequest().authenticated();
        })
        .formLogin(formLogin ->
            formLogin
                .loginPage("/login")
                .permitAll()
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/home", true)
                .failureHandler(customAuthenticationFailureHandler())
        )
        .logout(logout ->
            logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll()
        );
    return httpSecurity.build();
  }

  @Bean
  public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
    return new CustomAuthenticationFailureHandler();
  }

  @Bean
  public UserDetailsService userDetailsService() {
    return myUserDetailsService;
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
    provider.setUserDetailsService(userDetailsService());
    provider.setPasswordEncoder(passwordEncoder());
    return provider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
