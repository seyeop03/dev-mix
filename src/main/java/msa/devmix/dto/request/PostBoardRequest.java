package msa.devmix.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import msa.devmix.domain.constant.Location;
import msa.devmix.dto.BoardDto;
import msa.devmix.dto.UserDto;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class PostBoardRequest {

    // 제목, 내용, 대표이미지, 모집상태, 포지션리스트, 지역, 프로젝트 진행기간, 기술스택


    @NotBlank(message = "제목을 입력해주세요.")
    @Length(max = 50)
    private String title; //게시글 제목

    @NotBlank(message = "내용을 입력해주세요.")
    @Length(max = 1500)
    private String content; //게시글 내용

    private String imageUrl; //게시글 대표 이미지 URL

    @NotNull
    private Long projectPeriod; //프로젝트 진행기간

    private String location;

    @NotNull
//    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate startDate; //프로젝트 시작일

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDate recruitEndDate; //모집 마감일


    private List<BoardTechStackRequest> boardTechStackList;
//    private List<BoardPositionRequest> boardPositionList;
    private List<@Valid BoardPositionRequest> boardPositionList;


//    public BoardDto toDto() {
//        return BoardDto.of(
//                title,
//                content,
//                location,
//                imageUrl,
//                projectPeriod,
//                startDate,
//                recruitEndDate
//                );
//    }

    public BoardDto toDto(UserDto userDto) {
        return BoardDto.of(
                title,
                content,
                userDto,
                Location.valueOf(location),
                imageUrl,
                projectPeriod,
                startDate,
                recruitEndDate
        );
    }
}
