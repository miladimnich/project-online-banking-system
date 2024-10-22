package com.example.project_online_banking_system.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;


@Entity
public class MyUser {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(nullable = false, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String role;


  @Column(name = "account_non_locked")
  private boolean accountNonLocked;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
  private List<Account> accounts;

  public MyUser() {
  }

  public MyUser(boolean accountNonLocked, List<Account> accounts, Long id, String password,
      String role, String username) {
    this.accountNonLocked = accountNonLocked;
    this.accounts = accounts;
    this.id = id;
    this.password = password;
    this.role = role;
    this.username = username;
  }

  public boolean isAccountNonLocked() {
    return accountNonLocked;
  }

  public void setAccountNonLocked(boolean accountNonLocked) {
    this.accountNonLocked = accountNonLocked;
  }

  public List<Account> getAccounts() {
    return accounts;
  }

  public void setAccounts(
      List<Account> accounts) {
    this.accounts = accounts;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String toString() {
    return "MyUser{" +
        "accountNonLocked=" + accountNonLocked +
        ", id=" + id +
        ", username='" + username + '\'' +
        ", password='" + password + '\'' +
        ", role='" + role + '\'' +
        ", accounts=" + accounts +
        '}';
  }
}

