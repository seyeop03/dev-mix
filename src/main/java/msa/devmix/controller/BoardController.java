package msa.devmix.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.config.oauth.userinfo.UserPrincipal;
import msa.devmix.domain.board.Board;
import msa.devmix.dto.*;
import msa.devmix.dto.request.*;
import msa.devmix.dto.response.BoardWithPositionTechStackResponse;
import msa.devmix.dto.response.CommentResponse;
import msa.devmix.dto.response.ResponseDto;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.BoardPositionRepository;
import msa.devmix.repository.BoardRepository;
import msa.devmix.service.ApplyService;
import msa.devmix.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;



@RestController
@RequestMapping("/api/v1/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final ApplyService applyService;
    private final BoardPositionRepository boardPositionRepository;

    /**
     * 게시글 기능
     */
    //게시물 상세 조회
    @GetMapping("/{board-id}")
    public ResponseEntity<?> board(@PathVariable("board-id") @Min(1) Long boardId) {

        // 제목, 내용, 게시글 번호(ID), 모집상태, 조회수, 북마크 해간놈 list, 진행방식(지역), 기술스택 dto, 진행기간, 시작일, 모집마감일, 포지션 dto, 작성자정보 dto, BaseTimeEntity, 댓글(따로)
        return ResponseEntity.ok()
                .body(ResponseDto.success(BoardWithPositionTechStackResponse.from(boardService.getBoard(boardId))));
    }

    //특정 페이지 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<?> boards(@PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        boardService.getBoards(pageable);
        return null;
    }

    //게시글 생성
    @PostMapping
    public ResponseEntity<?> postBoard(@Valid @RequestBody PostBoardRequest postBoardRequest,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal) {

        BoardDto boardDto = postBoardRequest.toDto(UserDto.from(userPrincipal.getUser()));

        List<BoardPositionDto> boardPositionDtos = postBoardRequest.getBoardPositionList()
                .stream()
                .map(BoardPositionRequest::toDto)
                .toList();

        List<BoardTechStackDto> boardTechStackDtos = postBoardRequest.getBoardTechStackList()
                .stream()
                .map(BoardTechStackRequest::toDto)
                .toList();

        boardService.saveBoard(boardDto, boardPositionDtos, boardTechStackDtos);

        return ResponseEntity.ok().body(ResponseDto.success());
    }

    //게시글 수정
    @PutMapping("/{board-id}")
    public ResponseEntity<?> updateBoard(@PathVariable("board-id") @Min(1) Long boardId,
                                         @Valid @RequestBody UpdateBoardRequest updateBoardRequest,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        if (Objects.equals(board.getUser().getId(), userPrincipal.getUser().getId())) {
            BoardDto boardDto = updateBoardRequest.toDto(UserDto.from(userPrincipal.getUser()));

            List<BoardPositionDto> boardPositionDtos = updateBoardRequest.getBoardPositionList()
                    .stream()
                    .map(BoardPositionRequest::toDto)
                    .toList();

            List<BoardTechStackDto> boardTechStackDtos = updateBoardRequest.getBoardTechStackList()
                    .stream()
                    .map(BoardTechStackRequest::toDto)
                    .toList();
            boardService.updateBoard(boardId, boardDto, boardPositionDtos, boardTechStackDtos);
        } else {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        return ResponseEntity.ok().body(ResponseDto.success());
    }

    //특정 게시글 삭제
    @DeleteMapping("/{board-id}")
    public ResponseEntity<?> deleteBoard(@PathVariable("board-id") @Min(1) Long boardId,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boardService.deleteBoard(boardId, userPrincipal.getUser());
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    //게시글 조회수 증가
    @PatchMapping("/increase-view-count/{board-id}")
    public ResponseEntity<?> increaseViewCount(@PathVariable("board-id") Long boardId) {
        boardService.increaseViewCount(boardId);
        return ResponseEntity.ok()
                .body(ResponseDto.success());
    }

    /**
     * 댓글 기능
     */
    //댓글 리스트 조회
    @GetMapping("/{board-id}/comments")
    public ResponseEntity<?> comments(@PathVariable("board-id") @Min(1) Long boardId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() ->
                        new CustomException(
                                ErrorCode.BOARD_NOT_FOUND,
                                String.format("Board with id %d not found", boardId))
                );

        List<CommentResponse> list = boardService.getComments(boardId)
                .stream()
                .map(CommentResponse::from)
                .toList();
        return ResponseEntity.ok().body(ResponseDto.success(list));
    }

    //댓글 등록
    @PostMapping("/{board-id}/comments")
    public ResponseEntity<?> postComment(@PathVariable("board-id") @Min(1) Long boardId,
                                         @Valid @RequestBody PostCommentRequest postCommentRequest,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boardService.saveComment(postCommentRequest.toDto(
                boardId,
                userPrincipal.getUser(),
                postCommentRequest.getContent())
        );

        return ResponseEntity.ok()
                .body(ResponseDto.success());
    }

    //댓글 삭제
    @DeleteMapping("/{board-id}/comments/{comment-id}")
    public ResponseEntity<?> deleteComment(@PathVariable("board-id") @Min(1) Long boardId,
                                           @PathVariable("comment-id") @Min(1) Long commentId,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {
        boardService.deleteComment(boardId, commentId, userPrincipal.getUser());

        return ResponseEntity.ok()
                .body(ResponseDto.success());
    }


    /**
     * 스크랩 기능
     */
    @PutMapping("/{board-id}/scrap")
    public ResponseEntity<?> putScrap(
            @PathVariable("board-id") Long boardId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        boardService.putScrap(boardId, userPrincipal.getUser());
        return ResponseEntity.ok().body(ResponseDto.success());
    }

    /**
     * 프로젝트 지원 기능
     */
    //프로젝트 지원
    @PostMapping("/{board-id}/apply")
    public ResponseEntity<?> apply(@PathVariable("board-id") Long boardId,
                                   @Valid @RequestBody ApplyRequest applyRequest,
                                   @AuthenticationPrincipal UserPrincipal userPrincipal) {
        applyService.saveApply(applyRequest.toDto(userPrincipal.getUser(), boardId));

        return ResponseEntity.ok()
                .body(ResponseDto.success());
    }

//    //특정 유저가 작성한 프로젝트별 지원자 대기(approve, reject) 리스트
//    @GetMapping("/{board-id}/apply")
//    public ResponseEntity<?> apply
//
//    //프로젝트 작성자의 지원 승인 및 거절
//    @GetMapping("/{board-id}/apply")
//    public ResponseEntity<?>

}
