package mos.mosback.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "study_group")
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String st_key;

    @Column(name = "st_title")
    private String st_title;

    @Column
    private String st_mode;

    @Column(name = "st_num")
    private int st_num;

    @Column
    private String subject;

    @Column
    private Date st_startDate;

    @Column
    private Date st_endDate;

    @Column
    private String st_date;

    @Column
    private String st_goal;

    @Column
    private String st_rules;

    @Column
    private Date st_createDate;

    @Column
    private Integer st_click;

    @Column
    private Date rc_start;

    @Column
    private Date rc_end;

    @Column
    private String st_quest;


    @Column
    private String st_tend;

    @Column
    private String st_category;


    public void setSt_key(String stKey) {
    }

    public void setSt_title(String stTitle) {
    }

    public void setSt_mode(String stMode) {
    }

    public void setSt_num(int stNum) {
    }

    public void setSt_startDate(Date stStartDate) {
    }

    public void setSt_endDate(Date stEndDate) {
    }

    public void isSt_onoff(boolean stOnoff) {
    }

    public void setSt_date(String stDate) {
    }

    public void setSt_goal(String stGoal) {
    }

    public void setSt_rules(String stRules) {
    }

    public void setSt_createDate(Date stCreateDate) {
    }

    public void setSt_click(int stClick) {
    }

    public void setRc_start(Date rcStart) {
    }

    public void setRc_end(Date rcEnd) {
    }

    public void setSt_quest(String stQuest) {
    }

    public void setSt_tend(String stTend) {
    }

    public void setSt_category(String stCategory) {
    }
}
