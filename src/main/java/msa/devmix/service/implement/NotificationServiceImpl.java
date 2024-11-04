package msa.devmix.service.implement;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.constant.NotificationType;
import msa.devmix.domain.notification.Notification;
import msa.devmix.domain.user.User;
import msa.devmix.dto.NotificationDto;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.EmitterRepository;
import msa.devmix.repository.NotificationRepository;
import msa.devmix.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * 1. 알림 이벤트 Listen
 * 2. 이벤트 발생시 Notification 을 DB 에 저장해둔다.
 * 3. 특정 클라이언트가 첫 connect 했을 때, 이전 이벤트를 모두 다 가져온다.
 * (하지만, 연결 끊기는게 잦다면, 다시 연결할 때마다 연결에 대한 비용 및 DB 조회에 대한 비용이 존재한다. 이 문제는 추후 해결)
 * 4.
 */

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60; //1 hour

    private final EmitterRepository emitterRepository;
    private final NotificationRepository notificationRepository;


    /**
     * 첫 연결 or 재 연결 구독 설정
     * 첫 연결시에는 읽지 않은 알림을 보내주어야 한다.
     * 그런데, 재연결일 때도 읽지 않은 알림들을 보내주어야할까?
     * 재연결에 대한 정보를 포함.
     */
    @Override
    public SseEmitter connect(User user) {

        SseEmitter oldSseEmitter = emitterRepository.findByUsername(user.getUsername());
        if (oldSseEmitter != null) {
            return oldSseEmitter;
        }

        SseEmitter sseEmitter = emitterRepository.save(user.getUsername(), new SseEmitter(DEFAULT_TIMEOUT));

        //Configure callbacks for a specific emitter
        sseEmitter.onCompletion(() -> {
            log.info("onCompletion callback");
            emitterRepository.deleteByUsername(user.getUsername());
        });
        sseEmitter.onTimeout(() -> {
            log.info("onTimeout callback");
            emitterRepository.deleteByUsername(user.getUsername());
        });

        //Send dummy event to prevent 503 errors
        try {
            sseEmitter.send(SseEmitter.event()
                    .name("connect") // 해당 이벤트의 이름 지정
                    .data("connected!")); // 503 에러 방지를 위한 더미 데이터
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //해당 회원에 대한 읽지 않은 알림 조회
        List<Notification> notifications = notificationRepository.findAllByUser_Username(user.getUsername())
                .stream()
                .filter(notification -> !notification.isRead())
                .collect(Collectors.toList());

        //읽지 않은 알림 전송
        if (!notifications.isEmpty()) {
            sendNotifications(user.getUsername(), sseEmitter, notifications);
        }

        return sseEmitter;
    }

    /**
     * 리스너를 통해 알림을 1개 생성하고 해당 알림을 수신하는 클라이언트에게 바로 전송
     */
    @Override
    public void send(User user, NotificationType notificationType, String content) {
        //알림 객체 생성
        Notification notification = notificationRepository.save(Notification.createNotification(user, notificationType, content));

        String username = user.getUsername();

        //특정 유저가 연결돼있는지 확인 후 존재한다면 알림 전송
        SseEmitter sseEmitter = emitterRepository.findByUsername(username);
        if (sseEmitter != null) {
            log.info("send notification to username {}", username);
            sendNotification(username, sseEmitter, notification);
        }
    }

    //해당 SseEmitter 를 통해 이벤트 1개를 실제로 전송
    private void sendNotification(String username, SseEmitter emitter, Notification data) {
        try {
            emitter.send(SseEmitter.event() //SseEmitter.event() 호출하여 SseEventBuilder 객체 생성
                    .name("sse")
                    .data(NotificationDto.from(data)));
        } catch (IOException exception) {
            emitterRepository.deleteByUsername(username);
        }
    }

    //해당 SseEmitter 를 통해 N개의 이벤트를 실제로 전송
    private void sendNotifications(String username, SseEmitter emitter, List<Notification> data) {
        try {
            emitter.send(SseEmitter.event() //SseEmitter.event() 호출하여 SseEventBuilder 객체 생성
                    .name("sse")
                    .data(data.stream()
                            .map(NotificationDto::from)
                            .collect(Collectors.toList())
                    )
            );
        } catch (IOException exception) {
            emitterRepository.deleteByUsername(username);
        }
    }

    public void patchNotification(User user, Long notificationId) {
        Notification notification = notificationRepository.findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.PERMISSION_DENIED));
        notification.setRead(true);
    }
}
