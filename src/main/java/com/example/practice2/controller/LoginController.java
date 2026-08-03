package com.example.practice2.controller;

import com.example.practice2.domain.Member;
import com.example.practice2.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final MemberService service;

    public LoginController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam(name = "loginId") String loginId, @RequestParam(name = "password") String password,
                        HttpSession session, Model model) {
        if (loginId != null && password != null) {
            Member loginUser = service.login(loginId, password);
            if (loginUser != null) {
                session.setAttribute("loginUser", loginUser);
                System.out.println("로그인 성공");
                return "redirect:/mypage";
            }
        }
        System.out.println("로그인 실패");
        return "/login";

    }
}///
