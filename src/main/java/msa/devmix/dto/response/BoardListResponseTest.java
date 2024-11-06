package msa.devmix.dto.response;

import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Data
public class BoardListResponseTest {

    private Long boardId;
    private String title;
    private String createdBy;
    private Long viewCount;
    private Long commentCount;
    private LocalDate recruitEndDate;

    @Setter
    private List<BoardPositionListResponseTest> positions;
    private List<BoardTechStackListResponseTest> techStacks;

    public BoardListResponseTest(
            Long boardId,
            String title,
            String createdBy,
            Long viewCount,
            Long commentCount,
            LocalDate recruitEndDate,
            List<BoardPositionListResponseTest> positions,
            List<BoardTechStackListResponseTest> techStacks) {
        this.boardId = boardId;
        this.title = title;
        this.createdBy = createdBy;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.recruitEndDate = recruitEndDate;
        this.positions = positions;
        this.techStacks = techStacks;
    }



    public static BoardListResponseTest of(Long boardId,
                                           String title,
                                           String createdBy,
                                           Long viewCount,
                                           Long commentCount,
                                           LocalDate recruitEndDate,
                                           List<BoardPositionListResponseTest> positions,
                                           List<BoardTechStackListResponseTest> techStacks) {
        return new BoardListResponseTest(
                boardId,
                title,
                createdBy,
                viewCount,
                commentCount,
                recruitEndDate,
                positions,
                techStacks);
    }

    public static BoardListResponseTest of(Long boardId,
                                           String title,
                                           String createdBy,
                                           Long viewCount,
                                           Long commentCount,
                                           LocalDate recruitEndDate) {
        return new BoardListResponseTest(boardId, title, createdBy, viewCount, commentCount, recruitEndDate, null, null);
    }
}
