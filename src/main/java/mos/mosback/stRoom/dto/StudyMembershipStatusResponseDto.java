package mos.mosback.stRoom.dto;

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




}