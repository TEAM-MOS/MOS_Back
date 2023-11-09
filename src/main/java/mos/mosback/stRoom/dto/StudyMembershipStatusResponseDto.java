package mos.mosback.stRoom.dto;

import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.domain.stRoom.StudyMemberEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StudyMembershipStatusResponseDto {


    private String title;
    private String status;

    public StudyMembershipStatusResponseDto(String title, String status) {
        this.title = title;
        this.status = status;
    }


    public String getTitle() {
        return title;
    }

    public String getStatus() {
        return status;
    }


    public void setMemberId(Long memberId) {
    }

    public void setRoomId(Long roomId) {
    }

    public void setStatus(String toString) {
    }
}