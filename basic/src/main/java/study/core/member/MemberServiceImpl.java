package study.core.member;

public class MemberServiceImpl implements MemberService{

    // MemberServiceImpl 은
    // MemberRepository에도 의존하고 (추상화에 의존)
    // MemoryMemberRepository에도 의존 함 (구체화에 의존)
    // DIP 위반
    private final MemberRepository memberRepository = new MemoryMemberRepository();

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }
}
