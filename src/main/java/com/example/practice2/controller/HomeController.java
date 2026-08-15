package com.example.practice2.controller;

import com.example.practice2.domain.Member;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpServletRequest req,Model model) {
        HttpSession session = req.getSession(false);

        if (session == null || session.getAttribute("loginUser") == null) {
            return "home";
        } else {
            Member loginUser = (Member)session.getAttribute("loginUser");
            model.addAttribute("loginUser",loginUser);
            return "mypage";
        }
    }
}///
