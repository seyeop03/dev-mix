package msa.devmix.repository.query;

import lombok.Data;
import lombok.EqualsAndHashCode;
import msa.devmix.domain.constant.Location;

import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(of = "boardId")
public class BoardQueryDto {

    private Long boardId;
    private String title;
    private String createdBy;
    private Long viewCount;
    private Long commentCount;
    private LocalDate recruitEndDate;
    private Location location;

    private List<BoardPositionQueryDto> positions;
    private List<BoardTechStackQueryDto> techStacks;

    public BoardQueryDto(Long boardId, String title, String createdBy, Long viewCount, Long commentCount, LocalDate recruitEndDate, Location location) {
        this.boardId = boardId;
        this.title = title;
        this.createdBy = createdBy;
        this.viewCount = viewCount;
        this.commentCount = commentCount;
        this.recruitEndDate = recruitEndDate;
        this.location = location;
    }
}
