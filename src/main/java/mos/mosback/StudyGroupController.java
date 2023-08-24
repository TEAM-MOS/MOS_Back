package mos.mosback;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            model.addAttribute("errorCode", statusCode);
            // 추가적인 오류 처리 로직을 여기에 작성할 수 있습니다.
        }
        return "error"; // error.html 템플릿을 생성하고 해당 템플릿을 이용하여 오류 정보를 출력
    }

}