package mos.mosback.controller;

import mos.mosback.dto.StudyGroupRequest;
import mos.mosback.entity.StudyGroup;
import mos.mosback.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/study-groups")
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
        return "index";
    }

    @GetMapping("/create")
    public String createForm() {
        return "create_form";
    }

    @PostMapping("/create")
    public ResponseEntity<Void> createStudyGroup(@RequestParam(required = false) String groupName,
                                                 @RequestParam(required = false) String subject,
                                                 @RequestParam(required = false) String location,
                                                 @RequestParam(required = false) Integer maxMembers,
                                                 @RequestBody(required = false) StudyGroupRequest studyGroupRequest) {

        StudyGroup studyGroup = new StudyGroup();

        if (studyGroupRequest != null) {
            studyGroup.setGroupName(studyGroupRequest.getGroupName());
            studyGroup.setSubject(studyGroupRequest.getSubject());
            studyGroup.setLocation(studyGroupRequest.getLocation());
            studyGroup.setMaxMembers(studyGroupRequest.getMaxMembers());
        } else {
            studyGroup.setGroupName(groupName);
            studyGroup.setSubject(subject);
            studyGroup.setLocation(location);
            studyGroup.setMaxMembers(maxMembers);
        }

        studyGroupService.createStudyGroup(studyGroup);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }



}