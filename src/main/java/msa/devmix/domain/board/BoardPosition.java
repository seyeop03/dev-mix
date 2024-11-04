package msa.devmix.domain.board;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.common.Position;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
public class BoardPosition {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_position_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    @Setter
    private Long requiredCount; //모집 인원
    @Setter
    private Long currentCount; //현재 인원

    @Builder
    public BoardPosition(Board board, Position position, Long requiredCount, Long currentCount) {
        this.board = board;
        this.position = position;
        this.requiredCount = requiredCount;
        this.currentCount = currentCount;
    }

    public void update(Position position, Long requiredCount) {
        this.position = position;
        this.requiredCount = requiredCount;
    }

    public static BoardPosition of(Board board, Position position, Long requiredCount, Long currentCount) {
        return BoardPosition.builder()
                .board(board)
                .position(position)
                .requiredCount(requiredCount)
                .currentCount(currentCount)
                .build();
    }

    public boolean isPositionAvailable() {
        return !currentCount.equals(requiredCount);
    }

}
