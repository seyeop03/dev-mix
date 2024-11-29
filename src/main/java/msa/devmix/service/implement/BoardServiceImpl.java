package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.board.*;
import msa.devmix.domain.common.Position;
import msa.devmix.domain.common.TechStack;
import msa.devmix.domain.constant.Location;
import msa.devmix.domain.constant.NotificationType;
import msa.devmix.domain.constant.RecruitmentStatus;
import msa.devmix.domain.user.User;
import msa.devmix.dto.*;
import msa.devmix.dto.response.BoardListResponseTest;
import msa.devmix.dto.response.BoardPositionListResponseTest;
import msa.devmix.dto.response.BoardTechStackListResponseTest;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.*;
import msa.devmix.repository.query.BoardPositionQueryDto;
import msa.devmix.repository.query.BoardQueryDto;
import msa.devmix.repository.query.BoardTechStackQueryDto;
import msa.devmix.service.BoardService;
import msa.devmix.service.NotificationService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardPositionRepository boardPositionRepository;
    private final PositionRepository positionRepository;
    private final TechStackRepository techStackRepository;
    private final BoardTechStackRepository boardTechStackRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ApplyRepository applyRepository;
    private final ScrapRepository scrapRepository;
    private final NotificationService notificationService;


    /**
     * 게시글 기능
     */
    //게시글 단건 조회
    @Override
    public BoardWithPositionTechStackDto getBoard(Long boardId) {

        BoardDto boardDto = boardRepository
                .findById(boardId)
                .map(BoardDto::from)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND, String.format("boardId: %d", boardId)));

        List<BoardPositionDto> boardPositionDtoList = boardPositionRepository
                .findByBoardId(boardId).stream()
                .map(BoardPositionDto::from)
                .toList();

        List<BoardTechStackDto> boardTechStackDtoList = boardTechStackRepository
                .findByBoardId(boardId).stream()
                .map(BoardTechStackDto::from)
                .toList();

        return BoardWithPositionTechStackDto.of(boardDto, boardPositionDtoList, boardTechStackDtoList);
    }

    //게시글 저장
    @Override
    @Transactional
    public void saveBoard(BoardDto boardDto,
                          List<BoardPositionDto> boardPositionDtos,
                          List<BoardTechStackDto> boardTechStackDtos) {

        Board board = boardDto.toEntity();
        board.setRecruitmentStatus(RecruitmentStatus.RECRUITING);
        board.setUser(userRepository.findById(boardDto.getUserDto().getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND)));
        board.setViewCount(0L);
        board.setCommentCount(0L);

        boardRepository.save(board);

        // boardPosition 저장
        List<String> positionNames = boardPositionDtos.stream()
                .map(BoardPositionDto::getPositionName)
                        .toList();

        List<Position> existingPositions = positionRepository.findByPositionNameIn(positionNames);

        if (positionNames.size() != existingPositions.size()) {
            throw new CustomException(ErrorCode.POSITION_NOT_FOUND);
        }

