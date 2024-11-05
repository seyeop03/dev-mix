package msa.devmix.service;

import msa.devmix.domain.user.User;
import msa.devmix.dto.*;
import msa.devmix.repository.query.BoardQueryDto;

import java.util.List;

public interface BoardService {
    BoardWithPositionTechStackDto getBoard(Long boardId);
    List<BoardQueryDto> getBoards(int pageNumber, int pageSize);
    void saveBoard(BoardDto boardDto, List<BoardPositionDto> boardPositionDtos, List<BoardTechStackDto> boardTechStackDtos);
    void updateBoard(Long boardId, BoardDto boardDto, List<BoardPositionDto> boardPositionDtos, List<BoardTechStackDto> boardTechStackDtos);
    void deleteBoard(Long boardId, User user);
    void increaseViewCount(Long boardId);
    void putScrap(Long boardNumber, User user);

    List<CommentDto> getComments(Long boardId);
    void saveComment(CommentDto commentDto);
    void deleteComment(Long boardId, Long commentId, User user);
}
