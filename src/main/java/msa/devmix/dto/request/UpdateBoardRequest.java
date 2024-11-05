package msa.devmix.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.constant.Location;
import msa.devmix.domain.constant.RecruitmentStatus;
import msa.devmix.dto.BoardDto;
import msa.devmix.dto.UserDto;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class UpdateBoardRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 50)
    private String title; //게시글 제목

    @NotBlank(message = "내용을 입력해주세요.")
    @Length(max = 1500)
    private String content; //게시글 내용

    private String imageUrl; //게시글 이미지 URL

    @NotNull
    private Long projectPeriod; //프로젝트 진행기간
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate; //프로젝트 시작일
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate recruitEndDate; //모집 마감일

    @NotNull
    private String location;

    @NotBlank
    private String recruitmentStatus; //모집 상태 (RECRUITING, COMPLETED)

    @NotEmpty
    private ArrayList<@Valid BoardTechStackRequest> boardTechStackList;
    @NotEmpty
    private ArrayList<@Valid BoardPositionRequest> boardPositionList;

    public BoardDto toDto(UserDto userDto) {
        return BoardDto.of(
                title,
                content,
                userDto,
                Location.valueOf(location),
                imageUrl,
                projectPeriod,
                startDate,
                recruitEndDate,
                RecruitmentStatus.valueOf(recruitmentStatus));
    }
}
