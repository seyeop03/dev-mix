package msa.devmix.service;

import msa.devmix.domain.user.User;
import msa.devmix.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {
    BoardWithPositionTechStackDto getBoard(Long boardId);
    Page<BoardDto> getBoards(Pageable pageable);
    void saveBoard(BoardDto boardDto, List<BoardPositionDto> boardPositionDtos, List<BoardTechStackDto> boardTechStackDtos);
    void updateBoard(Long boardId, BoardDto boardDto, List<BoardPositionDto> boardPositionDtos, List<BoardTechStackDto> boardTechStackDtos);
    void deleteBoard(Long boardId, User user);
    void increaseViewCount(Long boardId);
    void putScrap(Long boardNumber, User user);

    List<CommentDto> getComments(Long boardId);
    void saveComment(CommentDto commentDto);
    void deleteComment(Long boardId, Long commentId, User user);
}
