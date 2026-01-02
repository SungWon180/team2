package com.team2.dbbuddy.controller;

import com.team2.dbbuddy.dto.UserDTO;
import com.team2.dbbuddy.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginForm() {
        return "user/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        UserDTO user = userService.login(email, password);
        if (user != null) {
            session.setAttribute("loginUser", user);
            return "redirect:/samples"; // Redirect to main page (currently samples list)
        } else {
            model.addAttribute("error", "아이디 또는 비밀번호를 확인해주세요");
            return "user/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/user/login";
    }

    @GetMapping("/signup")
    public String signupForm() {
        return "user/signup";
    }

    @PostMapping("/signup")
    public String signup(UserDTO userDTO, Model model, HttpSession session) {
        try {
            userService.register(userDTO);

            // Auto-login
            UserDTO loginUser = userService.login(userDTO.getEmail(), userDTO.getPasswd());
            if (loginUser != null) {
                session.setAttribute("loginUser", loginUser);
                return "redirect:/samples";
            }

            return "redirect:/user/login"; // Fallback
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "user/signup";
        }
    }
}
