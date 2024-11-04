package msa.devmix.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.constant.Location;
import msa.devmix.domain.constant.RecruitmentStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class BoardDto {

    private Long boardId;
    private String title;
    private String content;

    @Setter
    private UserDto userDto;

    private Location location;
    private String imageUrl;
    private Long viewCount; //조회수
    private Long projectPeriod; //프로젝트 진행기간
    private LocalDate startDate; //프로젝트 시작일
    private LocalDate recruitEndDate;
    private RecruitmentStatus recruitmentStatus;
    private LocalDateTime createdAt; //게시글 생성일자
    private LocalDateTime lastModifiedAt; //게시글 수정일자

    public static BoardDto of(
            String title,
            String content,
            UserDto userDto,
            Location location,
            String imageUrl,
            Long projectPeriod,
            LocalDate startDate,
            LocalDate recruitEndDate,
            RecruitmentStatus recruitmentStatus) {
        return new BoardDto(
                null,
                title,
                content,
                userDto,
                location,
                imageUrl,
                null,
                projectPeriod,
                startDate,
                recruitEndDate,
                recruitmentStatus,
                null,
                null);
    }

    public static BoardDto from(Board board) {
        return BoardDto.of(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                UserDto.from(board.getUser()),
                board.getLocation(),
                board.getImageUrl(),
                board.getRecruitmentStatus(),
                board.getViewCount(),
                board.getProjectPeriod(),
                board.getStartDate(),
                board.getRecruitEndDate(),
                board.getCreatedAt(),
                board.getLastModifiedAt()
        );
    }

    public static BoardDto of(Long boardId,
                              String title,
                              String content,
                              UserDto userDto,
                              Location location,
                              String imageUrl,
                              RecruitmentStatus recruitmentStatus,
                              Long viewCount,
                              Long projectPeriod,
                              LocalDate startDate,
                              LocalDate recruitEndDate,
                              LocalDateTime createdAt,
                              LocalDateTime lastModifiedAt) {
        return new BoardDto(
                boardId,
                title,
                content,
                userDto,
                location,
                imageUrl,
                viewCount,
                projectPeriod,
                startDate,
                recruitEndDate,
                recruitmentStatus,
                createdAt,
                lastModifiedAt);
    }

    public static BoardDto of(String title,
                              String content,
                              UserDto userDto,
                              Location location,
                              String imageUrl,
                              Long projectPeriod,
                              LocalDate startDate,
                              LocalDate recruitEndDate
                              ) {
        return new BoardDto(
                null,
                title,
                content,
                userDto,
                location,
                imageUrl,
                null,
                projectPeriod,
                startDate,
                recruitEndDate,
                null,
                null,
                null);
    }

    public Board toEntity() {
        return Board.of(
                title,
                content,
                imageUrl,
                location,
                projectPeriod,
                startDate,
                recruitEndDate);
    }


}
