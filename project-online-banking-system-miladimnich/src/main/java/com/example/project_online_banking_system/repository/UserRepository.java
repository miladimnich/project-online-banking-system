package com.example.project_online_banking_system.repository;

 import com.example.project_online_banking_system.entity.MyUser;
 import java.util.Optional;
  import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {
  Optional<MyUser> findByUsername(String username);



}
