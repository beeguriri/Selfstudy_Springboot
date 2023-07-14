package study.advanced.trace;

import java.util.UUID;

public class TraceId {

    private final String id;
    private final int level;

    public TraceId() {
        this.id = createId();
        this.level = 0;
    }

    private TraceId(String id, int level) {
        this.id = id;
        this.level = level;
    }

    private String createId() {
        //앞 8자리만 사용
        return UUID.randomUUID().toString().substring(0, 8);
    }

    //level 증가 위하여 만든 메서드
    public TraceId createNextId() {
        return new TraceId(id, level+1);
    }

    //level 감소 위하여 만든 메서드
    public TraceId createPrevId() {
        return new TraceId(id, level-1);
    }

    public boolean isFirstLevel() {
        return level == 0;
    }

    public String getId() {
        return id;
    }

    public int getLevel() {
        return level;
    }
}
