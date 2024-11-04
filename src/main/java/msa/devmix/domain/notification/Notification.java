package msa.devmix.domain.notification;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.constant.NotificationType;
import msa.devmix.domain.user.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    private String content; //알림 내용

    @Column(nullable = false)
    @Setter
    private boolean isRead;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificationType notificationType;


    @Builder
    public Notification(User user, NotificationType notificationType, String content, boolean isRead) {
        this.user = user;
        this.notificationType = notificationType;
        this.content = content;
        this.isRead = isRead;
    }

    public static Notification createNotification(User user, NotificationType notificationType, String content) {
        return Notification.builder()
                .user(user)
                .notificationType(notificationType)
                .content(content)
                .isRead(false)
                .build();
    }
}
