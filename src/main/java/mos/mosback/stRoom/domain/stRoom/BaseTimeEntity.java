package mos.mosback.stRoom.domain.stRoom;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass //JPA Entity 클래스들이 BaseTimeEntity를 상속할 경우 필드들도 칼럼으로 인식하도록 한다
@EntityListeners(AuditingEntityListener.class) //BaseTimeEntity 클래스에 Auditing 기능을 포함한다
public abstract class BaseTimeEntity {

    @CreatedDate //Entity가 생성되어 저장될때 시간이 자동저장
    private LocalDateTime createdDate;

}
//모든 Entity의 상위 클래스가 되어 엔티티들의 생성시간을 자동으로 관리하는 클래스