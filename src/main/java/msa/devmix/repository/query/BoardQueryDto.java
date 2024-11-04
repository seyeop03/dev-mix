package msa.devmix.repository.query;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(of = "boardId")
public class BoardQueryDto {
    private Long boardId;
}
