package msa.devmix.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.dto.response.ResponseDto;
import msa.devmix.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardV1Controller {

    private final BoardService boardService;

    // 특정 페이지 게시글 리스트 조회(Test)
    @GetMapping
    public ResponseEntity<?> boards(@PageableDefault(size = 16) Pageable pageable) {
        return ResponseEntity.ok().body(ResponseDto.success(boardService.findAllBoards(pageable)));
    }
}
