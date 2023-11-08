package mos.mosback.stRoom.dto;
import lombok.Getter;
import lombok.Setter;
import mos.mosback.stRoom.domain.stRoom.StRoomEntity;
import mos.mosback.stRoom.service.ToDoService;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class Home_RoomResponseDto {
    private String title;
    private String category; // 스터디 카테고리
    private int memberNum; //현재 멤버수
    private int maxMember; //모집 멤버수
    private String location; //스터디장소
    private int online; //온라인
    private LocalDate startDate; //스터디 시작날짜
    private LocalDate endDate;
    //+ 유저프로필사진

    public Home_RoomResponseDto(StRoomEntity entity) {
        this.title = entity.getTitle();
        this.startDate = entity.getStartDate();
        this.endDate = entity.getEndDate();
        this.location = entity.getLocation();
        this.online = entity.getOnline();
        this.category = entity.getCategory();
        this.memberNum = entity.getMemberNum();
        this.maxMember = entity.getMaxMember();
    }

}
