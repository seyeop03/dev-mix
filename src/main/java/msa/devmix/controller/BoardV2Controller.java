package msa.devmix.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.config.oauth.userinfo.UserPrincipal;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.constant.Location;
import msa.devmix.dto.*;
import msa.devmix.dto.request.*;
import msa.devmix.dto.response.*;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.BoardRepository;
import msa.devmix.repository.query.BoardQueryDto;
import msa.devmix.service.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;


@RestController
@RequestMapping("/api/v2/boards")
@Slf4j
@RequiredArgsConstructor
public class BoardV2Controller {

    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final ApplyService applyService;
    private final TechStackService techStackService;
    private final PositionService positionService;

    /**
     * 게시글 기능
     */
    //게시물 상세 조회
    @GetMapping("/{board-id}")
    public ResponseEntity<?> board(@PathVariable("board-id") @Min(1) Long boardId) {

        BoardWithPositionTechStackDto dto = boardService.getBoard(boardId);
        boardService.increaseViewCount(boardId);

        return ResponseEntity.ok()
                .body(ResponseDto.success(BoardWithPositionTechStackResponse.from(dto)));
    }

    //특정 페이지 게시글 리스트 조회
    @GetMapping
    public ResponseEntity<?> boards(@RequestParam(defaultValue = "1") int pageNumber,
                                    @RequestParam(defaultValue = "16") int pageSize) {
        return ResponseEntity.ok().body(ResponseDto.success(
                boardService.getBoards(pageNumber, pageSize)
                        .stream()
                        .map(BoardListResponse::from)
                        .toList()
                )
        );
    }

    //특정 페이지 게시글 리스트 조회 + 스크랩
    @GetMapping("/scrap-list")
    public ResponseEntity<?> boards(@RequestParam(defaultValue = "1") int pageNumber,
                                    @RequestParam(defaultValue = "16") int pageSize,
                                    @AuthenticationPrincipal UserPrincipal userPrincipal) {

        List<BoardQueryDto> bookmarked = boardService.getBookMarkedBoards(userPrincipal.getUser(), pageNumber, pageSize);

        return ResponseEntity.ok().body(ResponseDto.success(bookmarked));
    }

    //게시글 생성
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> postBoard(@Valid @RequestPart(value = "postBoardRequest") PostBoardRequest postBoardRequest,
                                       @AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @RequestPart(value = "boardImage", required = false) MultipartFile boardImage) throws IOException {

        BoardDto boardDto = postBoardRequest.toDto(UserDto.from(userPrincipal.getUser()));

        List<BoardPositionDto> boardPositionDtos = postBoardRequest.getBoardPositionList()
                .stream()
                .map(BoardPositionRequest::toDto)
                .toList();

        List<BoardTechStackDto> boardTechStackDtos = postBoardRequest.getBoardTechStackList()
                .stream()
                .map(BoardTechStackRequest::toDto)
                .toList();

        boardService.saveBoard(boardDto, boardPositionDtos, boardTechStackDtos, boardImage);

        return ResponseEntity.ok().body(ResponseDto.success());
    }

    //게시글 수정
    @PutMapping(value = "/{board-id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> updateBoard(@PathVariable("board-id") @Min(1) Long boardId,
                                         @Valid @RequestPart(value = "updateBoardRequest") UpdateBoardRequest updateBoardRequest,
                                         @RequestPart(value = "boardImage", required = false) MultipartFile boardImage,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {

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
            boardService.updateBoard(boardId, boardDto, boardPositionDtos, boardTechStackDtos, boardImage);
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
        boardService.saveComment(
                postCommentRequest.toDto(
                        boardId,
                        userPrincipal.getUser(),
                        postCommentRequest.getContent()
                )
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

    /**
     * DB 에 존재하는 기술 스택, 포지션, 지역 정보
     */
    //지역 정보 조회
    @GetMapping("/locations")
    public ResponseEntity<?> locations() {
        return ResponseEntity.ok()
                .body(Location.getAllLocations());
    }

    //특정 포지션에 해당하는 기술 스택들 조회
    @GetMapping("/tech-stacks")
    public ResponseEntity<?> techStacks(@RequestParam(required = false) String positionName) {
        return ResponseEntity.ok()
                .body(ResponseDto.success(techStackService.getTechStacksViaPositionName(positionName)
                        .stream()
                        .map(TechStackResponse::from)
                        .toList())
                );
    }

    @GetMapping("/positions")
    public ResponseEntity<?> positions() {

        return ResponseEntity.ok()
                .body(ResponseDto.success(PositionResponse.from(positionService.getPositions())));
    }


}
