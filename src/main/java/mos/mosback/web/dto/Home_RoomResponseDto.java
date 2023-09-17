package mos.mosback.web.dto;
import lombok.Getter;
import mos.mosback.domain.posts.StRoomEntity;
import java.util.Date;


@Getter
public class Home_RoomResponseDto {

    private String title;
    private String category; // 스터디 카테고리
    private int memberNum; //현재 멤버수
    private int maxMember; //모집 멤버수
    private String location;
    private int online; //온라인
    private Date deadline ; //스터디 모집 마감날짜

    //+ 유저프로필사진

    public Home_RoomResponseDto(StRoomEntity entity) {
        this.title = entity.getTitle();
        this.deadline = entity.getDeadline();
        this.location = entity.getLocation();
        this.online = entity.getOnline();
        this.category = entity.getCategory();
        this.memberNum = entity.getMemberNum();
        this.maxMember = entity.getMaxMember();

    }
}
