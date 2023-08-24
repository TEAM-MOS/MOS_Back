package mos.mosback.entity;


import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "study_group")
public class StudyGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long key;

    @Column(name = "title",nullable = false)
    private String title;

    @Column(nullable = false)
    private String mode;

    @Column(name = "num",nullable = false)
    private int num;

    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private Date endDate;

    @Column(nullable = false)
    private String date;

    @Column(nullable = false)
    private String goal;

    @Column(nullable = false)
    private String rules;

    @Column(nullable = false)
    private Date createDate;

    @Column(nullable = false)
    private Integer click;

    @Column(nullable = false)
    private Date rcstart;

    @Column(nullable = false)
    private Date rcend;

    @Column(nullable = false)
    private String quest;


    @Column(nullable = false)
    private String tend;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private boolean onoff;



}
