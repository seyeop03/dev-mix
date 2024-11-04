package msa.devmix.repository;

import msa.devmix.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser_Username(String username);
    Optional<Notification> findByIdAndUserId(Long id, Long userId);
}
