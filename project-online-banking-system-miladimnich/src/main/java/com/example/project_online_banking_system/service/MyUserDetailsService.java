package com.example.project_online_banking_system.service;

import com.example.project_online_banking_system.entity.MyUser;
import com.example.project_online_banking_system.repository.UserRepository;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

  @Autowired
  private UserRepository repository;


  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<MyUser> user = repository.findByUsername(username);
    if(user.isPresent()){
      var userObj = user.get();
      return User.builder()
          .username(userObj.getUsername())
          .password(userObj.getPassword())
          .roles(getRoles(userObj))
          .build();
    }else {
      throw new UsernameNotFoundException(username);
    }
  }

  private String[] getRoles(MyUser user) {
    if(user.getRole()==null){
      return new String[]{"USER"};
    }
    return user.getRole().split(",");
  }

}