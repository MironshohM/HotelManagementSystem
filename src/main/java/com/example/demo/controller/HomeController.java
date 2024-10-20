package com.example.demo.controller;


import com.example.demo.entity.User;
import com.example.demo.repo.UserRepository;
import com.example.demo.securityService.MailService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/hotel")
public class HomeController {
    @Autowired
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping({"/", "home"})
    public String home() {
        return "home"; // Thymeleaf template for homepage
    }


    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String processRegistration(@Valid @ModelAttribute("user") User user,
                                      BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.toString()));
            System.out.println("Error is here");
            return "register";
        }

        // Check if user already exists (by email)
        if (userRepository.findUserByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "This email is already registered.");
            return "register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        try {
            // Send verification email
            userRepository.save(user);
            mailService.sendVerificationEmail(user.getEmail(), user.getUsername());
            model.addAttribute("emailSent", "A verification code has been sent to your email.");
            model.addAttribute("unverifiedUser", user); // Keep track of the unverified user
            return "verify-email"; // Redirect to the email verification page
        } catch (MessagingException e) {
            model.addAttribute("emailError", "Failed to send verification email. Please try again.");
            return "register";
        }
    }

    @PostMapping("/verify-email")
    public String verifyEmail(@RequestParam("email") String email,
                              @RequestParam("verificationCode") String code,
                              @ModelAttribute("unverifiedUser") User user, Model model) {

        if (mailService.verifyCode(email, code)) {
            // Update user's verified status and save to DB
            Optional<User> userByEmailOpt = userRepository.findUserByEmail(email);
            User verifiedUser = userByEmailOpt.get();
            verifiedUser.setVerified(true);

            userRepository.save(verifiedUser);


            return "redirect:/login";
        } else {
            model.addAttribute("verificationError", "Invalid verification code. Please try again.");

            return "verify-email";
        }
    }
}
