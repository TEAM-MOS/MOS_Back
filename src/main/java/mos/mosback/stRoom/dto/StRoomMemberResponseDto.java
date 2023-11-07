package mos.mosback.stRoom.dto;

import lombok.Data;
import mos.mosback.stRoom.domain.stRoom.MemberStatus;

@Data
public class StRoomMemberResponseDto {
    private Long memberId;
    private MemberStatus status;


}
