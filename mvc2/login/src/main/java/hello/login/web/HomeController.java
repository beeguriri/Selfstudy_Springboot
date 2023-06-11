package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

//    @GetMapping("/")
    public String home() {
        return "home";
    }

//    @GetMapping("/")
    //로그인 하지 않은 사용자도 들어와야 하므로 required = false
    public String homeLogin(@CookieValue(name="memberId", required = false) Long memberId,
                            Model model) {

        if(memberId == null)
            return "home";

        //로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null)
            return "home";

        model.addAttribute("member", loginMember);

        return "loginHome";

    }

    @GetMapping("/")
    //로그인 하지 않은 사용자도 들어와야 하므로 required = false
    public String homeLoginV2(HttpServletRequest request, Model model) {

        //세션 관리자에 저장 된 회원정보 조회
        Member member = (Member) sessionManager.getSession(request);

        //로그인
        if (member == null)
            return "home";

        model.addAttribute("member", member);

        return "loginHome";

    }
}