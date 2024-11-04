package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import msa.devmix.domain.board.BoardPosition;
import msa.devmix.domain.common.Position;
import msa.devmix.domain.constant.NotificationType;
import msa.devmix.domain.user.User;
import msa.devmix.dto.ApplyDto;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.*;
import msa.devmix.service.ApplyService;
import msa.devmix.service.NotificationService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ApplyServiceImpl implements ApplyService {

    private final ApplyRepository applyRepository;
    private final BoardPositionRepository boardPositionRepository;
    private final NotificationService notificationService;
    private final BoardRepository boardRepository;
    private final PositionRepository positionRepository;

    //== 프로젝트 지원 ==//
    @Transactional
    public void saveApply(ApplyDto dto) {

        //게시글 존재 여부 확인 후 게시글 작성자 리턴
        User user = boardRepository.findById(dto.getBoardId())
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND))
                .getUser();

        //포지션명에 대해 포지션 존재 여부 확인
        Position position = positionRepository.findByPositionName(dto.getPositionName())
                .orElseThrow(() -> new CustomException(ErrorCode.POSITION_NOT_FOUND));

        //해당 게시글에 해당 포지션이 존재하는지 확인
        BoardPosition boardPosition = boardPositionRepository.findByBoardIdAndPosition(dto.getBoardId(), position)
                .orElseThrow(() -> new CustomException(ErrorCode.POSITION_NOT_MATCH));

        //해당 게시글에 해당 포지션의 인원 꽉 찼는지 확인
        if (!boardPosition.isPositionAvailable()) {
            throw new CustomException(ErrorCode.POSITION_FULL);
        }

        applyRepository.save(dto.toEntity(dto.getUser(), boardPosition));

        //프로젝트 게시글 작성자에게 프로젝트 참여 신청 알림
        notificationService.send(
                user,
                NotificationType.POST_PARTICIPATION,
                dto.getUser().getNickname() + "님이 프로젝트 참여 신청했습니다!"
        );
    }
}
