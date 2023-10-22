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

}