//        boardPositionDtos
//                .stream()
//                .map(boardPositionDto -> {
//                    if (positionRepository.findByPositionName(boardPositionDto.getPositionName()) != null) {
//                        return boardPositionDto.toEntity(board, positionRepository.findByPositionName(boardPositionDto.getPositionName()));
//                    } else {
//                        throw new CustomException(ErrorCode.POSITION_NOT_FOUND);
//                    }
//                })
//                .forEach(boardPositionRepository::save);
        boardPositionDtos.stream()
                .map(boardPositionDto -> boardPositionDto.toEntity(board, positionRepository.findByPositionName(boardPositionDto.getPositionName()).get(), boardPositionDto.getRequiredCount()))
                .forEach(boardPositionRepository::save);


        // boardTechStack 저장
        List<String> techStackNames = boardTechStackDtos.stream()
                .map(BoardTechStackDto::getTechStackName)
                .toList();

        List<TechStack> existingTechStacks = techStackRepository.findByTechStackNameIn(techStackNames);

        if (techStackNames.size() != existingTechStacks.size()) {
            throw new CustomException(ErrorCode.TECH_STACK_NOT_FOUND);
        }

        boardTechStackDtos.stream()
                .map(boardTechStackDto
                        -> boardTechStackDto.toEntity(
                                board,
                                techStackRepository.findByTechStackName(boardTechStackDto.getTechStackName())))
                .forEach(boardTechStackRepository::save);

    }

    //게시글 수정
    @Override
    @Transactional
    public void updateBoard(Long boardId,
                            BoardDto boardDto,
                            List<BoardPositionDto> boardPositionDtos,
                            List<BoardTechStackDto> boardTechStackDtos) {

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        board.update(boardDto);

        // boardPosition 수정
        // 현재 게시판의 기존 `BoardPosition` 엔티티 목록 가져오기
        List<BoardPosition> existingBoardPositions = boardPositionRepository.findByBoardId(boardId);

       // 새로운 요청 데이터에 맞춰 `BoardPosition` 업데이트 또는 삽입
        for (BoardPositionDto dto : boardPositionDtos) {
            Position position = positionRepository.findByPositionName(dto.getPositionName())
                    .orElseThrow(() -> new CustomException(ErrorCode.POSITION_NOT_FOUND));
            BoardPosition boardPosition = existingBoardPositions.stream()
                    .filter(bp -> bp.getPosition().equals(position))
                    .findFirst()
                    .orElse(null);

            if (boardPosition != null) {
                // 이미 존재하는 경우: 업데이트
                boardPosition.setRequiredCount(dto.getRequiredCount());
                boardPosition.setCurrentCount(dto.getCurrentCount());
            } else {
                // 새로 추가하는 경우: 엔티티 생성 후 저장
                BoardPosition newBoardPosition = dto.toEntity(board, position, dto.getRequiredCount(), dto.getCurrentCount());
                boardPositionRepository.save(newBoardPosition);
            }
        }

        // 기존 리스트에서 새로운 데이터에 없는 항목은 삭제
        List<BoardPosition> deleteBoardPosition  = existingBoardPositions.stream()
                .filter(bp -> boardPositionDtos.stream().noneMatch(dto -> dto.getPositionName().equals(bp.getPosition().getPositionName())))
                .toList();

        boardPositionRepository.deleteAll(deleteBoardPosition);



        // boardTechStack 업데이트
        // 현재 게시판의 기존 `BoardTechStack` 엔티티 목록 가져오기
        List<BoardTechStack> existingBoardTechStack = boardTechStackRepository.findByBoardId(boardId);

        for (BoardTechStackDto dto : boardTechStackDtos) {
            TechStack techStack = techStackRepository.findByTechStackName(dto.getTechStackName());
            BoardTechStack boardTechStack = existingBoardTechStack.stream()
                    .filter(bts -> bts.getTechStack().equals(techStack))
                    .findFirst()
                    .orElse(null);
            // 이미 존재하는 경우 pass

            if (boardTechStack == null) {
                // 새로 추가하는 경우: 엔티티 생성 후 저장
                BoardTechStack newBoardTechStack = dto.toEntity(board, techStack);
                boardTechStackRepository.save(newBoardTechStack);
            }

        }

        // 새로운 데이터에 없는 항목 삭제
        List<BoardTechStack> deleteBoardTechStack = existingBoardTechStack.stream()
                .filter(bts -> boardTechStackDtos.stream()
                        .noneMatch(dto -> dto.getTechStackName().equals(bts.getTechStack().getTechStackName())))
                .toList();
        boardTechStackRepository.deleteAll(deleteBoardTechStack);


    }

    //게시글 삭제
    @Override
    @Transactional
    public void deleteBoard(Long boardId, User user) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        boardRepository.findByIdAndUserId(boardId, user.getId())
                        .orElseThrow(() -> new CustomException(ErrorCode.PERMISSION_DENIED));

        deleteRelatedBoard(boardId);
        boardRepository.deleteById(boardId);
    }

    private void deleteRelatedBoard(Long boardId) {
        // 연관된 엔티티 일괄 삭제
        List<BoardPosition> boardPositionList = boardPositionRepository.findByBoardId(boardId);
        applyRepository.deleteAllByBoardPositionIn(boardPositionList);
        boardTechStackRepository.deleteAllByBoardId(boardId);
        boardPositionRepository.deleteAllByBoardId(boardId);
        scrapRepository.deleteAllByBoardId(boardId);
        commentRepository.deleteAllByBoardId(boardId);
    }

    //게시글 리스트 조회
    @Override
    @Transactional
    public List<BoardQueryDto> getBoards(int pageNumber, int pageSize) {

        // board 조회
        List<BoardQueryDto> boards = boardRepository.findBoards(pageNumber, pageSize);

        // boardIds 가져오기
        List<Long> boardIds = boards.stream().map(BoardQueryDto::getBoardId)
                .toList();

        // boardPosition 가져오기
        Map<Long, List<BoardPositionQueryDto>> boardPositionQueryDtos = boardRepository.findBoardPositionQueryDtos(boardIds);

        boards.forEach(board -> board.setPositions(boardPositionQueryDtos.get(board.getBoardId())));

        // boardTechStack 가져오기
        Map<Long, List<BoardTechStackQueryDto>> boardTechStackQueryDtos = boardRepository.findBoardTechStackQueryDtos(boardIds);

        boards.forEach(board -> board.setTechStacks(boardTechStackQueryDtos.get(board.getBoardId())));

        return boards;
    }

    //게시글 조회수 증가
    /* 게시글을 조회할 때마다 조회수 증가 로직이 함께 실행되면 그 두 기능이 강하게 결합되므로,
        조회수 증가를 독립된 API 로 분리하면 유지보수성이 높아짐 */
    @Transactional
    @Override
    public void increaseViewCount(Long boardId) {
         Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
//         board.increaseViewCount(); //Dirty checking
        boardRepository.increaseViewCount(boardId); //bulk insert
    }

    /**
     * 스크랩 기능
     */
    @Transactional
    @Override
    public void putScrap(Long boardId, User user) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        scrapRepository.findByUserIdAndBoardId(user.getId(), boardId)
                .ifPresentOrElse(
                        scrapRepository::delete,
                        () -> scrapRepository.save(Scrap.of(user, board))
                );
    }

    /**
     * 댓글 기능
     */
    //댓글 리스트 조회 => fetch join 으로 N+1 문제 해결
    public List<CommentDto> getComments(Long boardId) {

        return commentRepository.findByBoardId(boardId)
                .stream()
                .map(CommentDto::from)
                .toList();
    }

    //댓글 등록
    @Transactional
    public void saveComment(CommentDto dto) {
        Board board = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        commentRepository.save(dto.toEntity(board, dto.getUser()));
        board.increaseCommentCount();

        notificationService.send(
            board.getUser(),
            NotificationType.POST_COMMENT,
            dto.getUser().getNickname()+ "님이 댓글을 등록했습니다!"
        );
    }

    //댓글 삭제
    @Transactional
    public void deleteComment(Long boardId, Long commentId, User user) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.PERMISSION_DENIED);
        }

        commentRepository.delete(comment);
        board.decreaseCommentCount();
    }

    //N+1 이슈 해결하기 전 로직
    @Override
    public List<BoardListResponseTest> findAllBoards(Pageable pageable) {
        Page<Board> boards = boardRepository.findAll(pageable);

        List<Long> boardIds = boards.stream()
                .map(Board::getId)
                .toList();


//        List<BoardPosition> boardPositions = boardIds.stream()
//                .map(boardId -> boardPositionRepository.findById(boardId)
//                        .orElseThrow(() -> new CustomException(ErrorCode.BOARD_POSITION_NOT_FOUND)))
//                .toList();
//
//        List<BoardTechStack> boardTechStacks = boardIds.stream()
//                .map(boardId -> boardTechStackRepository.findById(boardId)
//                        .orElseThrow(() -> new CustomException(ErrorCode.TECH_STACK_NOT_FOUND)))
//                .toList();

        List<BoardListResponseTest> boardListResponseTests = boards.stream()
                .map(board -> BoardListResponseTest.of(
                        board.getId(),
                        board.getTitle(),
                        board.getCreatedBy(),
                        board.getViewCount(),
                        board.getCommentCount(),
                        board.getRecruitEndDate(),
                        board.getLocation().toString()))
                .toList();

//        Map<Long, List<BoardPositionListResponseTest>> boardPositionMap = BoardPositionListResponseTest.from(boardPositions)
//                .stream()
//                .collect(Collectors.groupingBy(BoardPositionListResponseTest::getBoardId));

        Map<Long, List<BoardPositionListResponseTest>> boardPositionMap = boardIds.stream()
                .flatMap(boardId -> BoardPositionListResponseTest.from(boardPositionRepository.findByBoardId(boardId)).stream())
                .collect(Collectors.groupingBy(BoardPositionListResponseTest::getBoardId));

        Map<Long, List<BoardTechStackListResponseTest>> boardTechStackMap = boardIds.stream()
                .flatMap(boardId -> BoardTechStackListResponseTest.from(boardTechStackRepository.findByBoardId(boardId)).stream())
                .collect(Collectors.groupingBy(BoardTechStackListResponseTest::getBoardId));

        boardListResponseTests.forEach(boardListResponseTest ->
                boardListResponseTest.setPositions(boardPositionMap.get(boardListResponseTest.getBoardId())));

        boardListResponseTests.forEach(boardListResponseTest ->
                boardListResponseTest.setTechStacks(boardTechStackMap.get(boardListResponseTest.getBoardId())));

//        Map<Long, List<BoardTechStackListResponseTest>> boardTechStackMap = BoardTechStackListResponseTest.from(boardTechStacks)
//                .stream().collect(Collectors.groupingBy(BoardTechStackListResponseTest::getBoardId));

//        boardListResponseTests.forEach(boardListResponseTest ->
//                boardListResponseTest.setPositions(boardPositionMap.get(boardListResponseTest.getBoardId())));

//        boardListResponseTests.forEach(boardListResponseTest ->
//                boardListResponseTest.setTechStacks(boardTechStackMap.get(boardListResponseTest.getBoardId())));

        return boardListResponseTests;
    }

