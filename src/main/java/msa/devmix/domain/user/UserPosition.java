package msa.devmix.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.common.Position;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class UserPosition {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    private UserPosition(User user, Position position) {
        this.user = user;
        this.position = position;
    }

    public static UserPosition of(User user, Position position) {
        return new UserPosition(user, position);
    }
}
