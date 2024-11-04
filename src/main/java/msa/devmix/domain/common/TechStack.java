package msa.devmix.domain.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
public class TechStack {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tech_stack_id")
    private Long id;

    private String techStackName; //기술 스택 이름
    private String imageUrl; //기술 스택 이미지 URL
}
