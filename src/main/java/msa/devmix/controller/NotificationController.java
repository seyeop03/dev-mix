package msa.devmix.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.config.oauth.userinfo.UserPrincipal;
import msa.devmix.domain.constant.NotificationType;
import msa.devmix.dto.response.ResponseDto;
import msa.devmix.repository.UserRepository;
import msa.devmix.service.NotificationService;
import msa.devmix.service.UserService;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/v1/notifications")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;


    @GetMapping(value = "/connect", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> connect(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok()
                .body(notificationService.connect(userPrincipal.getUser()));
    }

    @PatchMapping("{notification-id}/read")
    public ResponseEntity<?> patchNotification(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable("notification-id") Long notificationId) {
        notificationService.patchNotification(userPrincipal.getUser(), notificationId);

        return ResponseEntity.ok().body(ResponseDto.success());
    }
}
