package msa.devmix.domain.board;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.common.BaseTimeEntity;
import msa.devmix.domain.user.User;


@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Comment extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    private String content; //댓글 내용

    private Comment(Board board, User user, String content) {
        this.board = board;
        this.user = user;
        this.content = content;
    }

    public static Comment of(Board board, User user, String content) {
        return new Comment(board, user, content);
    }
}
