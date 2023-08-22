package mos.mosback.dto;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

@RequestMapping("/create")
public class StudyGroupRequest {

    private String st_key;
    private String st_title;
    private String st_mode;
    private int st_num;
    private Date st_startDate;
    private Date st_endDate;
    private boolean st_onoff;
    private String st_date;
    private String st_goal;
    private String st_rules;
    private Date st_createDate;
    private int st_click;
    private Date rc_start;
    private Date rc_end;
    private String st_quest;
    private String st_tend;
    private String st_category;

    //Getter, Setter 메소드

    public String getSt_key() {
        return st_key;
    }

    public void setSt_key(String st_key) {
        this.st_key = st_key;
    }

    public String getSt_title() {
        return st_title;
    }

    public void setSt_title(String st_title) {
        this.st_title = st_title;
    }

    public String getSt_mode() {
        return st_mode;
    }

    public void setSt_mode(String st_mode) {
        this.st_mode = st_mode;
    }

    public int getSt_num() {
        return st_num;
    }

    public void setSt_num(int st_num) {
        this.st_num = st_num;
    }

    public Date getSt_startDate() {
        return st_startDate;
    }

    public void setSt_startDate(Date st_startDate) {
        this.st_startDate = st_startDate;
    }

    public Date getSt_endDate() {
        return st_endDate;
    }

    public void setSt_endDate(Date st_endDate) {
        this.st_endDate = st_endDate;
    }

    public boolean isSt_onoff() {
        return st_onoff;
    }

    public void setSt_onoff(boolean st_onoff) {
        this.st_onoff = st_onoff;
    }

    public String getSt_date() {
        return st_date;
    }

    public void setSt_date(String st_date) {
        this.st_date = st_date;
    }

    public String getSt_goal() {
        return st_goal;
    }

    public void setSt_goal(String st_goal) {
        this.st_goal = st_goal;
    }

    public String getSt_rules() {
        return st_rules;
    }

    public void setSt_rules(String st_rules) {
        this.st_rules = st_rules;
    }

    public Date getSt_createDate() {
        return st_createDate;
    }

    public void setSt_createDate(Date st_createDate) {
        this.st_createDate = st_createDate;
    }

    public int getSt_click() {
        return st_click;
    }

    public void setSt_click(int st_click) {
        this.st_click = st_click;
    }

    public Date getRc_start() {
        return rc_start;
    }

    public void setRc_start(Date rc_start) {
        this.rc_start = rc_start;
    }

    public Date getRc_end() {
        return rc_end;
    }

    public void setRc_end(Date rc_end) {
        this.rc_end = rc_end;
    }

    public String getSt_quest() {
        return st_quest;
    }

    public void setSt_quest(String st_quest) {
        this.st_quest = st_quest;
    }

    public String getSt_tend() {
        return st_tend;
    }

    public void setSt_tend(String st_tend) {
        this.st_tend = st_tend;
    }

    public String getSt_category() {
        return st_category;
    }

    public void setSt_category(String st_category) {
        this.st_category = st_category;
    }
}