package msa.devmix.domain.board;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.common.TechStack;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class BoardTechStack {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_tech_stack_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tech_stack_id")
    private TechStack techStack;

    @Builder
    public BoardTechStack(Board board, TechStack techStack) {
        this.board = board;
        this.techStack = techStack;
    }

    public static BoardTechStack of(Board board, TechStack techStack) {
        return BoardTechStack.builder()
                .board(board)
                .techStack(techStack)
                .build();
    }

}
