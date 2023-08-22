package mos.mosback.controller;

import mos.mosback.dto.StudyGroupRequest;
import mos.mosback.entity.StudyGroup;
import mos.mosback.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@ComponentScan
@RestController
@RequestMapping("/study-groups")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;

    @Autowired
    public StudyGroupController(StudyGroupService studyGroupService) {
        this.studyGroupService = studyGroupService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<StudyGroup> studyGroups = studyGroupService.getAllStudyGroups();
        model.addAttribute("studyGroups", studyGroups);
        return "index"; // 뷰 이름에 확장자 .html 추가
    }
    @GetMapping("/create")
    public String createForm() {
        return "create_form"; // Assuming create_form.html is a Thymeleaf template
    }
    @PostMapping("/create")
    public ResponseEntity<Void> createStudyGroup(@RequestBody StudyGroupRequest studyGroupRequest){

        StudyGroup studyGroup = new StudyGroup();


            studyGroup.setSt_key(studyGroupRequest.getSt_key());
            studyGroup.setSt_title(studyGroupRequest.getSt_title());
            studyGroup.setSt_mode(studyGroupRequest.getSt_mode());
            studyGroup.setSt_num(studyGroupRequest.getSt_num());
            studyGroup.setSt_startDate(studyGroupRequest.getSt_startDate());
            studyGroup.setSt_endDate(studyGroupRequest.getSt_endDate());
            studyGroup.isSt_onoff(studyGroupRequest.isSt_onoff());
            studyGroup.setSt_date(studyGroupRequest.getSt_date());
            studyGroup.setSt_goal(studyGroupRequest.getSt_goal());
            studyGroup.setSt_rules(studyGroupRequest.getSt_rules());
            studyGroup.setSt_createDate(studyGroupRequest.getSt_createDate());
            studyGroup.setSt_click(studyGroupRequest.getSt_click());
            studyGroup.setRc_start(studyGroupRequest.getRc_start());
            studyGroup.setRc_end(studyGroupRequest.getRc_end());
            studyGroup.setSt_quest(studyGroupRequest.getSt_quest());
            studyGroup.setSt_tend(studyGroupRequest.getSt_tend());
            studyGroup.setSt_category(studyGroupRequest.getSt_category());

        studyGroupService.createStudyGroup(studyGroup);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}