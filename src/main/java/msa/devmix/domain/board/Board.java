package msa.devmix.domain.board;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.common.BaseEntity;
import msa.devmix.domain.constant.Location;
import msa.devmix.domain.constant.RecruitmentStatus;
import msa.devmix.domain.user.User;
import msa.devmix.dto.BoardDto;

import java.time.LocalDate;


@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class Board extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @Setter
    private User user;

    private String title; //게시글 제목
    private String content; //게시글 내용
    private String imageUrl;//게시글 이미지 URL

    private Location location;

    private Long projectPeriod; //프로젝트 진행기간
    private LocalDate startDate; //프로젝트 시작일
    private LocalDate recruitEndDate; //모집 마감일

    @Enumerated(EnumType.STRING)
    @Setter
    private RecruitmentStatus recruitmentStatus; //모집 현황 (RECRUITING, COMPLETED)

    @Setter
    private Long viewCount; //조회수
    @Setter
    private Long commentCount; //댓글 갯수


    public static Board of(
                 String title,
                 String content,
                 String imageUrl,
                 Location location,
                 Long projectPeriod,
                 LocalDate startDate,
                 LocalDate recruitEndDate
    ) {
        return new Board(
                null,
                null,
                title,
                content,
                imageUrl,
                location,
                projectPeriod,
                startDate,
                recruitEndDate,
                null,
                null,
                null
        );
    }

    public static Board of(String title,
                           String content,
                           String imageUrl,
                           Location location,
                           Long projectPeriod,
                           LocalDate startDate,
                           LocalDate recruitEndDate,
                           RecruitmentStatus recruitmentStatus) {
        return new Board(
                null,
                null,
                title,
                content,
                imageUrl,
                location,
                projectPeriod,
                startDate,
                recruitEndDate,
                recruitmentStatus,
                null,
                null);

    }


    public void update(BoardDto boardDto) {
             this.title = boardDto.getTitle();
             this.content = boardDto.getContent();
             this.imageUrl = boardDto.getImageUrl();
             this.location = boardDto.getLocation();
             this.projectPeriod = boardDto.getProjectPeriod();
             this.startDate = boardDto.getStartDate();
             this.recruitEndDate = boardDto.getRecruitEndDate();
             this.recruitmentStatus = boardDto.getRecruitmentStatus();
    }

    public void increaseViewCount() {
        this.viewCount++;
    }

    public void increaseCommentCount() {
        this.commentCount++;
    }
}
