package msa.devmix.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.constant.NotificationType;
import msa.devmix.domain.notification.Notification;

@Getter
@AllArgsConstructor
public class NotificationDto {

    private Long id;
    private String content;
    private boolean isRead;
    private NotificationType notificationType;

    public static NotificationDto from(Notification notification) {
        return new NotificationDto(
                notification.getId(),
                notification.getContent(),
                notification.isRead(),
                notification.getNotificationType()
        );
    }
}
