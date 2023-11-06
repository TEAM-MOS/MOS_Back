package mos.mosback.stRoom.dto;

import lombok.Data;
import mos.mosback.stRoom.domain.stRoom.MemberStatus;

@Data
public class StudyMemberHistoryDto {
    private MemberStatus status; // 스터디 상태
    private String title; // 스터디 제목
}
