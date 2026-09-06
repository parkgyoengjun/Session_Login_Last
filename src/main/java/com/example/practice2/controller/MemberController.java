package com.example.practice2.controller;

import com.example.practice2.annotation.LoginUser;
import com.example.practice2.domain.Member;
import com.example.practice2.security.PrincipalDetails;
import com.example.practice2.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

//     # Resolver 추가 전
//    @GetMapping("/mypage")
//    public String mypageForm(HttpSession session, Model model) {
//        Object loginUser = session.getAttribute("loginUser");
//        if (loginUser != null) {
//            model.addAttribute("loginUser", loginUser);
//            return "/mypage";
//        }
//        return "redirect:/save";
//    }

    // # Resolvers 추가 후
    // HttpSession, getAttribute 캐스팅 코드가 사라짐
    // 파라미터에 @LoginUser Member loginUser 선언하면 스프링이 자동으로 값을 채워줌
    @GetMapping("/mypage")
//    public String mypageForm(@LoginUser Member loginUser, Model model) {
    public String mypageForm(@AuthenticationPrincipal PrincipalDetails principalDetails, Model model) {
        model.addAttribute("loginUser", principalDetails.getMember());
        return "mypage";
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}///
