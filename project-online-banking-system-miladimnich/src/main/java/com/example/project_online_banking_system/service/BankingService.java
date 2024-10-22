package com.example.project_online_banking_system.service;

import com.example.project_online_banking_system.entity.Account;
import com.example.project_online_banking_system.entity.MyUser;
import com.example.project_online_banking_system.repository.AccountRepository;
import com.example.project_online_banking_system.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankingService {

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private UserRepository userRepository;

  @Transactional
  public Account openNewAccount(MyUser user, String accountHolderName, double initialDeposit) {
    String accountNumber = generateAccountNumber();
    Account account = new Account();
    account.setAccountHolderName(accountHolderName);
    account.setBalance(initialDeposit);
    account.setAccountNumber(accountNumber);
    account.setUser(user);
    return accountRepository.save(account);
  }

  private String generateAccountNumber() {
    Random random = new Random();
    String accountNumber;

    do {
      accountNumber = String.format("%012d", random.nextInt(1000000000));
    } while (accountRepository.existsByAccountNumber(
        accountNumber));

    return accountNumber;

  }

  @Transactional
  public void deposit(String accountNumber, double amount) throws Exception {
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new RuntimeException("Account not found"));

    if (amount <= 0) {
      throw new RuntimeException("Deposit amount must be positive");
    }

    account.setBalance(account.getBalance() + amount);
    accountRepository.save(account);
  }


  @Transactional
  public void withdraw(String username,String accountNumber, double amount) throws Exception {
    if (amount <= 0) {
      throw new RuntimeException("Amount must be greater than zero");
    }
    Account account = accountRepository.findByAccountNumber(accountNumber)
        .orElseThrow(() -> new Exception("Account not found"));
    MyUser user = userRepository.findByUsername(username)
        .orElseThrow(() -> new Exception("User not found"));

    if (!account.getUser().equals(user)) {
      throw new Exception("You do not have permission to access this account.");
    }
    if (account.getBalance() < amount) {
      throw new Exception("Insufficient funds");
    }

    account.setBalance(account.getBalance() - amount);

    accountRepository.save(account);
  }


  @Transactional
  public void transfer(String fromAccountNumber, String toAccountNumber, double amount, String username) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be greater than zero.");
    }

    Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber)
        .orElseThrow(() -> new IllegalArgumentException("From account does not exist"));

    Account toAccount = accountRepository.findByAccountNumber(toAccountNumber)
        .orElseThrow(() -> new IllegalArgumentException("To account does not exist"));

    MyUser user = userRepository.findByUsername(username)
        .orElseThrow(() -> new IllegalArgumentException("User not found"));

    if (!fromAccount.getUser().equals(user)) {
      throw new IllegalArgumentException("You do not have access to this account.");
    }

    if (fromAccount.getBalance() < amount) {
      throw new IllegalArgumentException("Insufficient funds.");
    }

    fromAccount.setBalance(fromAccount.getBalance() - amount);
    toAccount.setBalance(toAccount.getBalance() + amount);

    accountRepository.save(fromAccount);
    accountRepository.save(toAccount);
  }

}
