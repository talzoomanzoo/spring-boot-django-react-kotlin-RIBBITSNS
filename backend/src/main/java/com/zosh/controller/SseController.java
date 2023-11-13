package com.zosh.controller;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

// import com.zosh.service.SseService;

// import lombok.RequiredArgsConstructor;

// @RestController
// @RequestMapping("/notifications")
// @RequiredArgsConstructor
// public class SseController {
//     private final SseService sseService;

//     @GetMapping(value = "/subscribe/{id}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//     public SseEmitter subscribe(@PathVariable Long id) {
//         return sseService.subscribe(id);
//     }

//     @PostMapping("/send-data/{id}")
//     public void sendData(@PathVariable Long id) {
//         sseService.notify(id, "data");
//     }
// }

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/notifications")
public class SseController {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    @GetMapping("/subscribe/{userId}")
    public SseEmitter subscribe(@PathVariable Long userId) {
        SseEmitter emitter = new SseEmitter();
        emitters.put(userId, emitter);

        // 클라이언트 연결 종료 시 처리
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));

        return emitter;
    }

    @GetMapping("/notify/{userId}")
    public void notifyClients(@PathVariable Long userId, String message) {
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().data(message, MediaType.TEXT_PLAIN));
            } catch (IOException e) {
                // 에러 발생 시 연결 종료 처리
                emitters.remove(userId);
                emitter.completeWithError(e);
            }
        }
    }
}

