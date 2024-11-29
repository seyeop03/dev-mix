package msa.devmix.service;

import msa.devmix.domain.user.User;
import msa.devmix.dto.*;
import msa.devmix.dto.response.BoardListResponseTest;
import msa.devmix.repository.query.BoardQueryDto;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface BoardService {
    BoardWithPositionTechStackDto getBoard(Long boardId);
    List<BoardQueryDto> getBoards(int pageNumber, int pageSize);
    void saveBoard(BoardDto boardDto, List<BoardPositionDto> boardPositionDtos, List<BoardTechStackDto> boardTechStackDtos, MultipartFile boardImage) throws IOException;
    void updateBoard(Long boardId, BoardDto boardDto, List<BoardPositionDto> boardPositionDtos, List<BoardTechStackDto> boardTechStackDtos, MultipartFile boardImage) throws IOException;
    void deleteBoard(Long boardId, User user);
    void increaseViewCount(Long boardId);
    void putScrap(Long boardNumber, User user);

    List<CommentDto> getComments(Long boardId);
    void saveComment(CommentDto commentDto);
    void deleteComment(Long boardId, Long commentId, User user);

    List<BoardListResponseTest> findAllBoards(Pageable pageable);
}
