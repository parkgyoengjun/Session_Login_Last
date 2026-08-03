package com.example.practice2.controller;

import com.example.practice2.service.MemberService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MemberController {
    private final MemberService service;

    public MemberController(MemberService service) {
        this.service = service;
    }

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    public String save(@RequestParam(name = "savedId")String saveId, @RequestParam(name = "password") String password, @RequestParam(name = "nickname") String nickname) {
        if (saveId != null && password != null && nickname != null) {
            service.save(saveId, password, nickname);
            return "redirect:/login";
        } else {
            return "redirect:/save";
        }
    }

    @GetMapping("/mypage")
    public String mypageForm(HttpSession session, Model model) {
        Object loginUser = session.getAttribute("loginUser");
        if (loginUser != null) {
            model.addAttribute("loginUser", loginUser);
            return "/mypage";
        }
        return "redirect:/save";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}///