//    @Override
//    public List<BoardListResponseTest> findAllBoards(Pageable pageable) {
//
//        Page<Board> boards = boardRepository.findAll(pageable);
//
//        List<Long> boardIds = boards.stream()
//                .map(Board::getId)
//                .toList();
//
//        /**
//         * boardPosition
//         */
//        List<BoardPosition> boardPositions = boardIds.stream()
//                .map(boardId -> boardPositionRepository.findById(boardId)
//                        .orElseThrow(() -> new CustomException(ErrorCode.POSITION_NOT_FOUND)))
//                .toList();
//
//        List<BoardTechStack> boardTechStacks = boardIds.stream()
//                .map(boardId -> boardTechStackRepository.findById(boardId)
//                        .orElseThrow(() -> new CustomException(ErrorCode.TECH_STACK_NOT_FOUND)))
//                .toList();
//
//        List<BoardListResponseTest> boardListResponseTests = boards.stream()
//                .map(board -> BoardListResponseTest.of(
//                        board.getId(),
//                        board.getTitle(),
//                        board.getCreatedBy(),
//                        board.getViewCount(),
//                        board.getCommentCount(),
//                        board.getRecruitEndDate(),
//                        board.getLocation().toString()))
//                .toList();
//
//        Map<Long, List<BoardPositionListResponseTest>> boardPositionMap = BoardPositionListResponseTest.from(boardPositions)
//                .stream()
//                .collect(Collectors.groupingBy(BoardPositionListResponseTest::getBoardId));
//
//        Map<Long, List<BoardTechStackListResponseTest>> boardTechStackMap = BoardTechStackListResponseTest.from(boardTechStacks)
//                .stream().collect(Collectors.groupingBy(BoardTechStackListResponseTest::getBoardId));
//
//        boardListResponseTests.forEach(boardListResponseTest ->
//                boardListResponseTest.setPositions(boardPositionMap.get(boardListResponseTest.getBoardId())));
//
//        boardListResponseTests.forEach(boardListResponseTest ->
//                boardListResponseTest.setTechStacks(boardTechStackMap.get(boardListResponseTest.getBoardId())));
//
//        return boardListResponseTests;
//    }



}
