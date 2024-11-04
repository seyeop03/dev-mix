package msa.devmix.domain.board;

import com.nimbusds.oauth2.sdk.dpop.DPoPUtils;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.constant.ParticipationStatus;
import msa.devmix.domain.user.User;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class Apply {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "apply_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_position_id")
    private BoardPosition boardPosition;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus participationStatus; //참여 여부 (YES, NO, UNKNOWN)
    private String note; //지원 사유 및 한마디

    private Apply(User user, BoardPosition boardPosition, ParticipationStatus participationStatus, String note) {
        this.user = user;
        this.boardPosition = boardPosition;
        this.participationStatus = participationStatus;
        this.note = note;
    }

    public static Apply of(User user, BoardPosition boardPosition, ParticipationStatus participationStatus, String note) {
        return new Apply(user, boardPosition, participationStatus, note);
    }
}
