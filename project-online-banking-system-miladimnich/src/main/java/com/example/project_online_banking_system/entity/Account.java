package com.example.project_online_banking_system.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String accountNumber;

  @Column(nullable = false)
  private double balance;

  @Column(nullable = false)
  private String accountHolderName;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  private MyUser user;

  public Account() {
  }

  public Account(String accountHolderName, String accountNumber, double balance, Long id,
      MyUser user) {
    this.accountHolderName = accountHolderName;
    this.accountNumber = accountNumber;
    this.balance = balance;
    this.id = id;
    this.user = user;
  }

  public String getAccountHolderName() {
    return accountHolderName;
  }

  public void setAccountHolderName(String accountHolderName) {
    this.accountHolderName = accountHolderName;
  }

  public String getAccountNumber() {
    return accountNumber;
  }

  public void setAccountNumber(String accountNumber) {
    this.accountNumber = accountNumber;
  }

  public double getBalance() {
    return balance;
  }

  public void setBalance(double balance) {
    this.balance = balance;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public MyUser getUser() {
    return user;
  }

  public void setUser(MyUser user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Account{" +
        "accountHolderName='" + accountHolderName + '\'' +
        ", id=" + id +
        ", accountNumber='" + accountNumber + '\'' +
        ", balance=" + balance +
        ", user=" + user +
        '}';
  }
}
