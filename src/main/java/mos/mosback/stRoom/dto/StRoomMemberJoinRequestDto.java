package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class StRoomMemberJoinRequestDto {
    private Long roomID;
    private String answer;
    private String email;


} //스터디 수정 시 생성 필드에 들어가는 내용수정
