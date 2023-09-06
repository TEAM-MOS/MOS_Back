package mos.mosback;

import com.fasterxml.jackson.databind.ObjectMapper;
import mos.mosback.web.dto.StudyGroupRequest;
import mos.mosback.data.entity.StudyGroup;
import mos.mosback.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@ComponentScan
@Controller
@RequestMapping("/study-groups")
public class StudyGroupController {

    private final StudyGroupService studyGroupService;
    private final ObjectMapper objectMapper;

    @Autowired
    public StudyGroupController(StudyGroupService studyGroupService, ObjectMapper objectMapper) {
        this.studyGroupService = studyGroupService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<StudyGroup> studyGroups = studyGroupService.getAllStudyGroups();
        model.addAttribute("studyGroups", studyGroups);
        return "index"; // 뷰 이름에 확장자 .html 추가
    }
    @GetMapping("/create")
    public String createForm(Model model) {
        List<StudyGroup> studyGroups = studyGroupService.getAllStudyGroups();
        model.addAttribute("studyGroups", studyGroups);
        return "create_form"; // Assuming create_form.html is a Thymeleaf template
    }

    @PostMapping("/create")
    public ResponseEntity<String> createStudyGroup(@RequestBody StudyGroupRequest studyGroupRequest) {
        try {
            StudyGroup studyGroup = objectMapper.convertValue(studyGroupRequest, StudyGroup.class);
            studyGroupService.createStudyGroup(studyGroup);
            return new ResponseEntity<>("Study group created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to create study group", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}