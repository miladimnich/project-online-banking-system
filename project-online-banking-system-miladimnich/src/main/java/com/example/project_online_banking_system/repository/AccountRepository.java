package com.example.project_online_banking_system.repository;

import com.example.project_online_banking_system.entity.Account;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long> {
  boolean existsByAccountNumber(String accountNumber);
  Optional<Account> findByAccountNumber(String accountNumber);

}
