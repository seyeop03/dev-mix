package msa.devmix.repository;

import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> sseEmitters = new ConcurrentHashMap<>();

    //SSE 이벤트 전송 객체를 저장
    public SseEmitter save(String username, SseEmitter sseEmitter) {
        sseEmitters.put(username, sseEmitter);
        return sseEmitter;
    }

    //해당 회원으로 SseEmitter 인스턴스 조회
    public SseEmitter findByUsername(String username) {
        return sseEmitters.get(username);
    }

    //해당 회원에 대한 SseEmitter 인스턴스 제거
    public void deleteByUsername(String username) {
        sseEmitters.remove(username);
    }
}
