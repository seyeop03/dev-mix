package msa.devmix.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


/**
 * 회원가입을 할 때 인증 서버로부터 인증 정보를 DB에 저장시킬 때 직접 핸들링해주어야 한다.
 * 이를 위해 직접 createdBy, modifiedBy 를 넣어줘야 하는데, 이 때 private 이 아닌 protected 를 해주어야 한다.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends BaseTimeEntity {

    @CreatedBy
    @Column(updatable = false)
    protected String createdBy;

    @LastModifiedBy
    protected String lastModifiedBy;
}
