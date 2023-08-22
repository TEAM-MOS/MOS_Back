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
@Controller
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
        return "index.html"; // 뷰 이름에 확장자 .html 추가
    }

    @GetMapping("/create")
    public String createForm() {
        return "create_form.html"; // 뷰 이름에 확장자 .html 추가
    }


    @PostMapping("/create")
    public ResponseEntity<Void> createStudyGroup(@RequestParam(required = false) String st_key,
                                                 @RequestParam(required = false) String st_title,
                                                 @RequestParam(required = false) String st_mode,
                                                 @RequestParam(required = false) Integer st_num,
                                                 @RequestParam(required = false) Date st_startDate,
                                                 @RequestParam(required = false) Date st_endDate,
                                                 @RequestParam(required = false) Boolean st_onoff,
                                                 @RequestParam(required = false) String st_date,
                                                 @RequestParam(required = false) String st_goal,
                                                 @RequestParam(required = false) String st_rules,
                                                 @RequestParam(required = false) Date st_createDate,
                                                 @RequestParam(required = false) Integer st_click,
                                                 @RequestParam(required = false) Date rc_start,
                                                 @RequestParam(required = false) Date rc_end,
                                                 @RequestParam(required = false) String st_quest,
                                                 @RequestParam(required = false) String st_tend,
                                                 @RequestParam(required = false) String st_category,
                                                 @RequestBody(required = false) StudyGroupRequest studyGroupRequest) {

        StudyGroup studyGroup = new StudyGroup();

        if (studyGroupRequest != null) {
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
        } else {
            studyGroup.setSt_key(groupName);
            studyGroup.setSubject(subject);
            studyGroup.setLocation(location);
            studyGroup.setMaxMembers(maxMembers);
        }

        studyGroupService.createStudyGroup(studyGroup);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}