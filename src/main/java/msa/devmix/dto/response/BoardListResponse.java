package msa.devmix.dto.response;


import lombok.Data;
import msa.devmix.domain.constant.Location;
import msa.devmix.repository.query.BoardPositionQueryDto;
import msa.devmix.repository.query.BoardQueryDto;
import msa.devmix.repository.query.BoardTechStackQueryDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class BoardListResponse {

    private Long boardId;
    private String title;
    private String createdBy;
    private Long viewCount;
    private Long commentCount;
    private LocalDate recruitEndDate;
    private String location;

    private List<BoardPositionListResponse> positions;
    private List<BoardTechStackListResponse> techStacks;

    private BoardListResponse(
            Long boardId,
            String title,
            String createdBy,
            Long viewCount,
            Long commentCount,
            LocalDate recruitEndDate,
            String location,
            List<BoardPositionListResponse> positions,
            List<BoardTechStackListResponse> techStacks) {
        this.boardId = boardId;
        this.title = title;
        this.createdBy = createdBy;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.recruitEndDate = recruitEndDate;
        this.location = location;
        this.positions = positions;
        this.techStacks = techStacks;
    }

    public static BoardListResponse of(
            Long boardId,
            String title,
            String createdBy,
            Long viewCount,
            Long commentCount,
            LocalDate recruitEndDate,
            String location,
            List<BoardPositionListResponse> positions,
            List<BoardTechStackListResponse> techStacks) {
        return new BoardListResponse(boardId, title, createdBy, viewCount, commentCount, recruitEndDate,location, positions, techStacks);
    }

    public static BoardListResponse from(BoardQueryDto boardQueryDto) {
        String location = boardQueryDto.getLocation().toString();
        return BoardListResponse.of(
                boardQueryDto.getBoardId(),
                boardQueryDto.getTitle(),
                boardQueryDto.getCreatedBy(),
                boardQueryDto.getViewCount(),
                boardQueryDto.getCommentCount(),
                boardQueryDto.getRecruitEndDate(),
                Location.getLocation(location),
                BoardPositionListResponse.from(boardQueryDto.getPositions()),
                BoardTechStackListResponse.from(boardQueryDto.getTechStacks()));
    }
}
