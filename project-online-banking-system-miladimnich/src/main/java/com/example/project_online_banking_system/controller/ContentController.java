package com.example.project_online_banking_system.controller;

import com.example.project_online_banking_system.entity.MyUser;
import com.example.project_online_banking_system.repository.UserRepository;
import com.example.project_online_banking_system.service.BankingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContentController {
  @Autowired
  private UserRepository myUserRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private BankingService bankingService;

  private String getCurrentUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (authentication != null && authentication.isAuthenticated()) ? authentication.getName() : null;
  }

  @GetMapping("/home")
  public String homePage(){
    return "home";
  }
  @GetMapping("/account/new")
  public String redirectToNewAccount() {
    return "new-account";
  }
  @GetMapping("/register/user")
  public String showRegistrationForm(Model model) {
    model.addAttribute("message", "Please fill in the form to register.");
    return "register";
  }

  @PostMapping("/register/user")
  public String createUser(
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("confirmPassword") String confirmPassword,
      Model model) {

    if (!password.equals(confirmPassword)) {
      model.addAttribute("message", "Passwords do not match!");
      return "register";
    }

    if (myUserRepository.findByUsername(username).isPresent()) {
      model.addAttribute("message", "Username already exists!");
      return "register";
    }
    try {
      MyUser user = new MyUser();
      user.setUsername(username);
      user.setPassword(passwordEncoder.encode(password)); // Encode password
      user.setRole("USER"); // Set default role

      myUserRepository.save(user); // Save user to database
    } catch (Exception e) {
      model.addAttribute("message", "Error creating user: " + e.getMessage());
      return "register";
    }
    return "redirect:/login";
  }


  @PostMapping("/account/new")
  public String handleAccountForm(
      @RequestParam("accountType") String accountType,
      @RequestParam("initialDeposit") double initialDeposit,
      Model model) {
    try {
      if (initialDeposit < 0) {
        model.addAttribute("message", "Initial deposit cannot be negative.");
        return "new-account";
      }
      String currentUsername = getCurrentUsername();

      MyUser user = myUserRepository.findByUsername(currentUsername)
          .orElseThrow(() -> new RuntimeException("User not found"));
      bankingService.openNewAccount(user, accountType, initialDeposit);
      return "redirect:/home";

    } catch (Exception e) {
      model.addAttribute("message", "Error: " + e.getMessage());
      return "new-account";  // Redirect back to the form in case of an error
    }
  }




  @GetMapping("/login")
  public String login(HttpServletRequest request, Model model) {
    String error = (String) request.getSession().getAttribute("error");
    if (error != null) {
      model.addAttribute("error", error);
      request.getSession().removeAttribute("error");
    }
    return "login";
  }


  @GetMapping("/transactions/deposit")
  public String deposit() {
    return "deposit";
  }

  @PostMapping("/transactions/deposit")
  public String depositMoney(
      @RequestParam("accountNumber") String accountNumber,
      @RequestParam("amount") double amount,
      RedirectAttributes redirectAttributes) {

    try {
      bankingService.deposit(accountNumber, amount);
      redirectAttributes.addFlashAttribute("message", "Deposit successful!");

    } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());

    }

    return "redirect:/home";
  }

  @GetMapping("/transactions/withdraw")
  public String withdrawMoney() {
    return "withdraw";
  }


  @PostMapping("/transactions/withdraw")
  public String withdrawMoney(
      @RequestParam("accountNumber") String accountNumber,
      @RequestParam("amount") double amount,
      RedirectAttributes redirectAttributes) {

    String currentUsername = getCurrentUsername();

    if (amount <= 0) {
      redirectAttributes.addFlashAttribute("error", "Error: Amount must be greater than zero.");
      return "redirect:/home";
    }

    try {
      bankingService.withdraw(currentUsername, accountNumber, amount);

      redirectAttributes.addFlashAttribute("message", "Withdrawal successful!");
     } catch (Exception e) {
      redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
    }

    return "redirect:/home";
  }

  @GetMapping("/transactions/transfer")
  public String transferMoney() {
    return "transfer";
  }


  @PostMapping("/transactions/transfer")
  public String transferMoney(
      @RequestParam("fromAccount") String fromAccountNumber,
      @RequestParam("toAccount") String toAccountNumber,
      @RequestParam("amount") double amount,
      RedirectAttributes redirectAttributes) {

    String currentUsername = getCurrentUsername();
    if (currentUsername == null) {
      redirectAttributes.addFlashAttribute("error", "User is not authenticated.");
      return "redirect:/transactions/transfer";
    }

    if (amount <= 0) {
      redirectAttributes.addFlashAttribute("error", "Amount must be greater than zero.");
      return "redirect:/transactions/transfer";
    }

    try {
      bankingService.transfer(fromAccountNumber, toAccountNumber, amount, currentUsername);
      redirectAttributes.addFlashAttribute("message", "Transfer successful!");
    } catch (IllegalArgumentException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
    }

    return "redirect:/home";
  }


}























