package mos.mosback.stRoom.domain.stRoom;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "study_member_room")
public class StudyMemberRoomEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "memberId", referencedColumnName = "memberId")
    private StudyMemberEntity member;


    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "roomId", referencedColumnName = "roomId")
    private StRoomEntity room;

    public MemberStatus getStatus() {
        return status;
    }

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public Long getMemberId() {
        if (member != null) {
            return member.getMemberId();
        }
        return null;
    }

    public Long getRoomId() {
        if (room != null) {
            return room.getRoomId();
        }
        return null;
    }


    public void setMember(StudyMemberEntity member) {
        this.member = member;
    }

    public void setRoom(StRoomEntity room) {
        this.room = room;
    }

    public void setStatus(MemberStatus status){
        this.status = status;
    }


    public StRoomEntity getRoom() {
        return this.room;
    }
}
