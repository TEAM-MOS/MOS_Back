package mos.mosback.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "study_group")
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(name = "title")
    private String title;

    @Column
    private String mode;

    @Column(name = "num")
    private int num;

    @Column
    private Date startDate;

    @Column
    private Date endDate;

    @Column
    private String date;

    @Column
    private String goal;

    @Column
    private String rules;

    @Column
    private Date createDate;

    @Column
    private Integer click;

    @Column
    private Date rcstart;

    @Column
    private Date rcend;

    @Column
    private String quest;


    @Column
    private String tend;

    @Column
    private String category;

    @Column
    private boolean onoff;



}
