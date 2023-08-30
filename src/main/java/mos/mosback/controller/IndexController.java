package mos.mosback.controller;
import lombok.RequiredArgsConstructor;
import mos.mosback.service.PostService;
import mos.mosback.web.dto.PostsResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Controller //머스테치에 URL 매핑해주기
public class IndexController {
    private final PostService postService;
    private final HttpSession httpSession;
//    @GetMapping("/")//머스테치 스타터 덕분에
//    // 컨트롤러에서 문자열을 반환할 때 앞의 경로와 뒤의 파일 확정자는 자동으로 지정된다.
//    public String index(Model model, @LoginUser SessionUser user) {
//        model.addAttribute("posts", postService.findAllDesc());
//        if (user != null) {
//            model.addAttribute("userName", user.getName());
//        }
//        return "index";
//    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("posts/update/{id}")
    public  String Update(@PathVariable Long groupID,
                          Model model){

        PostsResponseDto dto = postService.findById(groupID);
        model.addAttribute("post",dto);

        return "posts-update";

    }

}
