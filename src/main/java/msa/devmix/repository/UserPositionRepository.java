package msa.devmix.repository;

import msa.devmix.domain.user.User;
import msa.devmix.domain.user.UserPosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserPositionRepository extends JpaRepository<UserPosition, Long> {

    //N+1 문제 해결 O => 페치조인은 연관된 데이터를 같이 끌고올 때 유용하므로 일반 조인 사용
    @Query("SELECT p.positionName FROM UserPosition up JOIN up.position p WHERE up.user = :user")
    List<String> findWithPositionByUser(User user);

    @Query("SELECT up FROM UserPosition up JOIN FETCH up.position WHERE up.user.id = :userId")
    List<UserPosition> findUserPositionWithPositionByUserId(Long userId);
}
